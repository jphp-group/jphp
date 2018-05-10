<?php
namespace httpclient;

use Closure;
use php\concurrent\Promise;
use php\format\JsonProcessor;
use php\format\ProcessorException;
use php\io\File;
use php\io\FileStream;
use php\io\IOException;
use php\io\MemoryStream;
use php\io\Stream;
use php\jsoup\Jsoup;
use php\lang\Thread;
use php\lang\ThreadPool;
use php\lib\fs;
use php\lib\reflect;
use php\lib\str;
use php\net\Proxy;
use php\net\SocketException;
use php\net\URL;
use php\net\URLConnection;
use php\time\Timer;
use php\xml\DomDocument;
use php\xml\XmlProcessor;

/**
 * Class HttpClient
 */
class HttpClient
{
    const CRLF = "\r\n";

    /**
     * @var string
     */
    public $baseUrl;

    /**
     * @var string
     */
    public $userAgent = 'JPHP Http Client';

    /**
     * @var string
     */
    public $referrer = '';

    /**
     * @var bool
     */
    public $followRedirects = true;

    /**
     * @var string
     */
    public $connectTimeout = '15s';

    /**
     * @var string
     */
    public $readTimeout = '0';

    /**
     * HTTP, SOCKS,
     * @var string
     */
    public $proxyType = 'HTTP';

    /**
     * @var string
     */
    public $proxy;

    /**
     * URLENCODE, MULTIPART, JSON, TEXT, XML, RAW
     * @var string
     */
    public $requestType = 'URLENCODE';

    /**
     * JSON, TEXT, XML, JSOUP
     * @var string
     */
    public $responseType = 'TEXT';

    /**
     * @var array|mixed
     */
    public $body = null;

    /**
     * @var string
     */
    public $encoding = 'UTF-8';

    /**
     * @var array
     */
    public $cookies = [];

    /**
     * @var array
     */
    public $headers = [];

    /**
     * @var null|callable
     */
    public $bodyParser = null;

    /**
     * @var callable[]
     */
    public $handlers = [];

    /**
     * @var string
     */
    protected $_boundary;

    /**
     * @var ThreadPool
     */
    protected $_threadPool;

    /**
     * HttpClient constructor.
     * @param string $baseUrl
     */
    public function __construct(string $baseUrl = '')
    {
        $this->_boundary = Str::random(90);
        $this->baseUrl = $baseUrl;
    }

    /**
     * @param null|ThreadPool $pool
     */
    public function setThreadPool(?ThreadPool $pool): void
    {
        $this->_threadPool = $pool;
    }

    /**
     * @param string $url
     * @param array $args
     * @return HttpResponse
     */
    public function get(string $url, array $args = []): HttpResponse
    {
        return $this->execute('GET', $url, $args);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return HttpResponse
     */
    public function post(string $url, $body = null): HttpResponse
    {
        return $this->execute('POST', $url, $body);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return HttpResponse
     */
    public function put(string $url, $body = null): HttpResponse
    {
        return $this->execute('PUT', $url, $body);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return HttpResponse
     */
    public function patch(string $url, $body = null): HttpResponse
    {
        return $this->execute('PATCH', $url, $body);
    }

    /**
     * @param string $url
     * @param array $args
     * @return HttpResponse
     */
    public function delete(string $url, array $args = []): HttpResponse
    {
        return $this->execute('DELETE', $url, $args);
    }

    /**
     * @param string $url
     * @param array $args
     * @return HttpResponse
     */
    public function options(string $url, array $args = []): HttpResponse
    {
        return $this->execute('OPTIONS', $url, $args);
    }

    /**
     * @param string $url
     * @param array $args
     * @return HttpResponse
     */
    public function head(string $url, array $args = []): HttpResponse
    {
        return $this->execute('HEAD', $url, $args);
    }

    /**
     * @non-getters
     * @param string $url
     * @param array $args
     * @return Promise
     */
    public function getAsync(string $url, array $args = []): Promise
    {
        return $this->executeAsync('GET', $url, $args);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return Promise
     */
    public function postAsync(string $url, $body = null): Promise
    {
        return $this->executeAsync('POST', $url, $body);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return Promise
     */
    public function putAsync(string $url, $body = null): Promise
    {
        return $this->executeAsync('PUT', $url, $body);
    }

    /**
     * @param string $url
     * @param mixed $body
     * @return Promise
     */
    public function patchAsync(string $url, $body = null): Promise
    {
        return $this->executeAsync('PATCH', $url, $body);
    }

    /**
     * @param string $url
     * @param array $args
     * @return Promise
     */
    public function deleteAsync(string $url, array $args = []): Promise
    {
        return $this->executeAsync('DELETE', $url, $args);
    }

    /**
     * @param string $url
     * @param array $args
     * @return Promise
     */
    public function optionsAsync(string $url, array $args = []): Promise
    {
        return $this->executeAsync('OPTIONS', $url, $args);
    }

    /**
     * @param string $url
     * @param array $args
     * @return Promise
     */
    public function headAsync(string $url, array $args = []): Promise
    {
        return $this->executeAsync('HEAD', $url, $args);
    }

    /**
     * @param HttpRequest $request
     * @param null|ThreadPool $threadPool
     * @return Promise
     */
    public function sendAsync(HttpRequest $request, ?ThreadPool $threadPool = null): Promise
    {
        return new Promise(function ($resolve, $reject) use ($threadPool, $request) {
            $handler = function () use ($resolve, $reject, $request) {
                try {
                    $resolve($this->send($request));
                } catch (\Throwable $e) {
                    $reject($e);
                }
            };

            $threadPool = $this->_threadPool ?: $threadPool;

            if ($threadPool) {
                $threadPool->execute($handler);
            } else {
                (new Thread($handler))->start();
            }
        });
    }

    /**
     * @param string $method
     * @param string $url
     * @param null $body
     * @return Promise
     * @internal param array|mixed $data
     */
    public function executeAsync(string $method, string $url, $body = null): Promise
    {
        return $this->sendAsync(new HttpRequest($method, $url, [], $body));
    }

    /**
     * @param HttpRequest $request
     * @return HttpResponse
     */
    public function send(HttpRequest $request): HttpResponse
    {
        /**
         * handlers...
         */
        $originRequest = $request;
        $request = clone $request;

        /** @var URLConnection $connect */
        $connect = null;
        $body = null;

        $existsBody = false;
        $url = $request->url();
        $data = $request->body();
        $method = $request->method();

        $request->cookies(flow($this->cookies, $request->cookies())->toMap());

        switch ($request->method()) {
            case 'PUT':
            case 'POST':
            case 'PATCH':
                $existsBody = true;
                break;

            default:
                $data = flow((array)$this->body)->append((array) $data)->toMap();

                if ($data) {
                    $url .= "?" . $this->formatUrlencode($data);
                }

                break;
        }

        $proxy = null;

        if ($this->proxy) {
            list($proxyHost, $proxyPort) = str::split($this->proxy, ':', 2);
            $proxy = new Proxy($this->proxyType, $proxyHost, $proxyPort);
        }

        if (!$request->absoluteUrl()) {
            $url = "{$this->baseUrl}{$url}";
            $request->url($url);
        }

        $connect = URLConnection::create($url, $proxy);

        $connect->connectTimeout = Timer::parsePeriod($this->connectTimeout);
        $connect->followRedirects = $this->followRedirects;
        $connect->readTimeout = Timer::parsePeriod($this->readTimeout);
        $connect->requestMethod = $method;
        $connect->doInput = true;
        $connect->doOutput = true;


        $headers = [];

        if ($this->referrer) {
            $headers['Referrer'] = $this->referrer;
        }

        $headers['User-Agent'] = $request->userAgent() ?? $this->userAgent;

        if ($existsBody) {
            switch ($request->type() ?: $this->requestType) {
                case 'NONE':
                    break;

                case 'JSON':
                    $headers['Content-Type'] = "application/json; charset=UTF-8";
                    $data = flow((array)$this->body)->append((array)$data)->withKeys()->toArray();
                    $body = (new JsonProcessor())->format($data);
                    break;

                case 'TEXT':
                    $headers['Content-Type'] = "text/html; charset=$this->encoding";
                    $body = $data !== null ? "$data" : "$this->body";
                    break;

                case 'XML':
                    $headers['Content-Type'] = "text/xml; charset=$this->encoding";

                    if ($data instanceof DomDocument) {
                        $xml = new XmlProcessor();
                        $data = $xml->format($data);
                    }

                    $body = $data !== null ? "$data" : "$this->body";
                    break;

                case 'URLENCODE':
                    $headers['Cache-Control'] = 'no-cache';
                    $headers['Content-Type'] = 'application/x-www-form-urlencoded';

                    $data = flow((array)$this->body)->append((array)$data)->withKeys()->toArray();

                    $body = $this->formatUrlencode($data);
                    break;

                case 'MULTIPART':
                    $headers['Cache-Control'] = 'no-cache';
                    $headers['Content-Type'] = 'multipart/form-data; boundary=' . $this->_boundary;

                    $data = flow((array)$this->body)->append((array)$data)->withKeys()->toArray();
                    $body = $this->formatMultipart($data);
                    break;

                case 'STREAM':
                case 'RAW':
                    if ($data instanceof Stream) {
                        $body = $data;
                    } else {
                        $body = new MemoryStream($data);
                    }

                    break;
            }
        }


        $cookie = [];
        foreach ($request->cookies() as $name => $value) {
            $value = URL::encode($value, $this->encoding);
            $cookie[] = "$name=$value";
        }

        if ($cookie) {
            $connect->setRequestProperty('Cookie', str::join($cookie, '; '));
        }

        $request->headers(flow($headers, $this->headers, $request->headers())->toMap());
        foreach ($request->headers() as $name => $value) {
            $connect->setRequestProperty($name, $value);
        }

        foreach ((array) $this->handlers as $handler) {
            if (is_callable($handler)) {
                $handler($request, $originRequest);
            }
        }

        return $this->connect($request, $url, $connect, $body);
    }

    /**
     * @param string $method
     * @param string $url
     * @param mixed $body
     * @return HttpResponse
     */
    public function execute(string $method, string $url, $body = null): HttpResponse
    {
        return $this->send(new HttpRequest($method, $url, [], $body));
    }

    protected function connect(HttpRequest $request, $fullUrl, URLConnection $connection, $body): HttpResponse
    {
        $response = new HttpResponse();

        try {
            if ($body) {
                if ($body instanceof Stream) {
                    $readFully = $body->readFully();
                    $connection->setRequestProperty('Content-Length', str::length($readFully));
                    $connection->getOutputStream()->write($readFully);
                    $readFully = null;
                } else {
                    $connection->setRequestProperty('Content-Length', str::length($body));
                    $connection->getOutputStream()->write($body);
                }
            }

            $connection->connect();
            $inStream = $connection->getInputStream();
        } catch (IOException $e) {
            $inStream = $connection->getErrorStream();
        } catch (SocketException $e) {
            $response->statusCode(500);
            $response->statusMessage($e->getMessage());
            $inStream = new MemoryStream();
        } catch (\Exception $e) {
            $response->statusCode(599);
            $response->statusMessage($e->getMessage());
            $inStream = new MemoryStream();
        }

        $body = null;

        try {
            switch ($request->responseType() ?: $this->responseType) {
                case 'JSON':
                    $data = $inStream->readFully();
                    try {
                        $body = (new JsonProcessor(JsonProcessor::DESERIALIZE_AS_ARRAYS))->parse($data);
                    } catch (ProcessorException $e) {
                        $response->statusCode(400);
                        $response->statusMessage($e->getMessage());
                    }

                    break;

                case 'TEXT':
                    $data = $inStream->readFully();
                    $body = $data;
                    break;

                case 'XML':
                    try {
                        $data = $inStream->readFully();
                        $body = (new XmlProcessor())->parse($data);
                    } catch (ProcessorException $e) {
                        $response->statusCode(400);
                        $response->statusMessage($e->getMessage());
                    }
                    break;

                case 'STREAM':
                    $body = $inStream;
                    break;

                case 'JSOUP':
                    if (class_exists(Jsoup::class)) {
                        $body = Jsoup::parse($inStream, $this->encoding, $fullUrl);
                        break;
                    } else {
                        throw new \Exception("Response Type cannot be JSoup, JSoup extension is not loaded");
                    }
            }

            if ($bodyParser = ($request->bodyParser() ?: $this->bodyParser)) {
                $body = $bodyParser($body, $response);
            }

            $response->body($body);
        } catch (SocketException | IOException $e) {
            $response->statusCode(500);
            $response->statusMessage($e->getMessage());
        }

        try {
            if ($response->statusCode() != 500) {
                $response->statusCode($connection->responseCode);
                $response->statusMessage($connection->responseMessage);
                $response->headers($connection->getHeaderFields());
            }
        } catch (IOException $e) {
            $response->statusCode(500);
            $response->statusMessage($e->getMessage());
        } catch (SocketException $e) {
            $response->statusCode(500);
            $response->statusMessage($e->getMessage());
        } catch (\Exception $e) {
            $response->statusCode(599);
            $response->statusMessage($e->getMessage());
        }

        return $response;
    }

    /**
     * @param array $data
     * @param string $prefix
     * @return string
     */
    protected function formatUrlencode(array $data, $prefix = '')
    {
        $str = [];

        foreach ($data as $code => $value) {
            if (is_array($value)) {
                $str[] = $this->formatUrlencode($value, $prefix ? "{$prefix}[$code]" : $code);
            } else {
                if ($prefix) {
                    $str[] = "{$prefix}[$code]=" . URL::encode($value);
                } else {
                    $str[] = "$code=" . URL::encode($value);
                }
            }
        }

        return str::join($str, '&');
    }

    protected function formatMultipart(array $data, $prefix = '')
    {
        $streams = [];

        $out = new MemoryStream();

        foreach ($data as $name => $value) {
            if ($value instanceof File) {
                $streams[$name] = new FileStream($value);
            } else if ($value instanceof Stream) {
                $streams[$name] = $value;
            } else {
                $out->write("--");
                $out->write($this->_boundary);
                $out->write(self::CRLF);

                $name = URL::encode($name);
                $out->write("Content-Disposition: form-data; name=\"$name\"");
                $out->write(self::CRLF);

                $out->write("Content-Type: text/plain; charset={$this->encoding}");
                $out->write(self::CRLF);
                $out->write(self::CRLF);

                $out->write("$value");
                $out->write(self::CRLF);
            }
        }

        foreach ($streams as $name => $stream) {
            /** @var Stream $stream */
            $out->write("--");
            $out->write($this->_boundary);
            $out->write(self::CRLF);

            $name = URL::encode($name);
            $out->write("Content-Disposition: form-data; name=\"$name\"; filename=\"");
            $out->write(URL::encode(fs::name($stream->getPath())) . "\"");
            $out->write(self::CRLF);

            $out->write("Content-Type: ");
            $out->write(URLConnection::guessContentTypeFromName($stream->getPath()));
            $out->write(self::CRLF);

            $out->write("Content-Transfer-Encoding: binary");
            $out->write(self::CRLF);
            $out->write(self::CRLF);

            $out->write($stream->readFully());
            $out->write(self::CRLF);

            $stream->close();
        }

        $out->write("--$this->_boundary--");
        $out->write(self::CRLF);
        $out->seek(0);

        return $out;
    }
}
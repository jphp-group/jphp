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
use php\net\URLConnection;
use php\time\Timer;
use php\xml\DomDocument;
use php\xml\XmlProcessor;

/**
 * Class HttpClient
 *
 * @property string $baseUrl
 * @property string $userAgent
 * @property string $referrer
 * @property bool $followRedirects
 * @property string $connectTimeout
 * @property string $readTimeout
 * @property string $proxyType
 * @property string $proxy
 * @property string $requestType
 * @property string $responseType
 * @property array $data
 * @property array $cookies
 * @property array $headers
 * @property string $encoding
 * @property callable|null $bodyParser
 */
class HttpClient
{
    const CRLF = "\r\n";

    /**
     * @var string
     */
    private $baseUrl;

    /**
     * @var string
     */
    private $userAgent = 'JPHP Http Client';

    /**
     * @var string
     */
    private $referrer = '';

    /**
     * @var bool
     */
    private $followRedirects = true;

    /**
     * @var string
     */
    private $connectTimeout = '15s';

    /**
     * @var string
     */
    private $readTimeout = '0';

    /**
     * HTTP, SOCKS,
     * @var string
     */
    private $proxyType = 'HTTP';

    /**
     * @var string
     */
    private $proxy;

    /**
     * URLENCODE, MULTIPART, JSON, TEXT, XML
     * @var string
     */
    private $requestType = 'URLENCODE';

    /**
     * JSON, TEXT, XML, JSOUP
     * @var string
     */
    private $responseType = 'TEXT';

    /**
     * @var array
     */
    private $data = [];

    /**
     * @var string
     */
    private $encoding = 'UTF-8';

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
    private $bodyParser = null;

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
     * @param mixed $data
     * @return HttpResponse
     */
    public function get(string $url, $data = null): HttpResponse
    {
        return $this->execute('GET', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return HttpResponse
     */
    public function post(string $url, $data = null): HttpResponse
    {
        return $this->execute('POST', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return HttpResponse
     */
    public function put(string $url, $data = null): HttpResponse
    {
        return $this->execute('PUT', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return HttpResponse
     */
    public function patch(string $url, $data = null): HttpResponse
    {
        return $this->execute('PATCH', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return HttpResponse
     */
    public function delete(string $url, $data = null): HttpResponse
    {
        return $this->execute('DELETE', $url, $data);
    }

    /**
     * @non-getters
     * @param string $url
     * @param mixed $data
     * @return Promise
     */
    public function getAsync(string $url, $data = null): Promise
    {
        return $this->executeAsync('GET', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return Promise
     */
    public function postAsync(string $url, $data = null): Promise
    {
        return $this->executeAsync('POST', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return Promise
     */
    public function putAsync(string $url, $data = null): Promise
    {
        return $this->executeAsync('PUT', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return Promise
     */
    public function patchAsync(string $url, $data = null): Promise
    {
        return $this->executeAsync('PATCH', $url, $data);
    }

    /**
     * @param string $url
     * @param mixed $data
     * @return Promise
     */
    public function deleteAsync(string $url, $data = null): Promise
    {
        return $this->executeAsync('DELETE', $url, $data);
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
     * @param array|mixed $data
     * @return Promise
     */
    public function executeAsync(string $method, string $url, $data = null): Promise
    {
        return $this->sendAsync(new HttpRequest($method, $url, $data));
    }

    /**
     * @param HttpRequest $request
     * @return HttpResponse
     */
    public function send(HttpRequest $request): HttpResponse
    {
        /** @var URLConnection $connect */
        $connect = null;
        $body = null;

        $existsBody = false;
        $url = $request->url();
        $data = $request->data();
        $method = $request->method();

        switch ($request->method()) {
            case 'PUT':
            case 'POST':
            case 'PATCH':
                $existsBody = true;
                break;

            default:
                $data = flow((array)$this->data)->append((array) $data)->toMap();

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
                    $data = flow((array)$this->data)->append((array)$data)->withKeys()->toArray();
                    $body = (new JsonProcessor())->format($data);
                    break;

                case 'TEXT':
                    $headers['Content-Type'] = "text/html; charset=$this->encoding";
                    $body = $data !== null ? "$data" : "$this->data";
                    break;

                case 'XML':
                    $headers['Content-Type'] = "text/xml; charset=$this->encoding";

                    if ($data instanceof DomDocument) {
                        $xml = new XmlProcessor();
                        $data = $xml->format($data);
                    }

                    $body = $data !== null ? "$data" : "$this->data";
                    break;

                case 'URLENCODE':
                    $headers['Cache-Control'] = 'no-cache';
                    $headers['Content-Type'] = 'application/x-www-form-urlencoded';

                    $data = flow((array)$this->data)->append((array)$data)->withKeys()->toArray();

                    $body = $this->formatUrlencode($data);
                    break;

                case 'MULTIPART':
                    $headers['Cache-Control'] = 'no-cache';
                    $headers['Content-Type'] = 'multipart/form-data; boundary=' . $this->_boundary;

                    $data = flow((array)$this->data)->append((array)$data)->withKeys()->toArray();
                    $body = $this->formatMultipart($data);
                    break;

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
        foreach (flow($this->cookies, $request->cookies())->toMap() as $name => $value) {
            $value = urlencode($value);
            $cookie[] = "$name=$value";
        }

        if ($cookie) {
            $connect->setRequestProperty('Cookie', str::join($cookie, '; '));
        }

        foreach (flow($headers, $this->headers, $request->headers())->toMap() as $name => $value) {
            $connect->setRequestProperty($name, $value);
        }

        echo ("Request {$method} -> {$url}"), "\n";
        return $this->connect($request, $url, $connect, $body);
    }

    /**
     * @param string $url
     * @param string $method
     * @param array|mixed $data
     * @return HttpResponse
     */
    public function execute(string $method, string $url, $data = null): HttpResponse
    {
        return $this->send(new HttpRequest($method, $url, $data));
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
                    $str[] = "{$prefix}[$code]=" . urlencode($value);
                } else {
                    $str[] = "$code=" . urlencode($value);
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

                $name = urlencode($name);
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

            $name = urlencode($name);
            $out->write("Content-Disposition: form-data; name=\"$name\"; filename=\"");
            $out->write(urlencode(fs::name($stream->getPath())) . "\"");
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

    /**
     * @return string
     */
    protected function getBaseUrl(): string
    {
        return $this->baseUrl;
    }

    /**
     * @param string $baseUrl
     */
    protected function setBaseUrl(string $baseUrl)
    {
        $this->baseUrl = $baseUrl;
    }

    /**
     * @return string
     */
    protected function getUserAgent(): string
    {
        return $this->userAgent;
    }

    /**
     * @param string $userAgent
     */
    protected function setUserAgent(string $userAgent)
    {
        $this->userAgent = $userAgent;
    }

    /**
     * @return string
     */
    protected function getReferrer(): string
    {
        return $this->referrer;
    }

    /**
     * @param string $referrer
     */
    protected function setReferrer(string $referrer)
    {
        $this->referrer = $referrer;
    }

    /**
     * @return bool
     */
    protected function isFollowRedirects(): bool
    {
        return $this->followRedirects;
    }

    /**
     * @param bool $followRedirects
     */
    protected function setFollowRedirects(bool $followRedirects)
    {
        $this->followRedirects = $followRedirects;
    }

    /**
     * @return string
     */
    protected function getConnectTimeout(): string
    {
        return $this->connectTimeout;
    }

    /**
     * @param string $connectTimeout
     */
    protected function setConnectTimeout(string $connectTimeout)
    {
        $this->connectTimeout = $connectTimeout;
    }

    /**
     * @return string
     */
    protected function getReadTimeout(): string
    {
        return $this->readTimeout;
    }

    /**
     * @param string $readTimeout
     */
    protected function setReadTimeout(string $readTimeout)
    {
        $this->readTimeout = $readTimeout;
    }

    /**
     * @return string
     */
    protected function getProxyType(): string
    {
        return $this->proxyType;
    }

    /**
     * @param string $proxyType
     */
    protected function setProxyType(string $proxyType)
    {
        $this->proxyType = $proxyType;
    }

    /**
     * @return string
     */
    protected function getProxy(): string
    {
        return $this->proxy;
    }

    /**
     * @param string $proxy
     */
    protected function setProxy(string $proxy)
    {
        $this->proxy = $proxy;
    }

    /**
     * @return string
     */
    protected function getRequestType(): string
    {
        return $this->requestType;
    }

    /**
     * @param string $requestType
     */
    protected function setRequestType(string $requestType)
    {
        $this->requestType = $requestType;
    }

    /**
     * @return string
     */
    protected function getResponseType(): string
    {
        return $this->responseType;
    }

    /**
     * @param string $responseType
     */
    protected function setResponseType(string $responseType)
    {
        $this->responseType = $responseType;
    }

    /**
     * @return array
     */
    protected function getData(): array
    {
        return $this->data;
    }

    /**
     * @param array $data
     */
    protected function setData(array $data)
    {
        $this->data = $data;
    }

    /**
     * @return string
     */
    protected function getEncoding(): string
    {
        return $this->encoding;
    }

    /**
     * @param string $encoding
     */
    protected function setEncoding(string $encoding)
    {
        $this->encoding = $encoding;
    }

    /**
     * @return callable|null
     */
    protected function getBodyParser()
    {
        return $this->bodyParser;
    }

    /**
     * @param callable|null $bodyParser
     */
    protected function setBodyParser($bodyParser)
    {
        $this->bodyParser = $bodyParser;
    }

    /**
     * @param string $name
     * @return bool|mixed
     * @throws \Error
     */
    public function __get(string $name)
    {
        $method = "get$name";

        if (method_exists($this, $method)) {
            return $this->{$method}();
        }

        $method = "is$name";

        if (method_exists($this, $method)) {
            return (bool) $this->{$method}();
        }

        throw new \Error("Property '$name' is not exists in class " . reflect::typeOf($this));
    }
    /**
     * @param string $name
     * @param $value
     * @return bool|mixed
     * @throws \Error
     */
    public function __set(string $name, $value)
    {
        $method = "set$name";

        if (method_exists($this, $method)) {
            $closure = Closure::fromCallable([$this, $method]);
            return $closure($value);
        }

        throw new \Error("Property '$name' is not exists in class " . reflect::typeOf($this) . " or readonly");
    }
}
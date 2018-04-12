<?php
namespace httpclient;

use php\io\Stream;
use php\lib\arr;
use php\lib\str;
use php\time\Time;

/**
 * Class HttpResponse
 */
class HttpResponse
{
    protected $body;

    protected $responseCode = 200;

    protected $statusMessage = null;

    protected $headers = [];

    protected $cookies = [];

    /**
     * @var Time
     */
    protected $time;

    /**
     * HttpResponse constructor.
     */
    public function __construct()
    {
        $this->time = Time::now();
    }


    /**
     * @param mixed $data
     * @return mixed|string|array|Stream
     */
    public function body($data = null)
    {
        if ($data) {
            $this->body = $data;
        } else {
            return $this->body;
        }
    }

    /**
     * @param int $responseCode
     * @return int
     */
    public function statusCode(?int $responseCode = null): ?int
    {
        if ($responseCode) {
            $this->responseCode = $responseCode;
        } else {
            return $this->responseCode;
        }
    }

    /**
     * @param string $statusMessage
     * @return string
     */
    public function statusMessage(?string $statusMessage = null): ?string
    {
        if ($statusMessage) {
            $this->statusMessage = $statusMessage;
        } else {
            return $this->statusMessage;
        }
    }

    /**
     * @param array $headerFields
     * @return array
     */
    public function headers(?array $headerFields = null): ?array
    {
        if ($headerFields) {
            foreach ($headerFields as $name => $value) {
                if (is_array($value) && sizeof($value) == 1) $value = arr::first($value);

                $this->headers[str::lower($name)] = $value;
            }
        } else {
            return $this->headers;
        }
    }

    /**
     * Returns header value.
     * @param string $name
     * @return mixed
     */
    public function header(string $name)
    {
        return $this->headers[str::lower($name)];
    }

    /**
     * Returns Content-Type header value.
     * @param string $contentType
     * @return string
     */
    public function contentType(?string $contentType = null): ?string
    {
        if ($contentType === null) {
            return $this->header('Content-Type');
        } else {
            $this->headers['content-type'] = $contentType;
        }
    }

    /**
     * Content-Length header value, returns -1 if it does not exist.
     * @param int $size
     * @return int
     */
    public function contentLength(?int $size = null): ?int
    {
        if ($size === null) {
            return (int) ($this->header('Content-Length') ?: -1);
        } else {
            $this->headers['content-length'] = $size;
        }
    }

    /**
     * @param string $name
     * @return string|array
     */
    public function cookie(string $name)
    {
        return $this->cookies()[$name];
    }

    /**
     * Return array of Set-Cookie header.
     * @param array $data
     * @return array
     */
    public function cookies(array $data = null): ?array
    {
        if ($data === null) {
            if ($this->cookies) {
                return $this->cookies;
            }

            $cookies = $this->header('Set-Cookie');
            $result = [];

            if (!is_array($cookies)) $cookies = [$cookies];

            foreach ($cookies as $cookie) {
                list($name, $value) = str::split($cookie, '=', 2);

                $values = str::split($value, ';', 10);

                $result[$name] = urldecode($values[0]);
            }

            return $this->cookies = $result;
        } else {
            $str = [];

            foreach ($data as $name => $value) {
                if (!is_array($value)) $value = [$value];

                foreach ($value as $one) {
                    $one = urlencode($one);
                    $str[] = "$name=$one";
                }
            }

            $this->cookies = $data;
            $this->headers['cookie'] = str::join($str, '&');
        }
    }

    /**
     * Check http code >= 200 and <= 399
     * @return bool
     */
    public function isSuccess(): bool
    {
        $statusCode = $this->statusCode();
        return $statusCode >= 200 && $statusCode <= 399;
    }

    /**
     * Check http code >= 400
     * @return bool
     */
    public function isFail(): bool
    {
        $statusCode = $this->statusCode();
        return $statusCode >= 400;
    }

    /**
     * Check http code >= 400
     * @return bool
     */
    public function isError(): bool
    {
        return $this->isFail();
    }

    /**
     * Check http code is 400.
     * @return bool
     */
    public function isBadRequest(): bool
    {
        return $this->statusCode() === 400;
    }

    /**
     * Check http code is 401.
     * @return bool
     */
    public function isAuthRequired(): bool
    {
        return $this->statusCode() === 401;
    }

    /**
     * Check http code is 402.
     * @return bool
     */
    public function isPaymentRequired(): bool
    {
        return $this->statusCode() === 402;
    }

    /**
     * Check http code is 404
     * @return bool
     */
    public function isNotFound(): bool
    {
        return $this->statusCode() === 404;
    }

    /**
     * Check http code is 403
     * @return bool
     */
    public function isAccessDenied(): bool
    {
        return $this->statusCode() === 403;
    }

    /**
     * Check http code is 405
     * @return bool
     */
    public function isInvalidMethod(): bool
    {
        return $this->statusCode() === 405;
    }

    /**
     * Check http code >= 500
     * @return bool
     */
    public function isServerError(): bool
    {
        return $this->statusCode() >= 500;
    }


    /**
     * Check http code is 503.
     * @return bool
     */
    public function isServiceUnavailable(): bool
    {
        return $this->statusCode() === 503;
    }

    /**
     * Time of creation response.
     * @return Time
     */
    public function time(): Time
    {
        return $this->time;
    }
}
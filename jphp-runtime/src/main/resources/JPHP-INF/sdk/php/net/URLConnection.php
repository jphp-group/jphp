<?php
namespace php\net;

use php\io\Stream;

/**
 * Class URLConnection
 * @packages std, core
 */
class URLConnection
{
    /**
     * @var bool
     */
    public $doOutput = false;

    /**
     * @var bool
     */
    public $doInput = false;

    /**
     * POST, GET, PUT, etc.
     * @var string
     */
    public $requestMethod;

    /**
     * that specifies the connect timeout value in milliseconds
     * @var int millis
     */
    public $connectTimeout;

    /**
     * the read timeout to a specified timeout, in milliseconds.
     * @var int millis
     */
    public $readTimeout;

    /**
     * @var bool
     */
    public $useCaches;

    /**
     * @var int millis
     */
    public $ifModifiedSince;

    /**
     * @var bool
     */
    public $followRedirects;

    /**
     * @readonly
     * @var URL
     */
    public $url;

    /**
     * @readonly
     * @var int
     */
    public $responseCode;

    /**
     * @readonly
     * @var int
     */
    public $responseMessage;

    /**
     * int the content length of the resource that this connection's URL
     * references, -1 if the content length is not known,
     * or if the content length is greater than Integer.MAX_VALUE.
     *
     * @readonly
     * @var int bytes
     */
    public $contentLength;

    /**
     * @readonly
     * @var string
     */
    public $contentType;

    /**
     * @readonly
     * @var string
     */
    public $contentEncoding;

    /**
     * @readonly
     * @var int
     */
    public $expiration;

    /**
     * @readonly
     * @var int
     */
    public $lastModified;

    /**
     * @readonly
     * @var bool
     */
    public $usingProxy;

    /**
     * @param URLConnection $parent
     */
    protected function __construct(URLConnection $parent)
    {
    }

    /**
     * Opens a communications link to the resource referenced by this
     * URL, if such a connection has not already been established.
     */
    public function connect()
    {
    }


    /**
     * @param string $name
     */
    public function getHeaderField($name)
    {
    }

    /**
     * @return array
     */
    public function getHeaderFields()
    {
    }

    /**
     * @return Stream
     */
    public function getInputStream()
    {
    }

    /**
     * @return Stream
     */
    public function getErrorStream()
    {
    }

    /**
     * @return Stream
     */
    public function getOutputStream()
    {
    }

    /**
     * @param string $name
     * @param string $value
     */
    public function setRequestProperty($name, $value)
    {
    }

    /**
     * @param string $name
     */
    public function getRequestProperty($name)
    {
    }

    /**
     * @return array
     */
    public function getRequestProperties()
    {
    }

    /*** Only FOR HTTP connections ***/

    /**
     * Indicates that other requests to the server
     * are unlikely in the near future. Calling disconnect()
     * should not imply that this HttpURLConnection
     * instance can be reused for other requests.
     */
    public function disconnect()
    {
    }

    /**
     * This method is used to enable streaming of a HTTP request body
     * without internal buffering, when the content length is <b>not</b>
     * known in advance. In this mode, chunked transfer encoding
     * is used to send the request body. Note, not all HTTP servers
     * support this mode.
     *
     * @param int $chunklen The number of bytes to write in each chunk.
     *          If chunklen is less than or equal to zero, a default
     *          value will be used.
     */
    public function setChunkedStreamingMode($chunklen)
    {
    }

    /**
     * Tries to determine the type of an input stream based on the
     * characters at the beginning of the input stream. This method can
     * be used by subclasses that override the
     * <code>getContentType</code> method.
     *
     * @param Stream $stream
     * @return string
     */
    public static function guessContentTypeFromStream(Stream $stream)
    {
    }

    /**
     * @param string $name
     */
    public static function guessContentTypeFromName($name)
    {
    }

    /**
     * @param string $url
     * @param Proxy $proxy
     * @return URLConnection
     */
    public static function create($url, Proxy $proxy = null)
    {
    }

    /**
     * Enable checking ssl for https
     */
    public static function enableSSLVerificationForHttps()
    {
    }

    /**
     * Disable checking ssl for https
     */
    public static function disableSSLVerificationForHttps()
    {
    }
}
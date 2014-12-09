<?php
namespace php\net;

use php\io\Stream;

/**
 * Class URLConnection
 * @package php\net
 */
class URLConnection {

    /**
     * Opens a communications link to the resource referenced by this
     * URL, if such a connection has not already been established.
     */
    public function connect() {}

    /**
     * @param int $timeout that specifies the connect timeout value in milliseconds
     */
    public function setConnectTimeout($timeout) {}

    /**
     * @return int
     */
    public function getConnectTimeout() {}

    /**
     * Sets the read timeout to a specified timeout, in
     * milliseconds.
     *
     * @param int $timeout
     */
    public function setReadTimeout($timeout) {}

    /**
     * @return int
     */
    public function getReadTimeout() {}

    /**
     * @return URL
     */
    public function getURL() {}

    /**
     * Returns the value of the ``content-length`` header field.
     *
     * @return int the content length of the resource that this connection's URL
     *          references, {@code -1} if the content length is not known,
     *          or if the content length is greater than Integer.MAX_VALUE.
     */
    public function getContentLength() {}

    /**
     * @return string
     */
    public function getContentType() {}

    /**
     * @return string
     */
    public function getContentEncoding() {}

    /**
     * @return int
     */
    public function getExpiration() {}

    /**
     * @return int
     */
    public function getDate() {}

    /**
     * @return int
     */
    public function getLastModified() {}

    /**
     * @param string $name
     */
    public function getHeaderField($name) {}

    /**
     * @return array
     */
    public function getHeaderFields() {}

    /**
     * @return Stream
     */
    public function getInputStream() {}

    /**
     * @return Stream
     */
    public function getOutputStream() {}

    /**
     * @return string
     */
    public function toString() {}

    /**
     * @param bool $doinput
     */
    public function setDoInput($doinput) {}

    /**
     * @return bool
     */
    public function getDoInput() {}

    /**
     * @param bool $doinput
     */
    public function setDoOutput($dooutput) {}

    /**
     * @return bool
     */
    public function getDoOutput() {}

    /**
     * @param bool $useCaches
     */
    public function setUseCaches($useCaches) {}

    /**
     * @return bool
     */
    public function getUseCaches() {}

    /**
     * @param int $ifmodifiedsince
     */
    public function setIfModifiedSince($ifmodifiedsince) {}

    /**
     * @return int
     */
    public function getIfModifiedSince() {}

    /**
     * @param string $name
     * @param string $value
     */
    public function setRequestProperty($name, $value) {}

    /**
     * @param string $name
     */
    public function getRequestProperty($name) {}

    /**
     * @return array
     */
    public function getRequestProperties() {}

    /*** Only FOR HTTP connections ***/

    /**
     * Indicates that other requests to the server
     * are unlikely in the near future. Calling disconnect()
     * should not imply that this HttpURLConnection
     * instance can be reused for other requests.
     */
    public function disconnect() {}

    /**
     * @return int
     */
    public function getResponseCode() {}

    /**
     * @return string
     */
    public function getResponseMessage() {}

    /**
     * @return bool
     */
    public function getInstanceFollowRedirects() {}

    /**
     * @param bool $value
     */
    public function setInstanceFollowRedirects($value) {}

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
    public function setChunkedStreamingMode($chunklen) {}

    /**
     * Set the method for the URL request, one of:
     * <UL>
     *  <LI>GET
     *  <LI>POST
     *  <LI>HEAD
     *  <LI>OPTIONS
     *  <LI>PUT
     *  <LI>DELETE
     *  <LI>TRACE
     * </UL> are legal, subject to protocol restrictions.  The default
     * method is GET.
     *
     * @param string $method
     */
    public function setRequestMethod($method) {}

    /**
     * @return string
     */
    public function getRequestMethod() {}

    /**
     * @return bool
     */
    public function usingProxy() {}

    /**
     * Tries to determine the type of an input stream based on the
     * characters at the beginning of the input stream. This method can
     * be used by subclasses that override the
     * <code>getContentType</code> method.
     *
     * @param Stream $stream
     * @return string
     */
    public static function guessContentTypeFromStream(Stream $stream) {}

    /**
     * @param string $name
     */
    public static function guessContentTypeFromName($name) {}
}
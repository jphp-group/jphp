<?php
namespace php\net;

use php\io\Stream;

/**
 * Class URL
 * @packages std, net
 */
class URL
{

    /**
     * @param string $uri
     */
    public function __construct($uri) { }

    /**
     * @param Proxy $proxy (optional)
     * @return URLConnection
     */
    public function openConnection(Proxy $proxy) { }

    /**
     * Gets the authority part of this ``URL``
     * @return string
     */
    public function getAuthority() { }

    /**
     * @return int
     */
    public function getPort() { }

    /**
     * Gets the default port number of the protocol associated
     * with this ``URL``. If the URL scheme or the URLStreamHandler
     * for the URL do not define a default port number,
     * then -1 is returned.
     *
     * @return int
     */
    public function getDefaultPort() { }

    /**
     * Gets the protocol name of this ``URL``
     *
     * @return string
     */
    public function getProtocol() { }

    /**
     * @return string
     */
    public function getHost() { }

    /**
     * Gets the file name of this <code>URL</code>.
     * The returned file portion will be
     * the same as ``getPath()``, plus the concatenation of
     * the value of ``getQuery()``, if any. If there is
     * no query portion, this method and ``getPath()`` will
     * return identical results.
     * @return string
     */
    public function getFile() { }

    /**
     * @return string
     */
    public function getPath() { }

    /**
     * @return string
     */
    public function getQuery() { }

    /**
     * Gets the anchor (also known as the "reference") of this URL
     * @return string
     */
    public function getRef() { }

    /**
     * Compares two URLs, excluding the fragment component.
     *
     * @param URL $url
     * @return bool
     */
    public function sameFile(URL $url) { }

    /**
     * @return string
     */
    public function toString() { }

    /**
     * Constructs a string representation of this URL. The
     * string is created by calling the toExternalForm
     * method of the stream protocol handler for this object.
     *
     * @return string
     */
    public function toExternalForm() { }

    /**
     * @return Stream
     */
    public function openStream() { }

    /**
     * @return string
     */
    public function __toString() { return $this->getRef(); }


    /**
     * @param string $text
     * @param string $encoding
     * @return string
     */
    public static function encode($text, $encoding = null)
    {
    }

    /**
     * @param string $text
     * @param string $encoding
     * @return string
     */
    public static function decode($text, $encoding = null)
    {
    }
}
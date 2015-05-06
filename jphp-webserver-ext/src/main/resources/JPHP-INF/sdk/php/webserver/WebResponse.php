<?php
namespace php\webserver;

/**
 * Class WebResponse
 * @package php\webserver
 */
class WebResponse
{
    /** @var int */
    public $status;

    /** @var string */
    public $contentType;

    /** @var string */
    public $characterEncoding;

    /** @var int */
    public $bufferSize;

    /**
     * @param WebResponse $parent
     */
    protected function __construct(WebResponse $parent) {}

    /**
     * @param string $name
     * @param string $value
     */
    public function setHeader($name, $value)
    {
    }

    /**
     * @param string $name
     * @return string
     */
    public function getHeader($name)
    {
    }

    /**
     * @param string $name
     * @return string[]
     */
    public function getHeaders($name)
    {
    }

    /**
     * @return string[]
     */
    public function getHeaderNames()
    {
    }

    /**
     * @param string $name
     * @param string $value
     */
    public function addHeader($name, $value)
    {
    }

    /**
     * @param string $location
     * @param int $httpStatus
     */
    public function redirect($location, $httpStatus = 301)
    {
    }

    /**
     * @param string $url
     */
    public function encodeRedirectURL($url)
    {
    }

    /**
     * @param string $content
     */
    public function writeToBody($content)
    {
    }

    /**
     * @param int $length
     */
    public function setContentLength($length)
    {
    }

    /**
     * @param array $cookie [name, value, maxAge, path, domain, httpOnly, secure, comment]
     */
    public function addCookie(array $cookie)
    {
    }

    /**
     * @return int
     */
    protected function getStatus()
    {
        return $this->status;
    }

    /**
     * @param int $status
     */
    protected function setStatus($status)
    {
        $this->status = $status;
    }

    /**
     * @return string
     */
    protected function getContentType()
    {
        return $this->contentType;
    }

    /**
     * @param string $contentType
     */
    protected function setContentType($contentType)
    {
        $this->contentType = $contentType;
    }

    /**
     * @return string
     */
    protected function getCharacterEncoding()
    {
        return $this->characterEncoding;
    }

    /**
     * @param string $characterEncoding
     */
    protected function setCharacterEncoding($characterEncoding)
    {
        $this->characterEncoding = $characterEncoding;
    }

    /**
     * @return int
     */
    protected function getBufferSize()
    {
        return $this->bufferSize;
    }

    /**
     * @param int $bufferSize
     */
    protected function setBufferSize($bufferSize)
    {
        $this->bufferSize = $bufferSize;
    }

    /**
     * @return WebResponse
     */
    public static function current()
    {
    }
}
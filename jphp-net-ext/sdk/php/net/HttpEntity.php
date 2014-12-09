<?php
namespace php\net;
use php\io\Stream;

/**
 * Class HttpEntity
 * @package php\net
 */
class HttpEntity {

    /**
     * @param Stream|string $source
     */
    public function __construct($source) { }

    /**
     * @return Stream
     */
    public function getContent() { return Stream::of(''); }

    /**
     * @return string
     */
    public function getContentType() { return ''; }

    /**
     * @return string
     */
    public function getContentEncoding() { return ''; }

    /**
     * @return int
     */
    public function getContentLength() { return 0; }

    /**
     * @return bool
     */
    public function isChunked() { return false; }

    /**
     * @return bool
     */
    public function isRepeatable() { return false; }

    /**
     * @return bool
     */
    public function isStreaming() { return false; }

    /**
     * @param null|string $encoding
     * @return string
     */
    public function toString($encoding = null) { return ''; }
}

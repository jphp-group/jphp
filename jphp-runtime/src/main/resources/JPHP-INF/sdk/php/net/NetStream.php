<?php
namespace php\net;

use php\io\IOException;
use php\io\Stream;

/**
 * http, ftp protocols
 *
 * Class NetStream
 * @packages std, net
 */
class NetStream extends Stream
{
    /**
     * {@inheritdoc}
     */
    public function read($length)
    {
    }

    /**
     * @param int $bufferSize
     * @return mixed|void
     */
    public function readFully($bufferSize = 4096)
    {
    }

    /**
     * @param int $bufferSize
     * @param callable $callback (NetStream $this, $len)
     */
    public function readFullyWithCallback($bufferSize, callable $callback) {}

    /**
     * {@inheritdoc}
     */
    public function write($value, $length = null)
    {
    }

    /**
     * {@inheritdoc}
     */
    public function eof()
    {
    }


    /**
     * {@inheritdoc}
     */
    public function seek($position)
    {
        throw new IOException("Unavailable seek() operation");
    }


    /**
     * {@inheritdoc}
     */
    public function getPosition()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function close()
    {
    }

    /**
     * @return URL
     */
    public function getUrl() {}

    /**
     * @param Proxy $proxy
     */
    public function setProxy(Proxy $proxy) {}

    /**
     * @return Proxy
     */
    public function getProxy() {}

    /**
     * @return URLConnection
     */
    public function getUrlConnection() {}
}
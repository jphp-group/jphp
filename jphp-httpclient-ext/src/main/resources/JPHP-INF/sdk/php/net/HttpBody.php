<?php
namespace php\net;

/**
 * Class HttpBody
 * @package php\net
 */
abstract class HttpBody
{
    /**
     * @param URLConnection $connection
     * @return void
     */
    abstract public function apply(URLConnection $connection);
}
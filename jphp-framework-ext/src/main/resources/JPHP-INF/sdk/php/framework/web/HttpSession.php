<?php
namespace php\framework\web;

/**
 * Class HttpSession
 * @package php\framework\web
 */
abstract class HttpSession implements \ArrayAccess
{
    /** @var string */
    public $id;

    /** @var int (UTC) */
    public $creationTime;

    /** @var int (UTC) */
    public $lastAccessedTime;

    /** @var int (seconds) */
    public $maxInactiveInterval;

    /**
     * Invalidate session.
     */
    public function invalidate() {}

    /**
     * @return bool
     */
    public function isNew() {}

    /**
     * {@inheritdoc}
     */
    public function offsetExists($offset)
    {
    }

    /**
     * {@inheritdoc}
     */
    public function offsetGet($offset)
    {
    }

    /**
     * {@inheritdoc}
     */
    public function offsetSet($offset, $value)
    {
    }

    /**
     * {@inheritdoc}
     */
    public function offsetUnset($offset)
    {
    }
}
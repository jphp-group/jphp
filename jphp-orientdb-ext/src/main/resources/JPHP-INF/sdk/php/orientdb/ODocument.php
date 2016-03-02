<?php
namespace php\orientdb;

/**
 * Class ODocument
 * @package php\orientdb
 */
class ODocument
{
    /**
     * @readonly
     * @var string
     */
    public $id;

    /**
     * @param string $className
     */
    public function __construct($className)
    {
    }

    /**
     * @return bool
     */
    public function isNew()
    {
    }

    /**
     * Reset id.
     */
    public function resetId()
    {
    }

    /**
     * Save document.
     */
    public function save()
    {
    }

    /**
     * Delete document.
     */
    public function delete()
    {
    }

    /**
     * Clear fields.
     */
    public function clear()
    {
    }

    /**
     * Reset to default values.
     */
    public function reset()
    {
    }

    /**
     * Undo changes.
     * @param $field (optional)
     */
    public function undo($field)
    {
    }

    /**
     * Load data to document from db.
     */
    public function load()
    {
    }

    /**
     *
     */
    public function reload()
    {
    }

    /**
     * @return string
     */
    public function toJson()
    {
    }

    /**
     * @param string $json
     */
    public function fromJson($json)
    {
    }

    /**
     * @param $name
     * @param $value
     */
    public function __set($name, $value)
    {
    }

    /**
     * @param $name
     */
    public function __get($name)
    {
    }

    /**
     * @param $name
     */
    public function __isset($name)
    {
    }

    /**
     * @param $name
     */
    public function __unset($name)
    {
    }

    /**
     * Cloneable object.
     */
    public function __clone()
    {
    }
}
<?php
namespace php\orientdb;
use php\util\Flow;

/**
 * Class ODatabase
 * @package php\orientdb
 */
class ODatabase
{
    /**
     * @readonly
     * @var string
     */
    public $name;

    /**
     * @readonly
     * @var string
     */
    public $url;

    /**
     * @readonly
     * @var int
     */
    public $size;

    /**
     * OPEN, CLOSED, IMPORTING
     * @readonly
     * @var string
     */
    public $status;

    /**
     * @var string
     */
    public $type;

    /**
     * @param string $uri
     */
    public function __construct($uri)
    {
    }

    /**
     * @param string $name
     */
    public function setUser($name)
    {
    }

    /**
     * @param string $name
     * @param string $password
     */
    public function setUserAndPassword($name, $password)
    {
    }

    /**
     * @return bool
     */
    public function exists()
    {
    }

    /**
     *
     */
    public function create()
    {
    }

    /**
     * Delete database.
     */
    public function drop()
    {
    }

    /**
     * Open database
     * @param string $username
     * @param string $password
     */
    public function open($username, $password)
    {
    }

    /**
     * Close database.
     */
    public function close()
    {
    }

    /**
     * ...
     */
    public function reload()
    {
    }

    /**
     * Flush all indexes and cached storage content to the disk.
     *
     * After this call users can perform only select queries. All write-related commands will queued till {@link #release()} command
     * will be called.
     *
     * Given command waits till all on going modifications in indexes or DB will be finished.
     *
     * IMPORTANT: This command is not reentrant.
     */
    public function freeze()
    {
    }

    /**
     * Allows to execute write-related commands on DB. Called after {@link #freeze()} command.
     */
    public function release()
    {
    }

    /**
     * Begin transaction.
     */
    public function begin()
    {
    }

    /**
     * @param bool $force
     */
    public function commit($force = false)
    {
    }

    /**
     * @param bool $force
     */
    public function rollback($force = false)
    {
    }

    /**
     * @param $className
     * @return Flow of ODocument
     */
    public function getDocuments($className)
    {
    }

    /**
     * @param string $query like sql.
     * @param int $limit
     * @return Flow of ODocument
     */
    public function query($query, $limit = -1)
    {
    }

    /**
     * @param string $query
     * @return int
     */
    public function command($query)
    {
    }
}
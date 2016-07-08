<?php
namespace php\sql;

use Iterator;
use php\io\File;
use php\io\Stream;
use php\time\Time;

/**
 * Class SqlStatement
 * @package php\sql
 * @iterator-type SqlResult
 */
abstract class SqlStatement implements Iterator
{
    /**
     * @param int $index
     * @param mixed $value
     */
    public function bind($index, $value)
    {
    }

    /**
     * @param int $index
     * @param Time $time
     */
    public function bindDate($index, Time $time)
    {
    }

    /**
     * @param int $index
     * @param Time $time
     */
    public function bindTime($index, Time $time)
    {
    }

    /**
     * @param int $index
     * @param Time|int $time
     */
    public function bindTimestamp($index, $time)
    {
    }

    /**
     * @param int $index
     * @param string|File|Stream $blob
     */
    public function bindBlob($index, $blob)
    {
    }

    /**
     * Returns null if rows does not exist.
     *
     * @return SqlResult|null
     * @throws SqlException
     */
    public function fetch()
    {
    }

    /**
     * @return int
     * @throws SqlException
     */
    public function update()
    {
    }

    /**
     * @return mixed
     */
    public function getLastInsertId()
    {
    }

    /**
     * @return SqlResult
     */
    public function getGeneratedKeys()
    {
    }

    /**
     * @return SqlResult
     */
    public function current()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function next()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function key()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function valid()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function rewind()
    {
    }
}
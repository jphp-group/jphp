<?php
namespace php\sql;

use php\io\Stream;
use php\time\Time;

/**
 * Class SqlResult
 * @package php\sql
 */
abstract class SqlResult
{
    /**
     * @return bool
     * @throws SqlException
     */
    public function isLast()
    {
    }

    /**
     * @return bool
     * @throws SqlException
     */
    public function isFirst()
    {
    }

    /**
     * Deletes current row.
     * @throws SqlException
     */
    public function delete()
    {
    }

    /**
     * @return bool
     * @throws SqlException
     */
    public function isDeleted()
    {
    }

    /**
     * @throws SqlException
     */
    public function refresh()
    {
    }

    /**
     * @param string $column
     * @return mixed|Stream|Time
     * @throws SqlException
     */
    public function get($column)
    {
    }

    /**
     * @param bool $assoc
     * @return array
     * @throws SqlException
     */
    public function toArray($assoc = true)
    {
    }
}
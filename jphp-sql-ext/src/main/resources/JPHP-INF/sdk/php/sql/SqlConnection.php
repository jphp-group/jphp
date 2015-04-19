<?php
namespace php\sql;

/**
 * Class SqlConnection
 * @package php\sql
 */
abstract class SqlConnection
{
    const TRANSACTION_READ_UNCOMMITTED = 1;
    const TRANSACTION_READ_COMMITTED = 2;
    const TRANSACTION_REPEATABLE_READ = 4;
    const TRANSACTION_NONE = 0;
    const TRANSACTION_SERIALIZABLE = 8;

    /**
     * Disable this to use transaction mode.
     * @var bool
     */
    public $autoCommit = true;

    /**
     * @var bool
     */
    public $readOnly = false;

    /**
     * See SqlConnection::TRANSACTION_* constants
     * @var int
     */
    public $transactionIsolation;

    /**
     * @var string
     */
    public $catalog;

    /**
     * @var string
     */
    public $schema;

    /**
     * @param string $name
     * @return string
     * @throws SqlException
     */
    public function identifier($name)
    {
    }

    /**
     * @param string $sql
     * @param array $arguments
     * @return SqlStatement
     */
    public function query($sql, array $arguments = null)
    {
    }

    /**
     * Makes all changes made since the previous
     * commit/rollback permanent and releases any database locks
     * currently held by this Connection object.
     *
     * @throws SqlException
     */
    public function commit()
    {
    }

    /**
     * Undoes all changes made in the current transaction
     * and releases any database locks currently held
     * by this Connection object.
     *
     * @throws SqlException
     */
    public function rollback()
    {
    }

    /**
     * @throws SqlException
     */
    public function close()
    {
    }

    /**
     * @return array
     */
    public function getCatalogs()
    {
    }

    /**
     * @return array
     */
    public function getSchemas()
    {
    }

    /**
     * @return array
     */
    public function getMetaData()
    {
    }
}
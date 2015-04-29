<?php
namespace php\sql;

/**
 * Class SqlConnectionPool
 * @package php\sql
 */
class SqlConnectionPool
{
    /**
     * @param SqlConnectionPool $parent
     */
    protected function __construct(SqlConnectionPool $parent)
    {
    }

    /**
     * @return SqlConnection
     */
    public function getConnection()
    {
    }

    /**
     * @param string $username
     * @return $this
     */
    public function setUser($username)
    {
    }

    /**
     * @param string $password
     * @return $this
     */
    public function setPassword($password)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setInitialPoolSize($value)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setMinPoolSize($value)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setMaxPoolSize($value)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setMaxStatements($value)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setAcquireIncrement($value)
    {
    }

    /**
     * @param int $seconds
     * @return $this
     */
    public function setMaxIdleTime($seconds)
    {
    }

    /**
     * @param int $seconds
     * @return $this
     */
    public function setMaxConnectionAge($seconds)
    {
    }

    /**
     * @param int $value
     * @return $this
     */
    public function setMaxStatementsPerConnection($value)
    {
    }
}
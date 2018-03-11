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
    public function setMaxPoolSize($value)
    {
    }

    /**
     * @param int $millis
     * @return $this
     */
    public function setIdleTimeout($millis)
    {
    }

    /**
     * @param int $millis
     * @return $this
     */
    public function setMaxLifetime($millis)
    {
    }

    /**
     * @param int $millis
     * @return $this
     */
    public function setMinimumIdle($millis)
    {
    }
}
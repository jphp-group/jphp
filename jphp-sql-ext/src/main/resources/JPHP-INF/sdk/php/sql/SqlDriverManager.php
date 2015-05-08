<?php
namespace php\sql;

/**
 * Class DriverManager
 * @package php\sql
 */
class SqlDriverManager
{
    /**
     * @param string $driverName - mysql, pgsql, postgres, mssql, firebird, sybase, sqlite, etc.
     * @throws SqlException if cannot install or find driver.
     */
    public static function install($driverName)
    {
    }

    /**
     * @param string $url
     * @param array $options (optional) username, password, etc.
     * @throws SqlException if a database access error occurs
     * @return SqlConnection
     */
    public static function getConnection($url, array $options)
    {
    }

    /**
     * @param string $url
     * @param string $driverName - mysql, pgsql, postgres, mssql, firebird, sybase, sqlite, etc.
     * @param array $options (optional)
     * @return SqlConnectionPool
     */
    public static function getPool($url, $driverName, array $options)
    {
    }
}
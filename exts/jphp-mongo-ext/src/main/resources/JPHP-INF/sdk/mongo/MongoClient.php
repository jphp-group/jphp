<?php
namespace mongo;

/**
 * Class MongoClient
 * @package mongo
 */
class MongoClient
{
    /**
     * MongoClient constructor.
     * @param string $host
     * @param int $port
     */
    public function __construct(string $host = '127.0.0.1', int $port = 27017)
    {
    }

    /**
     * @param string $uri
     * @return MongoClient
     */
    static public function createFromURI(string $uri): MongoClient
    {
    }

    /**
     * @param string $name
     * @return MongoDatabase
     */
    public function database(string $name): MongoDatabase
    {
    }

    /**
     * Get All Databases of Mongo Server.
     * @return MongoIterable
     */
    public function databases(): MongoIterable
    {
    }

    public function close()
    {
    }
}
<?php
namespace packager\repository;

use httpclient\HttpClient;
use php\lib\fs;
use php\lib\str;

/**
 * Class ServerRepository
 * @package packager\repository
 */
class ServerRepository extends ExternalRepository
{
    /**
     * @var HttpClient
     */
    protected $client;

    public function __construct($source)
    {
        parent::__construct($source);

        $this->client = new HttpClient($source);
        $this->client->responseType = 'JSON';
        $this->client->requestType = 'JSON';
    }

    /**
     * @return bool
     */
    public function isFit(): bool
    {
        return str::startsWith($this->getSource(), 'http://')
            || str::startsWith($this->getSource(), 'https://');
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $copied = fs::copy(
            "{$this->getSource()}/repo/$pkgName?version=$pkgVersion&download=1", $toFile
        );

        return $copied > -1;
    }

    public function getVersions(string $pkgName): array
    {
        return fs::parseAs("{$this->getSource()}/repo/$pkgName/find", "json");
    }
}
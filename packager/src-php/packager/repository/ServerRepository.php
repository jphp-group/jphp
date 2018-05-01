<?php
namespace packager\repository;

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
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

    public function getVersions(string $pkgName): array
    {
        $res = $this->client->send(new HttpRequest('GET', '/repo/find', [], ['name' => $pkgName]));

        if ($res->isSuccess()) {
            return (array) $res->body()['versions'];
        } else {
            Console::warn("Failed to find package '{2}' in {0}, {1}", $this->getSource(), $res->statusMessage(), $pkgName);
            return [];
        }
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $res = $this->client->send(new HttpRequest('GET', '/repo/get', [], ['name' => $pkgName, 'version' => $pkgVersion]));

        if ($res->isSuccess()) {
            $url = $res->body()['downloadUrl'];

            if ($url) {
                $copied = fs::copy($url, $toFile, function () {
                    Console::print(".");
                }, 1024 * 128);
                Console::log(". done.");

                return $copied > 0;
            } else {
                Console::warn(
                    "Failed to download package '{0}@{1}' from {2}, donwloadUrl is empty.",
                    $pkgName, $pkgVersion, $this->getSource()
                );
            }
        } else {
            Console::warn("Failed to get package '{0}@{1}' from {2}, {3}", $pkgName, $pkgVersion, $this->getSource(), $res->statusMessage());
        }

        return false;
    }
}
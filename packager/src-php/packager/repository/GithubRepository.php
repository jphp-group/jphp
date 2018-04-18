<?php
namespace packager\repository;


use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use php\io\IOException;
use php\lang\IllegalArgumentException;
use php\lib\fs;
use php\lib\str;
use php\net\URL;

class GithubRepository extends ExternalRepository
{
    /**
     * @var URL
     */
    private $sourceUrl;

    public function __construct($source)
    {
        parent::__construct($source);

        try {
            $this->sourceUrl = new URL($source);
        } catch (IllegalArgumentException|IOException $e) {
            // ...
        }
    }

    public function isFit(): bool
    {
        return str::startsWith($this->getSource(), 'https://github.com/')
            || str::startsWith($this->getSource(), 'http://github.com/');
    }

    public function getVersions(string $pkgName): array
    {
        $client = new HttpClient();

        $request = new HttpRequest(
            'GET', "https://raw.githubusercontent.com{$this->sourceUrl->getPath()}/master/$pkgName/versions.json"
        );
        $request->responseType('JSON');

        $res = $client->send($request);

        if ($res->isSuccess()) {
            return $res->body();
        } else {
            Console::warn("Failed to get version of '{0}' from '{1}', {2}", $pkgName, $this->getSource(), $res->statusMessage());
            return [];
        }
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $copied = fs::copy(
            "{$this->getSource()}/raw/master/$pkgName/$pkgVersion.tar.gz", $toFile
        );

        return $copied > 0;
    }
}
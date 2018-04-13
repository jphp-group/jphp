<?php

namespace packager\repository;

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use php\lang\IllegalArgumentException;
use php\lib\fs;
use php\lib\str;
use php\net\URL;

class GithubReleasesRepository extends ExternalRepository
{
    /**
     * @var URL
     */
    private $sourceUrl;

    /**
     * @var HttpClient
     */
    private $client;

    /**
     * @var array
     */
    private $cache = [];

    public function __construct($source)
    {
        parent::__construct($source);

        try {
            $this->sourceUrl = new URL($source);
            $this->client = new HttpClient("https://api.github.com/repos{$this->sourceUrl->getPath()}");
            $this->client->handlers = [
                function (HttpRequest $request) {
                    //Console::log("-> {0} request '{1}'", $request->method(), $request->url());
                }
            ];

            $this->client->responseType = 'JSON';
            $this->client->requestType = 'JSON';
            $this->client->headers['Accept'] = 'application/vnd.github.v3+json';
        } catch (IllegalArgumentException $e) {
            // ...
        }
    }

    public function isFit(): bool
    {
        return (str::startsWith($this->getSource(), 'https://github.com/')
                || str::startsWith($this->getSource(), 'http://github.com/'))
            && str::endsWith($this->getSource(), '/releases');
    }

    /**
     * @param string $pkgName
     * @return array
     */
    public function getVersions(string $pkgName): array
    {
        $releases = $this->cache;

        if (!isset($releases)) {
            $response = $this->client->get('');

            if ($response->isSuccess()) {
                $releases = $this->cache = $response->body();
            } else {
                return [];
            }
        }

        $result = [];

        foreach ($releases as $release) {
            if ($release['draft'] || $release['prerelease']) {
                continue;
            }

            $versionInfo = null;
            foreach ($release['assets'] as $asset) {
                if ($asset['content_type'] === 'application/json') {
                    $versionInfo = fs::parseAs($asset['browser_download_url'], 'json');
                }
            }

            if ($versionInfo && $versionInfo['name'] === $pkgName) {
                $result[$versionInfo['version']] = flow($versionInfo)->onlyKeys(['size', 'sha256'])->toMap();
            }
        }


        return $result;
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $response = $this->client->get('');

        if ($response->isSuccess()) {
            foreach ($response->body() as $release) {
                if ($release['draft'] || $release['prerelease']) {
                    continue;
                }

                $versionInfo = null;
                foreach ($release['assets'] as $asset) {
                    if ($asset['content_type'] === 'application/json') {
                        $versionInfo = fs::parseAs($asset['browser_download_url'], 'json');

                        if ($versionInfo && $versionInfo['name'] === $pkgName && $versionInfo['version'] === $pkgVersion) {
                            foreach ($release['assets'] as $sub) {
                                if ($sub['content_type'] === 'application/gzip') {
                                    return fs::copy($sub['browser_download_url'], $toFile, null, 1024 * 128) > 0;
                                }
                            }

                            break;
                        }

                        break;
                    }
                }
            }
        }

        return false;
    }
}
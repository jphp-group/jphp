<?php

namespace packager\repository;

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use php\format\ProcessorException;
use php\io\IOException;
use php\jsoup\Jsoup;
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
                    Console::debug("--> [httpclient] {0} '{1}'", $request->method(), $request->url());
                }
            ];

            $this->client->responseType = 'JSON';
            $this->client->requestType = 'JSON';
            $this->client->headers['Accept'] = 'application/vnd.github.v3+json';
        } catch (IllegalArgumentException|IOException $e) {
            // ...
        }
    }

    public function isFit(): bool
    {
        return (str::startsWith($this->getSource(), 'https://github.com/')
                || str::startsWith($this->getSource(), 'http://github.com/'))
            && str::endsWith($this->getSource(), '/releases');
    }

    protected function fetchVersionInfo($release): ?array
    {
        $doc = Jsoup::parseText($release['body']);

        foreach ($doc->select('details') as $spoiler) {
            if (str::endsWith($spoiler->select('summary')->text(), '.json')) {
                $json = $spoiler->select('pre')->text();

                if ($json) {
                    return str::parseAs($json, 'json');
                }
            }
        }

        foreach ($release['assets'] as $asset) {
            if ($asset['content_type'] === 'application/json') {
                return fs::parseAs($asset['browser_download_url'], 'json');
            }
        }

        return null;
    }

    /**
     * @return array
     */
    protected function fetchReleases(): array
    {
        $releases = $this->cache;

        if (!$releases) {
            $releases = flow([]);

            $page = 1;
            do {
                $request = new HttpRequest('GET', '', ['rel' => 'last'], ['per_page' => 100, 'page' => $page]);

                $response = $this->client->send($request);

                if ($response->isSuccess()) {

                    if ($response->body()) {
                        $releases->append($response->body());

                        if (sizeof($response->body()) < 100) {
                            return $this->cache = $releases->toArray();
                        }

                        $page++;
                    } else {
                        return $this->cache = $releases->toArray();
                    }
                } else {
                    Console::warn("-> failed to fetch release, {0}", $response->statusMessage());
                    return $releases->toArray();
                }
            } while (true);
        }

        return $releases;
    }

    /**
     * @param string $pkgName
     * @return array
     */
    public function getVersions(string $pkgName): array
    {
        $releases = $this->fetchReleases();
        $result = [];

        foreach ($releases as $release) {
            if ($release['draft'] || $release['prerelease']) {
                continue;
            }

            Console::debug("GithubReleasesRepo.GetVersions.release '{0}'", $release['tag_name']);

            $versionInfo = $this->fetchVersionInfo($release);

            if ($versionInfo && $versionInfo['name'] === $pkgName) {
                $result[$versionInfo['version']] = flow($versionInfo)->onlyKeys(['size', 'sha256'])->toMap();
            }
        }


        return $result;
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $releases = $this->fetchReleases();

        if ($releases) {
            foreach ($releases as $release) {
                if ($release['draft'] || $release['prerelease']) {
                    continue;
                }

                $versionInfo = $this->fetchVersionInfo($release);

                if ($versionInfo && $versionInfo['name'] === $pkgName && $versionInfo['version'] === $pkgVersion) {
                    foreach ($release['assets'] as $sub) {
                        if ($sub['content_type'] === 'application/gzip') {
                            return fs::copy($sub['browser_download_url'], $toFile, null, 1024 * 128) > 0;
                        }
                    }

                    break;
                }
            }
        }

        return false;
    }
}
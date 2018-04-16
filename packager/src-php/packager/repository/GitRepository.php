<?php
namespace packager\repository;

use compress\GzipOutputStream;
use compress\TarArchive;
use git\Git;
use packager\cli\Console;
use php\io\IOException;
use php\lang\IllegalArgumentException;
use php\lang\System;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use php\net\URL;
use php\util\Regex;
use semver\SemVersion;

/**
 * Class GitRepository
 * @package packager\repository
 */
class GitRepository extends ExternalRepository
{
    private const PREFIXES = ['git://', 'git+http://', 'git+https://', 'git+ssh://', 'git+file://'];

    /**
     * @var Git
     */
    protected $git;

    public function __construct($source)
    {
        if (str::startsWith($source, "github.com/")
            || str::startsWith($source, "bitbucket.com/")
            || str::startsWith($source, "gitlab.com/")) {
            $source = "git+https://$source";
        }

        parent::__construct($source);
    }

    /**
     * @return string
     */
    public function getNormalSource(): string
    {
        $source = $this->getSource();

        if (str::startsWith($source, "git+")) {
            $source = str::sub($source, 4);
        }

        [$source, ] = str::split($source, '#');

        return $source;
    }

    public function getHash(): string
    {
        $source = $this->getSource();

        [$repo, $version] = str::split($source, '#');

        if (!$version) {
            return 'master';
        }

        return $version;
    }

    /**
     * @return string
     */
    public function getLocalDirectory(): string
    {
        $home = System::getProperty("jppm.home") . "/../download/git";

        $source = new URL($this->getNormalSource());

        $dir = "$home/{$source->getHost()}/{$source->getPath()}";
        return $dir;
    }

    /**
     * @return bool
     */
    public function isFit(): bool
    {
        $source = $this->getSource();

        foreach (self::PREFIXES as $prefix) {
            if (str::startsWith($source, $prefix)) {
                return true;
            }
        }

        return false;
    }

    protected function fetchGit()
    {
        if ($this->git) {
            return $this->git;
        }

        $dir = $this->getLocalDirectory();

        if (!fs::isDir($dir)) {
            if (!fs::makeDir($dir)) {
                throw new IOException("Failed to create directory '$dir'");
            }
        }

        $git = new Git($dir, true);
        $recreate = true;

        foreach ($git->remoteList() as $remote) {
            if ($remote['name'] === 'origin') {
                $uri = arr::first($remote['uris']);

                if ($uri !== $this->getNormalSource()) {
                    $recreate = true;
                } else {
                    $recreate = false;
                }

                break;
            }
        }

        if ($recreate) {
            fs::clean($dir);

            $git = new Git($dir, true);
            $git->remoteAdd('origin', $this->getNormalSource());

            Console::debug("Git.Clone '{0}' to '{1}'", $this->getNormalSource(), $dir);
        }

        $git->clean(['force' => true, 'cleanDirectories' => true]);
        $git->fetch(['removeDeletedRefs' => true, 'remote' => 'origin', 'tagOpt' => 'FETCH_TAGS']);

        //

        return $this->git = $git;
    }

    public function getVersions(string $pkgName): array
    {
        $git = $this->fetchGit();

        $result = [];

        foreach (['branch' => $git->branchList(['listMode' => 'REMOTE']), 'tag' => $git->getTags()] as $type => $refs) {
            foreach ($refs as $tag) {
                $name = $tag['name'];

                if (str::startsWith($name, 'refs/tags/')) {
                    $name = str::sub($name, 10);
                } else if (str::startsWith($name, 'refs/remotes/origin/')) {
                    $name = str::sub($name, 20);
                }

                if (!$tag['objectId']) {
                    continue;
                }

                try {
                    new SemVersion($name);
                } catch (IllegalArgumentException $e) {
                    if (str::startsWith($name, 'v')) {
                        $name = str::sub($name, 1);

                        try {
                            new SemVersion($name);
                        } catch (IllegalArgumentException $e) {
                            // nop.
                        }
                    }
                }

                $result[$name] = [
                    'realVersion' => $name,
                    'hash' => $tag['objectId']
                ];
            }
        }

        return $result;
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        return false;
    }

    public function downloadToDirectory(string $pkgName, string $pkgVersion, string $toDir): bool
    {
        $versions = $this->getVersions($pkgName);

        $pkgVersion = $versions[$pkgVersion]['hash'];

        if (!$pkgVersion) {
            return false;
        }

        $git = $this->fetchGit();

        $dir = $this->getLocalDirectory();

        $git->clean(['force' => true]);
        $ref = 'jppm-' . $pkgVersion;

        if ($git->findRef($ref)) {
            $git->branchDelete([$ref], true);
        }

        $git->branchCreate($ref, ['startPoint' => $pkgVersion, 'force' => true]);

        $git->checkout(['startPoint' => $ref, 'name' => $ref]);
        $git->merge([$pkgVersion], ['commit' => 'false']);

        fs::clean($toDir);

        fs::scan($dir, function ($filename) use ($dir, $toDir) {
            $name = fs::relativize($filename, $dir);

            if (fs::isFile($filename) && !str::startsWith($name, '.git/')) {
                fs::ensureParent("$toDir/$name");
                fs::copy($filename, "$toDir/$name", null, 1024 * 256);
            }
        });

        return true;
    }
}
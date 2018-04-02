<?php
namespace ssh;

use php\io\MiscStream;
use php\io\Stream;
use php\lib\fs;

/**
 * Class SSHSftpChannel
 * @package ssh
 *
 * @packages ssh
 */
class SSHSftpChannel extends SSHChannel
{
    /**
     * @readonly
     * @var string
     */
    public $home;

    /**
     * @readonly
     * @var string
     */
    public $version;

    /**
     * @readonly
     * @var int
     */
    public $serverVersion;

    /**
     * @readonly
     * @var int
     */
    public $bulkRequests;

    /**
     * @return string
     *
     * @throws SSHException
     */
    public function pwd(): string
    {
    }

    /**
     * @return string
     *
     * @throws SSHException
     */
    public function lpwd(): string
    {
    }

    /**
     * @param string $path
     *
     * @throws SSHException
     */
    public function cd(string $path): void
    {
    }

    /**
     * @param string $path
     *
     * @throws SSHException
     */
    public function lcd(string $path): void
    {
    }

    /**
     * @param int $gid
     * @param string $path
     *
     * @throws SSHException
     */
    public function chgrp(int $gid, string $path): void
    {
    }

    /**
     * @param int $permissions
     * @param string $path
     *
     * @throws SSHException
     */
    public function chmod(int $permissions, string $path): void
    {
    }

    /**
     * @param int $uid
     * @param string $path
     *
     * @throws SSHException
     */
    public function chown(int $uid, string $path): void
    {
    }

    /**
     * @param string $path
     * @param callable $lsSelector (array[name,longname,attrs] $entry): bool - return true to break.
     * @return array[]
     *
     * @throws SSHException
     */
    public function ls(string $path, callable $lsSelector = null): array
    {
    }


    /**
     * @param string $oldpath
     * @param string $newpath
     *
     * @throws SSHException
     */
    public function rename(string $oldpath, string $newpath): void
    {
    }

    /**
     * @param string $oldpath
     * @param string $newpath
     *
     * @throws SSHException
     */
    public function symlink(string $oldpath, string $newpath): void
    {
    }

    /**
     * @param string $oldpath
     * @param string $newpath
     *
     * @throws SSHException
     */
    public function hardlink(string $oldpath, string $newpath): void
    {
    }

    /**
     * @param string $dir
     *
     * @throws SSHException
     */
    public function mkdir(string $dir): void
    {
    }

    /**
     * @param string $dir
     *
     * @throws SSHException
     */
    public function rmdir(string $dir): void
    {
    }

    /**
     * @param string $path
     *
     * @throws SSHException
     */
    public function rm(string $path): void
    {
    }

    /**
     * @param string $path
     * @return string
     *
     * @throws SSHException
     */
    public function realpath(string $path): string
    {
    }

    /**
     * @param string $path
     * @return string
     *
     * @throws SSHException
     */
    public function readlink(string $path): string
    {
    }

    /**
     * @param string $path
     * @return array|null
     * @throws SSHException
     */
    public function stat(string $path): ?array
    {
    }

    /**
     * @param string $path
     * @return array|null
     * @throws SSHException
     */
    public function lstat(string $path): ?array
    {
    }

    /**
     * @param string $src
     * @param int $skip
     * @param callable|null $progressHandler (int $count, int $offset, int $max)
     * @return MiscStream
     *
     * @throws SSHException
     */
    public function get(string $src, int $skip = 0, callable $progressHandler = null): MiscStream
    {
    }

    /**
     * @param string $src
     * @param string $mode OVERWRITE, RESUME or APPEND.
     * @param callable|null $progressHandler (int $count, int $offset, int $max)
     * @param int $offset
     * @return MiscStream
     *
     * @throws SSHException
     */
    public function put(string $src, string $mode = 'OVERWRITE', callable $progressHandler = null, int $offset = 0): MiscStream
    {
    }

    /**
     * @param string $key
     * @return string
     */
    public function getExtension(string $key): string
    {
    }
}
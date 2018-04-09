<?php

use packager\cli\Console;
use php\io\IOException;
use php\io\Stream;
use php\lib\fs;

class Tasks
{
    /**
     * @param string $name
     * @param array $args
     */
    static function run(string $name, array $args = [])
    {
        global $app;
        $app->invokeTask($name, $args);
    }

    /**
     * @param string $path
     * @param string $content
     * @param bool $ignoreErrs
     * @return bool
     */
    static function createFile(string $path, string $content = '', bool $ignoreErrs = false): bool
    {
        Console::log("-> create new file '{0}'", $path);

        try {
            Stream::putContents($path, $content);
            return true;
        } catch (IOException $e) {
            Console::error("Failed to create file '{0}', cause = {0}", $path, $e->getMessage());
            if (!$ignoreErrs) {
                exit(-1);
            }

            return false;
        }
    }

    /**
     * @param string $path
     * @param bool $ignoreErrs
     * @return bool
     */
    static function deleteFile(string $path, bool $ignoreErrs = false): bool
    {
        if (fs::exists($path)) {
            Console::log("-> delete file '{0}'", $path);

            if (fs::delete($path)) {
                return true;
            } else {
                Console::error("Failed to delete file '{0}'", $path);
                if (!$ignoreErrs) {
                    exit(-1);
                }

                return false;
            }
        }

        return true;
    }

    /**
     * @param string $path
     * @param bool $ignoreErrs
     * @return bool
     */
    static function cleanDir(string $path, bool $ignoreErrs = false): bool
    {
        if (fs::isDir($path)) {
            Console::log("-> clean dir '{0}'", $path);

            $result = fs::clean($path);

            if (!$result['error']) {
                return true;
            } else {
                Console::error("Failed to clean dir '{0}'", $path);

                foreach ($result['error'] as $file) {
                    Console::log("--> unable to delete file '{0}'", $file);
                }

                if (!$ignoreErrs) {
                    exit(-1);
                }

                return false;
            }
        }

        return true;
    }

    /**
     * @param string $path
     * @param bool $ignoreErrs
     * @return bool
     */
    static function createDir(string $path, bool $ignoreErrs = false): bool
    {
        if (fs::isDir($path)) {
            return true;
        }

        Console::log("-> create dir '{0}'", $path);

        if (fs::makeDir($path)) {
            return true;
        } else {
            Console::error("Failed to create dir '{0}'", $path);
            if (!$ignoreErrs) {
                exit(-1);
            }

            return false;
        }
    }
}
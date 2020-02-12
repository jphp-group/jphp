<?php

use packager\cli\Console;
use packager\JavaExec;
use php\io\IOException;
use php\io\Stream;
use php\lang\System;
use php\lib\arr;
use php\lib\fs;

class Tasks
{
    /**
     * @param string $name
     * @param array $args
     * @param array $flags
     */
    static function run(string $name, array $args = [], ...$flags)
    {
        global $app;

        $app->invokeTask($name, $args, ...arr::combine($flags, $flags));
    }

    /**
     * @param string $dir
     * @param string $name
     * @param array $args
     * @param array ...$flags
     */
    static function runExternal(string $dir, string $name, array $args = [], ...$flags)
    {
        Console::log("-> {0} for '{1}'", $name, $dir);

        $exec = new JavaExec();
        $exec->addClassPath(System::getProperty("jppm.home") . "/packager-all.jar");
        $exec->addClassPath(System::getProperty("jppm.home") . "/buildSrc");

        $array = flow([$name], $args, flow($flags)->map(function ($e) {
            return "-$e";
        }))->toArray();

        $process = $exec->run($array, $dir);
        $process = $process->inheritIO()->startAndWait();

        if ($process->getExitValue() != 0) {
            exit($process->getExitValue());
        }
    }

    /**
     * Copy file or directory.
     *
     * @param string $path
     * @param string $intoDir
     * @param bool $ignoreErrs
     */
    static function copy(string $path, string $intoDir, bool $ignoreErrs = false)
    {
        if (fs::isFile($path)) {
            if (fs::copy($path, "$intoDir/" . fs::name($path), 1024 * 128) < 0) {
                Console::error("Failed to copy file '{0}' into dir '{1}'", $path, $intoDir);
                if (!$ignoreErrs) {
                    exit(-1);
                }
            }
        } else if (fs::isDir($path)) {
            fs::scan($path, function ($file) use ($path, $intoDir, $ignoreErrs) {
                $name = fs::relativize($file, $path);

                if (fs::isDir($file)) {
                    fs::makeDir("$intoDir/$name");
                    return;
                }

                fs::ensureParent("$intoDir/$name");

                if (fs::copy($file, "$intoDir/$name", 1024 * 128) < 0) {
                    Console::error("Failed to copy file '{0}' into dir '{1}'", $path, $intoDir);
                    if (!$ignoreErrs) {
                        exit(-1);
                    }
                }
            });
        }
    }

    /**
     * @param string $path
     * @param string $content
     * @param bool $ignoreErrs
     * @return bool
     */
    static function createFile(string $path, string $content = '', bool $ignoreErrs = false): bool
    {
        Console::info("Create new file '{0}'", $path);

        try {
            fs::ensureParent($path);
            Stream::putContents($path, $content);
            return true;
        } catch (IOException $e) {
            Console::error("Failed to create file '{0}', cause = {1}", $path, $e->getMessage());
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
            Console::info("Delete file '{0}'", $path);

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
     * @param array $filter
     * @param bool $ignoreErrs
     * @return bool
     */
    static function cleanDir(string $path, array $filter = [], bool $ignoreErrs = false): bool
    {
        if (fs::isDir($path)) {
            Console::info("Clean dir '{0}'", $path);

            $result = fs::clean($path, $filter);

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

        Console::info("Create dir '{0}'", $path);

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
<?php
namespace packager\cli;


use php\io\Stream;
use php\lang\System;
use php\lib\arr;
use php\lib\str;
use php\util\Scanner;

class Console
{
    public static function log($message, ...$args)
    {
        $stream = System::out();

        foreach ($args as $i => $arg) {
            $message = str::replace($message, "\{$i\}", $arg);
        }

        $stream->write($message);
        $stream->write("\n");
    }

    public static function warn($message, ...$args)
    {
        static::log("[WARNING] $message", ...$args);
    }

    public static function error($message, ...$args)
    {
        static::log("[ERROR] $message", ...$args);
    }

    public static function readYesNo(string $message, bool $default = false): bool
    {
        $result = str::lower(static::read("$message (Y/n)", $default ? "y" : "n"));

        if (arr::has(['yes', 'y'], $result)) return true;
        if (arr::has(['no', 'n'], $result)) return false;

        static::log(" -> please enter Y (yes) or N (no), try again ...");

        return static::readYesNo($message, $default);
    }

    public static function read(string $message, string $default = null): string
    {
        $stream = Stream::of('php://stdout', 'w');
        $stream->write($message . " ");

        $stdin = new Scanner(Stream::of('php://stdin', 'r'));
        if ($stdin->hasNextLine()) {
            $line = $stdin->nextLine();
            if (!$line) {
                return $default;
            }

            return $line;
        }

        return null;
    }
}
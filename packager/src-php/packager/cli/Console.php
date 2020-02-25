<?php
namespace packager\cli;

use packager\Colors;
use php\io\Stream;
use php\lang\System;
use php\lib\arr;
use php\lib\str;
use php\util\Scanner;

class Console
{
    public static function isXTerm(): bool
    {
        static $xterm;

        if ($xterm === null) {
            $xterm = str::equalsIgnoreCase($_ENV['TERM'], 'xterm');
        }

        return $xterm;
    }

    public static function log($message, ...$args)
    {
        static::print($message, ...$args);

        System::out()->write("\n");
    }

    public static function printForXterm($message, ...$args)
    {
        if (Console::isXTerm()) {
            static::print($message, ...$args);
        }
    }

    public static function print($message, ...$args)
    {
        $stream = System::out();

        foreach ($args as $i => $arg) {
            $message = str::replace($message, "{{$i}}", $arg);
        }

        $stream->write($message);
    }

    public static function debug($message, ...$args)
    {
        global $app;
        if ($app->isDebug()) {
            static::log(Colors::withColor('(debug)', 'silver')." $message", ...$args);
        }
    }

    public static function warn($message, ...$args)
    {
        static::log(Colors::withColor('(warning)', 'yellow')." $message", ...$args);
    }

    public static function error($message, ...$args)
    {
        static::log(Colors::withColor('(error)', 'red')." $message", ...$args);
    }

    public static function info($message, ...$args)
    {
        static::log(Colors::withColor('(info)', 'magenta')." $message", ...$args);
    }

    public static function readYesNo(string $message, bool $default = false): bool
    {
        $result = str::lower(static::read("$message (Y/n)", $default ? "yes" : "no"));

        if (arr::has(['yes', 'y'], $result)) return true;
        if (arr::has(['no', 'n'], $result)) return false;

        static::log(" -> please enter ".Colors::withColor('Y', 'green')." (yes) or ".Colors::withColor('N', 'yellow')." (no), try again ...");

        return static::readYesNo($message, $default);
    }

    public static function read(string $message, string $default = null): string
    {
        $stream = Stream::of('php://stdout', 'w');
        $stream->write($message . " ");

        if ($default) {
            $stream->write("(default = $default) ");
        }

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
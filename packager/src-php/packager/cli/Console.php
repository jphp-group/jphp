<?php
namespace packager\cli;


use php\io\Stream;
use php\lib\str;
use php\util\Scanner;

class Console
{
    public static function log($message, ...$args)
    {
        $stream = Stream::of('php://stdout', 'w');

        foreach ($args as $i => $arg) {
            $message = str::replace($message, "\{$i\}", $arg);
        }

        $stream->write($message);
        $stream->write("\n");
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
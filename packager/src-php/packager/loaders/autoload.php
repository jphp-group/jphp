<?php

use php\io\Stream;
use php\lib\fs;
use php\lib\str;

class Autoloader
{
    function __construct(array $sources)
    {
        foreach ($sources as $source) {
            $this->add($source['ns'], $source['dirs'], $source['type']);
        }
    }

    function add(string $ns, array $dirs, string $type = 'std')
    {
        switch ($type) {
            case 'std':
                spl_autoload_register(function ($className) use ($ns, $dirs) {
                     if (str::startsWith($className, $ns)) {
                         $fileName = str::replace($className, '\\', '/') . ".php";

                         foreach ($dirs as $dir) {
                            if (Stream::exists($file = "$dir/$fileName")) {
                                require $file;
                                return;
                            }
                         }
                     }
                });
                break;
        }
    }
}

return new Autoloader(fs::parseAs('./autoload.json', 'json'));
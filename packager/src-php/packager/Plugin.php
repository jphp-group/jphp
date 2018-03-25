<?php
namespace packager;

use packager\cli\ConsoleApp;
use php\lib\fs;
use php\lib\str;

abstract class Plugin
{
    /**
     * @var Package
     */
    protected $package;

    /**
     * Plugin constructor.
     * @param Package $package
     */
    public function __construct(Package $package)
    {
        $this->package = $package;
    }

    /**
     * @param ConsoleApp $consoleApp
     * @return bool
     */
    public function beforeConsole(ConsoleApp $consoleApp)
    {
    }

    /**
     * @param string $pluginDir
     */
    public static function registerLoaders(string $pluginDir)
    {
        spl_autoload_register(function ($className) use ($pluginDir) {
            if (str::startsWith($className, "plugins\\")) {
                $file = $pluginDir . '/' . str::replace(str::sub($className, 8), '\\', '/') . '.php';

                if (fs::isFile($file)) {
                    require $file;
                }
            }
        });
    }
}
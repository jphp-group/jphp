<?php
namespace php\lang;

use php\io\File;
use php\io\Stream;

/**
 * Class Module
 * @packages std, core
 */
class Module
{


    /**
     * Register all functions and classes of module in current environment
     *
     * @param File|Stream|string $source
     * @param bool $compiled
     * @param bool $debugInformation
     */
    public function __construct($source, $compiled = false, $debugInformation = true) { }

    /**
     * @return string
     */
    public function getName() {}

    /**
     * Include module and return result
     * @param array $variables
     * @return mixed
     */
    public function call(array $variables = null) { }

    /**
     * Dump all the jvm classes of the module as .class files into targetDir
     * @param string $targetDir
     * @param bool $saveDebugInfo
     * @return array compile result
     */
    public function dumpJVMClasses(string $targetDir, bool $saveDebugInfo = true): array {
    }

    /**
     * @param File|Stream|string $target
     * @param bool $saveDebugInfo
     * @param bool $includeJvmData
     */
    public function dump($target, bool $saveDebugInfo = true, bool $includeJvmData = true) { }

    /**
     * Java Bytecode data (.class file)
     * @return string binary string
     */
    public function getData(): string { }

    /**
     * Remove bytecode data.
     */
    public function cleanData() {}

    /**
     * @return \ReflectionClass[]
     */
    public function getClasses(): array
    {
    }

    /**
     * @return \ReflectionFunction[]
     */
    public function getFunctions(): array {}

    /**
     * @return array
     */
    public function getConstants(): array {}

    /**
     * @param string $name
     * @param array $classes
     */
    public static function package($name, array $classes) { }
}

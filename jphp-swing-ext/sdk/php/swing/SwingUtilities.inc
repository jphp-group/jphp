<?php
namespace php\swing;

/**
 * Class SwingUtilities
 */
final class SwingUtilities {

    private function __construct(){}

    /**
     * @param callable $handler (Exception|JavaObject $exception)
     */
    public static function setExceptionHandler(callable $handler){}

    /**
     * @param callable $runner
     */
    public static function invokeLater(callable $runner){}
}

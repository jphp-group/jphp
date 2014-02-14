<?php
namespace php\lang;

/**
 * Class System
 * @package php\lang
 */
final class System {

    private function __construct() { }

    /**
     * Exit from program with status globally
     * @param int $status
     */
    public static function halt($status) { }
}
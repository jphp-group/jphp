<?php
namespace packager;

use packager\cli\Console;
use php\lang\System;
use php\lib\char;
use php\lib\str;
use system\DFFIConsole;

class Colors {
    public static $ANSI_CODES = array(
        "off"        => 0,
        "bold"       => 1,
        "italic"     => 3,
        "underline"  => 4,
        "blink"      => 5,
        "inverse"    => 7,
        "hidden"     => 8,
        "gray"       => 30,
        "red"        => 31,
        "green"      => 32,
        "yellow"     => 33,
        "blue"       => 34,
        "magenta"    => 35,
        "cyan"       => 36,
        "silver"     => "0;37",
        "white"      => 37,
        "black_bg"   => 40,
        "red_bg"     => 41,
        "green_bg"   => 42,
        "yellow_bg"  => 43,
        "blue_bg"    => 44,
        "magenta_bg" => 45,
        "cyan_bg"    => 46,
        "white_bg"   => 47,
    );

    public static function withColor($str, $color)
    {
        if (!DFFIConsole::hasColorSupport()) {
            return $str;
        }

        $color_attrs = explode("+", $color);
        $ansi_str = "";

        foreach ($color_attrs as $attr) {
            $ansi_str .= char::of(27) . "[" . self::$ANSI_CODES[$attr] . "m";
        }

        $ansi_str .= $str . char::of(27) . "[" . self::$ANSI_CODES["off"] . "m";
        return $ansi_str;
    }
}
?>
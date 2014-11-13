<?php

use php\format\JsonProcessor;
use php\util\Locale;

class X {
    static function get() {
        $processor = new JsonProcessor();
        $locale = Locale::RUSSIA();
        return $processor->format(['message' => $locale->getLanguage()]);
    }
}

return X::get();
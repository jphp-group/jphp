<?php
namespace php\swing;

use php\io\File;
use php\io\Stream;

/**
 * Class XmlUIReader
 * @package php\swing
 */
class UIReader {

    /**
     * @param Stream|File|string $stream
     * @return UIElement
     */
    public function read($stream) { }

    /**
     * @param callable $handle  (UIElement $el, $var)
     */
    public function onRead(callable $handle = NULL) { }

    /**
     * @param callable $handle  (UIElement $el, $value) -> mixed
     */
    public function onTranslate(callable $handle = NULL) { }
}
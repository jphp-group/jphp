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
     * Enables that the reader will create instances of UIInternalForm insteadof UIForm and UIDialog
     * @var bool
     */
    public $useInternalForms;

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

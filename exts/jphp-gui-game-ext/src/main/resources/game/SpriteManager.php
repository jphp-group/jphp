<?php
namespace game;

use php\game\UXSprite;
use php\gui\UXDialog;
use php\gui\UXImage;
use php\gui\UXNode;
use php\io\IOException;
use php\io\Stream;
use php\lang\IllegalStateException;
use php\xml\XmlProcessor;

/**
 * Class SpriteManager
 * @package game
 */
class SpriteManager
{
    /**
     * @var UXSprite[]
     */
    protected $sprites = [];

    /**
     * @var SpriteSpec[]
     */
    protected $spriteSpecs = [];

    /**
     * SpriteManager constructor.
     */
    public function __construct()
    {

    }

    static function current()
    {
        static $instance = null;

        if ($instance == null) {
            return $instance = new SpriteManager();
        }

        return $instance;
    }

    /**
     * @param SpriteSpec $spec
     * @return UXSprite
     */
    public function register(SpriteSpec $spec)
    {
        $sprite = new UXSprite();
        $sprite->image = new UXImage("res://{$spec->file}");
        $sprite->speed = $spec->speed;
        $sprite->frameSize = [$spec->frameWidth, $spec->frameHeight];

        foreach ($spec->animations as $name => $indexes) {
            $sprite->setAnimation($name, $indexes);
        }

        $sprite->currentAnimation = $spec->defaultAnimation;
        $this->_register($spec, $sprite);
        return $sprite;
    }

    /**
     * @param $name
     * @param UXSprite $sprite
     * @throws IllegalStateException
     */
    private function _register($name, UXSprite $sprite)
    {
        if ($this->sprites[$name]) {
            throw new IllegalStateException("Sprite '$name' already register");
        }

        $this->sprites[$name] = $sprite;
    }

    /**
     * @param $name
     * @return null|UXSprite
     */
    public function fetch($name)
    {
        if ($sprite = $this->get($name)) {
            return $sprite;
        }

        $xml = new XmlProcessor();
        try {
            $document = $xml->parse(Stream::getContents("res://.game/sprites/$name.sprite"));

            $spec = new SpriteSpec($name, $document->find('/sprite'));
            $this->spriteSpecs[$name] = $spec;

            return $this->register($spec);
        } catch (IOException $e) {
            return null;
        }
    }

    public function fetchSpec($name)
    {
        if ($spriteSpec = $this->spriteSpecs[$name]) {
            return $spriteSpec;
        }

        $xml = new XmlProcessor();
        try {
            $document = $xml->parse(Stream::getContents("res://.game/sprites/$name.sprite"));

            $spec = new SpriteSpec($name, $document->find('/sprite'));
            $this->spriteSpecs[$name] = $spec;

            $this->register($spec);

            return $spec;
        } catch (IOException $e) {
            return null;
        }
    }

    /**
     * @param $name
     * @return UXSprite
     */
    public function get($name)
    {
        return $this->sprites[$name];
    }

    /**
     * @param $name
     * @return UXSprite
     */
    public function __get($name)
    {
        return $this->get($name);
    }

}
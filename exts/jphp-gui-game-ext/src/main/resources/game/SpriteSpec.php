<?php
namespace game;

use php\game\UXSprite;
use php\gui\UXNode;
use php\lib\Items;
use php\lib\Str;
use php\util\Flow;
use php\xml\DomElement;

/**
 * Class SpriteSpec
 * @package game
 */
class SpriteSpec
{
    const DATA_PROPERTY_NAME = '--sprite-spec';

    /**
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $file;

    /**
     * @var string
     */
    public $schemaFile = null;

    /**
     * @var int
     */
    public $frameWidth;

    /**
     * @var int
     */
    public $frameHeight;

    /**
     * @var int
     */
    public $speed = 12;

    /**
     * @var array of int[]
     */
    public $animations = [];

    /**
     * @var string
     */
    public $fixtureType;

    /**
     * @var array
     */
    public $fixtureData = [];

    /**
     * @var bool
     */
    public $metaCentred = true;

    /**
     * @var bool
     */
    public $metaAutoSize = false;

    /**
     * @var string
     */
    public $defaultAnimation;

    /**
     * @param string $sprite
     * @param UXNode $node
     */
    public static function apply($sprite, $node)
    {
        $manager = SpriteManager::current();

        $sprite = $manager->fetch($sprite);
        $spriteSpec = $manager->fetchSpec($sprite);

        $node->data(SpriteSpec::DATA_PROPERTY_NAME, $spriteSpec);
        $node->sprite = $sprite;
    }

    /**
     * @param $name
     * @param DomElement $element
     */
    function __construct($name, DomElement $element = null)
    {
        $this->name = $name;

        if ($element) {
            $this->file = $element->getAttribute('file');

            $this->frameWidth = (int)$element->getAttribute('frameWidth');
            $this->frameHeight = (int)$element->getAttribute('frameHeight');
            $this->defaultAnimation = $element->getAttribute('defaultAnimation');
            $this->metaCentred = (bool) $element->getAttribute('metaCentred');
            $this->metaAutoSize = (bool) $element->getAttribute('metaAutoSize');

            if ($element->hasAttribute('speed')) {
                $this->speed = (int)$element->getAttribute('speed');
            }

            $this->readFixture($element);
            $this->readAnimation($element);
        }
    }

    private function readAnimation(DomElement $element)
    {
        /** @var DomElement $domAnimation */
        foreach ($element->findAll('./animation') as $domAnimation) {
            $name = $domAnimation->getAttribute('name');

            $this->animations[$name] = Flow::of(Str::split($domAnimation->getAttribute('indexes'), ','))->map(function ($one) {
                return (int)trim($one);
            })->toArray();
        }
    }

    private function readFixture(DomElement $element)
    {
        if ($element->getAttribute('fixtureType')) {
            $this->fixtureType = $element->getAttribute('fixtureType');

            $point = [];
            $data  = [];

            foreach (str::split($element->getAttribute('fixtureData'), ',') as $p) {
                $point[] = $p;

                if (sizeof($point) == 2) {
                    $data[] = $point;
                    $point = [];
                }
            }

            if (sizeof($data) == 1) {
                $data = $data[0];
            }

            $this->fixtureData = $data;
        }
    }

    public function removeFromAnimations(array $indexes)
    {
        foreach ($this->animations as $name => $list) {
            $this->removeFromAnimation($name, $indexes);
        }
    }

    public function removeFromAnimation($animation, array $indexes)
    {
        $this->animations[$animation] = Flow::of($this->animations[$animation])->find(function ($el) use ($indexes) {
            return !in_array($el, $indexes);
        })->toArray();
    }
}
<?php
namespace php\gui\effect;
use Countable;
use Iterator;

/**
 * Class UXEffectPipeline
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXEffectPipeline implements Iterator, Countable, \ArrayAccess
{
    /**
     * @var int
     */
    public $count = 0;

    /**
     * Clear effects.
     */
    public function clear()
    {
    }

    /**
     * @param UXEffect $effect
     */
    public function add(UXEffect $effect)
    {
    }

    /**
     * @param $index
     * @param UXEffect $effect
     */
    public function insert($index, UXEffect $effect)
    {
    }

    /**
     * @param UXEffect[] $effects
     */
    public function addAll(array $effects)
    {
    }

    /**
     * @param UXEffect $effect
     */
    public function remove(UXEffect $effect)
    {
    }

    /**
     * @param UXEffect $effect
     * @return bool
     */
    public function has(UXEffect $effect)
    {
    }

    /**
     * @param UXEffect $effect
     */
    public function enable(UXEffect $effect)
    {
    }

    /**
     * @param UXEffect $effect
     */
    public function disable(UXEffect $effect)
    {
    }

    /**
     * @param UXEffect $effect
     * @return bool
     */
    public function isEnabled(UXEffect $effect)
    {
    }

    /**
     * @return UXEffect[]
     */
    public function all()
    {
    }

    public function current()
    {
    }

    public function next()
    {
    }

    public function key()
    {
    }

    public function valid()
    {
    }

    public function rewind()
    {
    }

    public function count()
    {
    }

    public function offsetExists($offset)
    {
    }

    public function offsetGet($offset)
    {
    }

    public function offsetSet($offset, $value)
    {
    }

    public function offsetUnset($offset)
    {
    }
}
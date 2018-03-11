<?php
namespace php\gui;

/**
 * Class UXSpinner
 * @package php\gui
 * @packages gui, javafx
 */
class UXSpinner extends UXControl
{
    /**
     * @var bool
     */
    public $editable = true;

    /**
     * @readonly
     * @var UXTextField
     */
    public $editor;

    /**
     * @readonly
     * @var mixed
     */
    public $value;

    /**
     * Text field alignment (pos).
     *
     * @var string
     */
    public $alignment = 'CENTER_LEFT';

    /**
     * @param int $steps
     */
    public function increment($steps = 1)
    {
    }

    /**
     * @param int $steps
     */
    public function decrement($steps = 1)
    {
    }

    /**
     * @param callable|null $incrementHandler
     * @param callable|null $decrementHandler
     */
    public function setValueFactory($incrementHandler, $decrementHandler)
    {
    }

    /**
     * @param int $min (optional)
     * @param int $max (optional)
     * @param int $initial (optional)
     * @param int $step
     */
    public function setIntegerValueFactory($min, $max, $initial, $step = 1)
    {
    }
}
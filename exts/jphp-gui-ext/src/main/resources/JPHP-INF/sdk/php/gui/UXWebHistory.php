<?php
namespace php\gui;

/**
 * Class UXWebHistory
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXWebHistory
{
    /**
     * @readonly
     * @var int
     */
    public $currentIndex;

    /**
     * @var int
     */
    public $maxSize;

    /**
     * @param int $offset
     */
    public function go($offset)
    {
    }

    public function goBack()
    {
    }

    public function goForward()
    {
    }

    /**
     * @return array [url, title, lastVisited]
     */
    public function getEntries()
    {
    }

    /**
     * @param string $property
     * @return UXValue
     */
    public function observer($property)
    {
    }
}
<?php
namespace helloworld;

/**
 * Class HelloWorld
 * @package helloworld
 */
class HelloWorld
{
    private $text;

    /**
     * HelloWorld constructor.
     * @param string $text
     */
    public function __construct(string $text)
    {
        $this->text = $text;
    }

    public function print()
    {
        echo $this->text, "\n";
    }
}
--TEST--
Bug #123
--FILE--
<?php

class a implements \RecursiveIterator
{
    public function current()
    {
    }

    public function key()
    {
    }

    public function next()
    {
    }

    public function rewind()
    {
    }

    public function valid()
    {
    }

    public function hasChildren()
    {
    }

    public function getChildren()
    {
    }
}?>
--EXPECT--

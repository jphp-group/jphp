<?php
namespace phpx\parser;

/**
 * Class ArgumentRecord
 * @package phpx\parser
 */
class ArgumentRecord extends AbstractSourceRecord
{
    /**
     * @var string
     */
    public $exprValue;

    /**
     * ANY, INT, DOUBLE, ARRAY, BOOLEAN, CALLABLE, STRING, OBJECT, VARARG, TRAVERSABLE, ITERABLE, VOID, SELF
     * @var string
     */
    public $hintType = 'ANY';

    /**
     * @var string
     */
    public $hintTypeClass;

    /**
     * @var bool
     */
    public $optional = false;

    /**
     * @var bool
     */
    public $reference = false;

    /**
     * @var bool
     */
    public $variadic = false;
}
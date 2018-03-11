<?php
namespace phpx\parser;

/**
 * Class SourceToken
 * @package phpx\parser
 */
abstract class SourceToken
{
    /**
     * ClassStmt, FunctionStmt, Name, ...
     * @var string
     */
    public $type;

    /**
     * @var string
     */
    public $word;

    /**
     * @var int
     */
    public $line;

    /**
     * @var int
     */
    public $position;

    /**
     * @var int
     */
    public $endLine;

    /**
     * @var int
     */
    public $endPosition;

    /**
     * @return bool
     */
    public function isNamedToken()
    {
    }

    /**
     * @return bool
     */
    public function isStatementToken()
    {
    }

    /**
     * @return bool
     */
    public function isExpressionToken()
    {
    }

    /**
     * @return bool
     */
    public function isOperatorToken()
    {
    }

    /**
     * @return array
     */
    public function getMeta()
    {
    }
}
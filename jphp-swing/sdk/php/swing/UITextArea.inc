<?php
namespace php\swing;

/**
 * Class UITextArea
 * @package php\swing
 */
class UITextArea extends UITextElement {
    /**
     * @var bool
     */
    public $lineWrap;

    /**
     * @var bool
     */
    public $wrapStyleWord;

    /**
     * @var int
     */
    public $rows;

    /**
     * @var int
     */
    public $columns;

    /**
     * @var int
     */
    public $tabSize;

    /**
     * @readonly
     * @var int
     */
    public $lineCount;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $horScrollPolicy;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $verScrollPolicy;
}

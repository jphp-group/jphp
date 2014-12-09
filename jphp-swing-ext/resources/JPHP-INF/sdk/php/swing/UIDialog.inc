<?php
namespace php\swing;

/**
 * Class UIDialog
 * @package php\swing
 */
class UIDialog extends UIWindow {
    const PLAIN_MESSAGE = -1;
    const ERROR_MESSAGE = 0;
    const INFORMATION_MESSAGE = 1;
    const WARNING_MESSAGE = 2;
    const QUESTION_MESSAGE = 3;

    const DEFAULT_OPTION = -1;
    const OK_CANCEL_OPTION = 2;
    const YES_NO_CANCEL_OPTION = 1;
    const YES_NO_OPTION = 0;

    const YES_OPTION = 0;
    const NO_OPTION = 1;
    const CANCEL_OPTION = 2;
    const OK_OPTION = 0;
    const CLOSED_OPTION = -1;

    /**
     * @var bool
     */
    public $modal;

    /**
     * @var string - modeless or document_modal or application_modal or toolkit_modal
     */
    public $modalType;

    /**
     * @param UIWindow $owner
     */
    public function __construct(UIWindow $owner = null) { }


    /**
     * Show basic message
     * @param string $text
     * @param string $title
     * @param int $type
     */
    public static function message($text, $title, $type = self::INFORMATION_MESSAGE) { }

    /**
     * @param string $text
     * @param string $title
     * @param int $optionType
     * @param int $type
     * @return int
     */
    public static function confirm($text, $title, $optionType = self::YES_NO_OPTION, $type = self::QUESTION_MESSAGE) { }

    /**
     * @param string $message
     * @param string $initialValue
     * @return string
     */
    public static function input($message, $initialValue = '') { return ''; }
}

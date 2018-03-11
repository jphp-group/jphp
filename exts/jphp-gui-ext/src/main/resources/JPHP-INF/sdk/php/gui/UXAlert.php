<?php
namespace php\gui;

/**
 * Class UXAlert
 * --RU--
 * Класс для отображения всплывающих диалогов с кнопками.
 *
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXAlert
{
    /** @var string */
    public $contentText = '';

    /** @var string */
    public $headerText = '';

    /** @var string */
    public $title = '';

    /** @var bool */
    public $expanded = true;

    /** @var UXNode */
    public $graphic = null;

    /** @var UXNode */
    public $expandableContent = null;

    /**
     * @param $alertType
     **/
    public function __construct($alertType)
    {
    }

    /**
     * @param array $buttons
     */
    public function setButtonTypes(array $buttons)
    {
    }

    /**
     * Показать диалог.
     */
    public function show()
    {
    }


    /**
     * Скрыть диалог.
     */
    public function hide()
    {
    }

    /**
     * @return mixed
     */
    public function showAndWait()
    {
    }
}
<?php
namespace php\gui;

use php\gui\event\UXEvent;
use php\gui\layout\UXPane;
use php\lang\IllegalStateException;

/**
 * Class UXWindow
 * @package php\gui
 * @packages gui, javafx
 *
 * @property bool $focused
 */
abstract class UXWindow
{
    /**
     * --RU--
     * Сцена.
     * @var UXScene
     */
    public $scene;

    /**
     * @var double
     */
    public $x;

    /**
     * @var double
     */
    public $y;

    /**
     * @var double
     */
    public $width;

    /**
     * @var double
     */
    public $height;

    /**
     * @var double
     */
    public $opacity = 1.0;

    /**
     * --RU--
     * Размеры [width, height]
     * @var double[]
     */
    public $size;

    /**
     * @var UXPane
     */
    public $layout;

    /**
     * --RU--
     * Компоненты.
     *
     * @readonly
     * @var UXList
     */
    public $children;

    /**
     * --RU--
     * Видимость.
     *
     * @var bool
     */
    public $visible;

    /**
     * --RU--
     * Тип курсора.
     *
     * @var string
     */
    public $cursor;

    /**
     * --RU--
     * Любые данные.
     *
     * @var mixed
     */
    public $userData = null;

    /**
     * --RU--
     * Перевести фокус на окно.
     */
    public function requestFocus()
    {
    }

    /**
     * --RU--
     * Показать окно.
     */
    public function show()
    {
    }

    /**
     * --RU--
     * Скрыть окно.
     */
    public function hide()
    {
    }

    /**
     * --RU--
     * Отцентрировать окно относительно разрешения.
     */
    public function centerOnScreen()
    {
    }

    /**
     *
     */
    public function sizeToScene()
    {
    }

    /**
     * Getter and Setter for object data.
     * --RU--
     * Пополнительные данные окна (геттер и сеттер).
     * @param string $name
     * @param mixed $value [optional]
     * @return mixed
     */
    public function data($name, $value)
    {
    }

    /**
     * --RU--
     * Навесить событие.
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general')
    {
    }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group)
    {
    }

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e)
    {
    }

    /**
     * @param string $event
     * @param callable $filter
     */
    public function addEventFilter($event, callable $filter)
    {
    }

    /**
     * Use observer() ..
     * @deprecated
     * @param string $property
     * @param callable $listener (UXWindow $self, $property, $oldValue, $newValue)
     */
    public function watch($property, callable $listener)
    {
    }

    /**
     * @param string $property
     * @return UXValue
     * @throws IllegalArgumentException
     */
    public function observer($property)
    {
    }

    /**
     * --RU--
     * Добавить компонент.
     *
     * @param UXNode $node
     *
     * @throws IllegalStateException
     */
    public function add(UXNode $node)
    {
    }

    /**
     * --RU--
     * Удалить компонент.
     *
     * @param UXNode $node
     *
     * @return bool
     * @throws IllegalStateException
     */
    public function remove(UXNode $node)
    {
    }

    /**
     * --RU--
     * Добавить файл css стилей окну.
     *
     * @param string $path
     */
    public function addStylesheet($path)
    {
    }

    /**
     * --RU--
     * Удалить файл css стилей у окна.
     *
     * @param string $path
     */
    public function removeStylesheet($path)
    {
    }

    /**
     * --RU--
     * Возвращает true если указанный файл css стилей уже добавлен окну.
     *
     * @param string $path
     * @return bool
     */
    public function hasStylesheet($path)
    {
    }

    /**
     * --RU--
     * Очистить от всех внешних стилей.
     */
    public function clearStylesheets()
    {
    }

    /**
     * Make layout virtual.
     */
    public function makeVirtualLayout()
    {
    }

    /**
     * @param string $id
     *
     * @return UXNode|null
     */
    public function __get($id)
    {
    }

    /**
     * @param string $id
     * @return bool
     */
    public function __isset($id)
    {
    }
}
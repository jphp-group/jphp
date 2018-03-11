<?php
namespace php\gui;

use php\gui\effect\UXEffect;
use php\gui\effect\UXEffectPipeline;
use php\gui\event\UXEvent;
use php\lang\IllegalArgumentException;
use php\gui\UXList;

/**
 * Class UXNode
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXNode
{


    /**
     * --RU--
     * Идентификатор.
     * @var string
     */
    public $id;

    /**
     * --RU--
     * CSS стили компонента в одну строку.
     * @var string
     */
    public $style;

    /**
     * --RU--
     * Родительский компонент.
     * @var UXParent
     */
    public $parent;

    /**
     * @var bool
     */
    public $cache = false;

    /**
     * DEFAULT, SPEED, QUALITY, SCALE, ROTATE, SCALE_AND_ROTATE
     * @var string
     */
    public $cacheHint = 'DEFAULT';

    /**
     * --RU--
     * Эффекты компонента.
     * @var UXEffectPipeline
     */
    public $effects;

    /**
     * @var UXNode
     */
    public $clip = null;

    /**
     * --RU--
     * Сцена компонента.
     * @var UXScene
     */
    public $scene;

    /**
     * --RU--
     * Форма компонента.
     * @var UXForm
     */
    public $form;

    /**
     * --RU--
     * Окно компонента
     * @var UXWindow
     */
    public $window;

    /**
     * --RU--
     * Позиция по X.
     *
     * @var double
     */
    public $x;

    /**
     * --RU--
     * Позиция по Y.
     *
     * @var double
     */
    public $y;

    /**
     * --RU--
     * Смещение по X.
     * @var double
     */
    public $translateX;

    /**
     * --RU--
     * Смещение по Y.
     * @var double
     */
    public $translateY;

    /**
     * @var double
     */
    public $translateZ;

    /**
     * @var double
     */
    public $scaleX = 1.0;

    /**
     * @var double
     */
    public $scaleY = 1.0;

    /**
     * @var double
     */
    public $scaleZ = 1.0;

    /**
     * Scale [X, Y]
     * @var double[]
     */
    public $scale = [1.0, 1.0];

    /**
     * --RU--
     * Экранная позция по X
     * @var double
     */
    public $screenX;

    /**
     * --RU--
     * Экранная позиция по Y
     * @var double
     */
    public $screenY;

    /**
     * Screen position [X, Y]
     * --RU--
     * Экранная позиция по [X, Y]
     * @var double[]
     */
    public $screenPosition;

    /**
     * --RU--
     * Ширина компонента.
     * @var double
     */
    public $width;

    /**
     * --RU--
     * Высота компонента.
     * @var double
     */
    public $height;

    /**
     * --RU--
     * Размеры (ширина, высота)
     * @var double[]
     */
    public $size;

    /**
     * --RU--
     * Позиция (x, y)
     * @var double[]
     */
    public $position;

    /**
     * --RU--
     * Видимость
     * @var bool
     */
    public $visible = true;

    /**
     * @var bool
     */
    public $managed = true;

    /**
     * --RU--
     * Доступность
     * @var bool
     */
    public $enabled = true;

    /**
     * --RU--
     * Прозрачность от 0 до 1.
     * @var double
     */
    public $opacity = 1;

    /**
     * --RU--
     * Угол поворота (от 0 до 360).
     * @var double 0..360
     */
    public $rotate = 0;

    /**
     * --RU--
     * Сфокусированный.
     * @readonly
     * @var bool
     */
    public $focused = false;

    /**
     * --RU--
     * Доступность фокусировки через Tab.
     * @var bool
     */
    public $focusTraversable = true;

    /**
     * --RU--
     * Список CSS классов.
     * @readonly
     * @var UXList of string
     */
    public $classes;

    /**
     * --RU--
     * Список CSS классов одной строкой через пробел.
     * @var string
     */
    public $classesString;

    /**
     * --RU--
     * Пользовательские данные.
     * @var mixed
     */
    public $userData = null;

    /**
     * --RU--
     * Игнорирование действий мышки.
     * @var bool
     */
    public $mouseTransparent = false;

    /**
     * --RU--
     * Курсор
     * @var string|UXImage
     */
    public $cursor = 'DEFAULT';

    /**
     * --RU--
     * Якорь.
     * @var double|null
     */
    public $leftAnchor, $rightAnchor, $topAnchor, $bottomAnchor = null;

    /**
     * --RU--
     * Якоря для фиксации размеров и позиции.
     * @var array
     */
    public $anchors = [];

    /**
     * @readonly
     * @var array
     */
    public $layoutBounds = ['x' => 0.0, 'y' => 0.0, 'z' => 0.0, 'width' => 0.0, 'height' => 0.0, 'depth' => 0.0];

    /**
     * @readonly
     * @var array
     */
    public $boundsInParent = ['x' => 0.0, 'y' => 0.0, 'z' => 0.0, 'width' => 0.0, 'height' => 0.0, 'depth' => 0.0];

    /**
     * UXNode constructor.
     */
    public function __construct()
    {
    }

    /**
     * Getter and Setter for object data
     * @param string $name
     * @param mixed $value [optional]
     * @return mixed
     */
    public function data($name, $value)
    {
    }

    /**
     * @param $x
     * @param $y
     * @return array [x, y]
     */
    public function screenToLocal($x, $y)
    {
    }

    /**
     * --RU--
     * Возвращает скриншот компонента.
     * @return UXImage
     */
    public function snapshot()
    {
    }

    /**
     * --RU--
     * Ищет и возвращает один компонент по css селектору.
     *
     * @param string $selector
     *
     * @return UXNode
     */
    public function lookup($selector)
    {
    }

    /**
     * --RU--
     * Ищет и возвращает все компоненты по css селектору.
     *
     * @param $selector
     *
     * @return UXNode[]
     */
    public function lookupAll($selector)
    {
    }

    /**
     * @param double $width
     * @param double $height
     */
    public function resize($width, $height)
    {
    }

    /**
     * @param double $x
     * @param double $y
     */
    public function relocate($x, $y)
    {
    }

    /**
     * Send to front.
     * --RU--
     * Переместить компонент поверх объектов.
     */
    public function toFront()
    {
    }

    /**
     * Send to back.
     * --RU--
     * Переместить компонент под объекты.
     */
    public function toBack()
    {
    }

    /**
     * --RU--
     * Перевести фокус на объект.
     */
    public function requestFocus()
    {
    }

    /**
     * Скрыть объект.
     */
    public function hide()
    {
    }

    /**
     * Показать объект.
     */
    public function show()
    {
    }

    /**
     * Скырть объект если он видимый, показать если невидим.
     */
    public function toggle()
    {
    }

    /**
     * Возвращает true, если объект не находится ни накаком другом объекте.
     * @return bool
     */
    public function isFree()
    {
    }

    /**
     * Уничтожить объект.
     */
    public function free()
    {
    }

    /**
     * Start drag and drop
     */
    public function startFullDrag()
    {
    }

    /**
     * @param array $modes variants MOVE, COPY, LINK
     * @return UXDragboard
     */
    public function startDrag(array $modes)
    {
    }

    /**
     * @param string|array $name [optional]
     * @param string $value [optional]
     * @return string|void
     */
    public function css($name, $value)
    {
    }

    /**
     * If required, apply styles to this Node and its children, if any. This method does not normally need to
     * be invoked directly but may be used in conjunction with {@link Parent#layout()} to size a Node before the
     * next pulse, or if the {@link #getScene() Scene} is not in a {@link javafx.stage.Stage}.
     */
    public function applyCss()
    {
    }

    /**
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
     * Use observer() ..
     * @deprecated
     * @param string $property
     * @param callable $listener (UXNode $self, $property, $oldValue, $newValue)
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
}
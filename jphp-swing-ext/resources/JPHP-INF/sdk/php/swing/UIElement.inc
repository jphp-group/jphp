<?php
namespace php\swing;

abstract class UIElement {
    /**
     * @readOnly
     * @var string
     */
    public $uid;

    /**
     * @var string
     */
    public $group;

    /**
     * Position X
     * @var int
     */
    public $x;

    /**
     * Position Y
     * @var int
     */
    public $y;

    /**
     * Width
     * @var int
     */
    public $w;

    /**
     * Height
     * @var int
     */
    public $h;

    /**
     * Size (width and height), [int, int]
     * @var array
     */
    public $size;

    /**
     * @var array [int, int]
     */
    public $preferredSize;

    /**
     * Min Size (width and height) [int, int]
     * @var array
     */
    public $minSize;

    /**
     * @var bool
     */
    public $autosize;

    /**
     * Position (x and y), [int, int]
     * @var array
     */
    public $position;

    /**
     * Screen Position (x and y), [int, int]
     * @var array
     */
    public $screenPosition;

    /**
     * @var bool
     */
    public $visible;

    /**
     * @var bool
     */
    public $enabled;

    /**
     * @var bool
     */
    public $focusable;

    /**
     * @var string - NONE, LEFT, RIGHT, TOP, BOTTOM, CLIENT
     */
    public $align;

    /**
     * [LEFT, TOP, RIGHT, BOTTOM]
     * @var array
     */
    public $anchors;

    /**
     * @var Font
     */
    public $font;

    /**
     * @var Border
     */
    public $border;

    /**
     * @var Color
     */
    public $background;

    /**
     * @var Color
     */
    public $foreground;

    /**
     * @var string
     */
    public $tooltipText;

    /**
     * @var bool
     */
    public $doubleBuffered;

    /**
     * @var bool
     */
    public $opaque;

    /**
     * @var bool
     */
    public $ignoreRepaint;

    /**
     * @var bool
     */
    public $autoscrolls;

    /**
     * @readonly
     * @var string
     */
    public $uiClassId;

    /**
     * @var UIPopupMenu
     */
    public $popupMenu;

    /**
     * @var string
     */
    public $cursor;

    /**
     * @var array [int, int, int, int]
     */
    public $padding;

    /**
     * @var UIContainer
     */
    public $owner;

    /**
     *
     */
    public function __construct() { }

    /**
     * Get graphic canvas object
     * @return Graphics
     */
    public function getGraphics() {  }

    /**
     * Events -
     *   Mouse:
     *      click
     *      mousePress
     *      mouseRelease
     *      mouseEnter
     *      mouseExit
     *      mouseMove
     *      mouseDrag
     *
     *   Keyboard:
     *      keyDown
     *      keyUp
     *      keyPress
     *
     *   Focus:
     *      focus
     *      blur
     *
     * Add callback for event
     * @param $name - name of event
     * @param callable $callback
     * @param string $group
     */
    public function on($name, callable $callback, $group = 'general'){ }

    /**
     * Remove all event callbacks (if group == null), or only group
     * @param $name
     * @param null|string $group
     * @return bool
     */
    public function off($name, $group = NULL) { }

    /**
     * Trigger callback by event name
     * @param $name
     * @param ... $args
     */
    public function trigger($name){ }

    /**
     * @param string $name
     */
    protected function addAllowedEventType($name) { }

    /**
     * @param string $name
     * @param callable $callback
     */
    public function setAction($name, callable $callback = null) { }

    /**
     * @param string $keyString
     * @param string $actionName
     */
    public function setInputKey($keyString, $actionName) { }

    /**
     * @return bool
     */
    public function hasFocus() { }

    /**
     * Add child component
     * @param UIElement $component
     * @param null|int $index
     */
    public function add(UIElement $component, $index = null) { }

    /**
     * Determines if this component or one of its immediate
     * subcomponents contains the (x, y) location,
     * and if so, returns the containing component. This method only
     * looks one level deep.
     * @param $x
     * @param $y
     * @return UIElement
     */
    public function getComponentAt($x, $y) { }

    /**
     * Prints this component.
     * @param Graphics $canvas
     */
    public function printOne(Graphics $canvas) { }

    /**
     * Prints this component and all of its subcomponents.
     * @param Graphics $canvas
     */
    public function printAll(Graphics $canvas) { }

    /**
     * Paints this component.
     * @param Graphics $canvas
     */
    public function paintOne(Graphics $canvas) { }

    /**
     * Paints this component and all of its subcomponents.
     * @param Graphics $canvas
     */
    public function paintAll(Graphics $canvas) { }

    /**
     * Resets the UI property to a value from the current look and feel.
     */
    public function updateUI() { }

    /**
     * Invalidates this component and its ancestors.
     */
    public function invalidate() { }

    /**
     * Repaints this component
     */
    public function repaint() { }

    /**
     * Revalidates the component hierarchy up to the nearest validate root.
     */
    public function revalidate() { }

    /**
     * @param int $x
     * @param int $y
     * @param int $w
     * @param int $h
     */
    public function repaintRegion($x, $y, $w, $h) { }

    /**
     * Requests that this Component get the input focus, and that this
     * Component's top-level ancestor become the focused Window.
     */
    public function grabFocus() { }

    /**
     * Return width of str for drawText + current font
     * @param $str
     * @return int
     */
    public function getTextWidth($str) { }

    /**
     * Return height of one line text with current font
     * @return int
     */
    public function getTextHeight() { }

    /**
     * Get component by unique id
     * @param string $uid
     * @return UIElement
     */
    public static function getByUid($uid){ }
}

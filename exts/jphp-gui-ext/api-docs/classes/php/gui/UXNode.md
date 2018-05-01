# UXNode

- **class** `UXNode` (`php\gui\UXNode`)
- **package** `gui`
- **source** `php/gui/UXNode.php`

**Child Classes**

> [UXShape](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXShape.md), [UXCanvas](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md), [UXCustomNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCustomNode.md), [UXData](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXData.md), [UXImageView](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageView.md), [UXMediaView](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaView.md), [UXParent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXParent.md)

**Description**

Class UXNode

---

#### Properties

- `->`[`id`](#prop-id) : `string` - _Идентификатор._
- `->`[`style`](#prop-style) : `string` - _CSS стили компонента в одну строку._
- `->`[`parent`](#prop-parent) : [`UXParent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXParent.md) - _Родительский компонент._
- `->`[`cache`](#prop-cache) : `bool`
- `->`[`cacheHint`](#prop-cachehint) : `string` - _DEFAULT, SPEED, QUALITY, SCALE, ROTATE, SCALE_AND_ROTATE_
- `->`[`effects`](#prop-effects) : [`UXEffectPipeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffectPipeline.md) - _Эффекты компонента._
- `->`[`clip`](#prop-clip) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`scene`](#prop-scene) : [`UXScene`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScene.md) - _Сцена компонента._
- `->`[`form`](#prop-form) : [`UXForm`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXForm.md) - _Форма компонента._
- `->`[`window`](#prop-window) : [`UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.md) - _Окно компонента_
- `->`[`x`](#prop-x) : `double` - _Позиция по X._
- `->`[`y`](#prop-y) : `double` - _Позиция по Y._
- `->`[`translateX`](#prop-translatex) : `double` - _Смещение по X._
- `->`[`translateY`](#prop-translatey) : `double` - _Смещение по Y._
- `->`[`translateZ`](#prop-translatez) : `double`
- `->`[`scaleX`](#prop-scalex) : `double`
- `->`[`scaleY`](#prop-scaley) : `double`
- `->`[`scaleZ`](#prop-scalez) : `double`
- `->`[`scale`](#prop-scale) : `double[]` - _Scale [X, Y]_
- `->`[`screenX`](#prop-screenx) : `double` - _Экранная позция по X_
- `->`[`screenY`](#prop-screeny) : `double` - _Экранная позиция по Y_
- `->`[`screenPosition`](#prop-screenposition) : `double[]` - _Screen position [X, Y]_
- `->`[`width`](#prop-width) : `double` - _Ширина компонента._
- `->`[`height`](#prop-height) : `double` - _Высота компонента._
- `->`[`size`](#prop-size) : `double[]` - _Размеры (ширина, высота)_
- `->`[`position`](#prop-position) : `double[]` - _Позиция (x, y)_
- `->`[`visible`](#prop-visible) : `bool` - _Видимость_
- `->`[`managed`](#prop-managed) : `bool`
- `->`[`enabled`](#prop-enabled) : `bool` - _Доступность_
- `->`[`opacity`](#prop-opacity) : `double` - _Прозрачность от 0 до 1._
- `->`[`rotate`](#prop-rotate) : `double 0..360` - _Угол поворота (от 0 до 360)._
- `->`[`focused`](#prop-focused) : `bool` - _Сфокусированный._
- `->`[`focusTraversable`](#prop-focustraversable) : `bool` - _Доступность фокусировки через Tab._
- `->`[`classes`](#prop-classes) : `UXList of string` - _Список CSS классов._
- `->`[`classesString`](#prop-classesstring) : `string` - _Список CSS классов одной строкой через пробел._
- `->`[`userData`](#prop-userdata) : `mixed` - _Пользовательские данные._
- `->`[`mouseTransparent`](#prop-mousetransparent) : `bool` - _Игнорирование действий мышки._
- `->`[`cursor`](#prop-cursor) : `string|UXImage` - _Курсор_
- `->`[`leftAnchor`](#prop-leftanchor) : `double|null` - _Якорь._
- `->`[`rightAnchor`](#prop-rightanchor) : `double|null` - _Якорь._
- `->`[`topAnchor`](#prop-topanchor) : `double|null` - _Якорь._
- `->`[`bottomAnchor`](#prop-bottomanchor) : `double|null` - _Якорь._
- `->`[`anchors`](#prop-anchors) : `array` - _Якоря для фиксации размеров и позиции._
- `->`[`layoutBounds`](#prop-layoutbounds) : `array`
- `->`[`boundsInParent`](#prop-boundsinparent) : `array`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXNode constructor._
- `->`[`data()`](#method-data) - _Getter and Setter for object data_
- `->`[`screenToLocal()`](#method-screentolocal)
- `->`[`snapshot()`](#method-snapshot) - _Возвращает скриншот компонента._
- `->`[`lookup()`](#method-lookup) - _Ищет и возвращает один компонент по css селектору._
- `->`[`lookupAll()`](#method-lookupall) - _Ищет и возвращает все компоненты по css селектору._
- `->`[`resize()`](#method-resize)
- `->`[`relocate()`](#method-relocate)
- `->`[`toFront()`](#method-tofront) - _Send to front._
- `->`[`toBack()`](#method-toback) - _Send to back._
- `->`[`requestFocus()`](#method-requestfocus) - _Перевести фокус на объект._
- `->`[`hide()`](#method-hide) - _Скрыть объект._
- `->`[`show()`](#method-show) - _Показать объект._
- `->`[`toggle()`](#method-toggle) - _Скырть объект если он видимый, показать если невидим._
- `->`[`isFree()`](#method-isfree) - _Возвращает true, если объект не находится ни накаком другом объекте._
- `->`[`free()`](#method-free) - _Уничтожить объект._
- `->`[`startFullDrag()`](#method-startfulldrag) - _Start drag and drop_
- `->`[`startDrag()`](#method-startdrag)
- `->`[`css()`](#method-css)
- `->`[`applyCss()`](#method-applycss) - _If required, apply styles to this Node and its children, if any. This method does not normally need to_
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)
- `->`[`watch()`](#method-watch) **common.deprecated** - _Use observer() .._
- `->`[`observer()`](#method-observer)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
UXNode constructor.

---

<a name="method-data"></a>

### data()
```php
data(string $name, mixed $value): mixed
```
Getter and Setter for object data

---

<a name="method-screentolocal"></a>

### screenToLocal()
```php
screenToLocal(mixed $x, mixed $y): array
```

---

<a name="method-snapshot"></a>

### snapshot()
```php
snapshot(): UXImage
```
Возвращает скриншот компонента.

---

<a name="method-lookup"></a>

### lookup()
```php
lookup(string $selector): UXNode
```
Ищет и возвращает один компонент по css селектору.

---

<a name="method-lookupall"></a>

### lookupAll()
```php
lookupAll(mixed $selector): UXNode[]
```
Ищет и возвращает все компоненты по css селектору.

---

<a name="method-resize"></a>

### resize()
```php
resize(double $width, double $height): void
```

---

<a name="method-relocate"></a>

### relocate()
```php
relocate(double $x, double $y): void
```

---

<a name="method-tofront"></a>

### toFront()
```php
toFront(): void
```
Send to front.

---

<a name="method-toback"></a>

### toBack()
```php
toBack(): void
```
Send to back.

---

<a name="method-requestfocus"></a>

### requestFocus()
```php
requestFocus(): void
```
Перевести фокус на объект.

---

<a name="method-hide"></a>

### hide()
```php
hide(): void
```
Скрыть объект.

---

<a name="method-show"></a>

### show()
```php
show(): void
```
Показать объект.

---

<a name="method-toggle"></a>

### toggle()
```php
toggle(): void
```
Скырть объект если он видимый, показать если невидим.

---

<a name="method-isfree"></a>

### isFree()
```php
isFree(): bool
```
Возвращает true, если объект не находится ни накаком другом объекте.

---

<a name="method-free"></a>

### free()
```php
free(): void
```
Уничтожить объект.

---

<a name="method-startfulldrag"></a>

### startFullDrag()
```php
startFullDrag(): void
```
Start drag and drop

---

<a name="method-startdrag"></a>

### startDrag()
```php
startDrag(array $modes): UXDragboard
```

---

<a name="method-css"></a>

### css()
```php
css(string|array $name, string $value): string|void
```

---

<a name="method-applycss"></a>

### applyCss()
```php
applyCss(): void
```
If required, apply styles to this Node and its children, if any. This method does not normally need to
be invoked directly but may be used in conjunction with {@link Parent#layout()} to size a Node before the
next pulse, or if the {@link #getScene() Scene} is not in a {@link javafx.stage.Stage}.

---

<a name="method-on"></a>

### on()
```php
on(string $event, callable $handler, string $group): void
```

---

<a name="method-off"></a>

### off()
```php
off(string $event, string $group): void
```

---

<a name="method-trigger"></a>

### trigger()
```php
trigger(string $event, php\gui\event\UXEvent $e): void
```

---

<a name="method-watch"></a>

### watch()
```php
watch(string $property, callable $listener): void
```
Use observer() ..

---

<a name="method-observer"></a>

### observer()
```php
observer(string $property): UXValue
```
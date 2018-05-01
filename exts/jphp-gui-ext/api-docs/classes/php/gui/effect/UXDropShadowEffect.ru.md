# UXDropShadowEffect

- **класс** `UXDropShadowEffect` (`php\gui\effect\UXDropShadowEffect`) **унаследован от** [`UXEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/effect/UXDrowShadowEffect.php`

**Описание**

Class UXDropShadowEffect

---

#### Свойства

- `->`[`blurType`](#prop-blurtype) : `string ONE_PASS_BOX, TWO_PASS_BOX, THREE_PASS_BOX, GAUSSIAN`
- `->`[`color`](#prop-color) : [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.ru.md)
- `->`[`radius`](#prop-radius) : `double`
- `->`[`offsetX`](#prop-offsetx) : `float`
- `->`[`offsetY`](#prop-offsety) : `float`
- `->`[`spread`](#prop-spread) : `float`
- `->`[`width`](#prop-width) : `double`
- `->`[`height`](#prop-height) : `double`
- `->`[`size`](#prop-size) : `float[] width + height`
- *См. также в родительском классе* [UXEffect](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- См. также в родительском классе [UXEffect](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(double $radius, UXColor|string $color, double $offsetX, double $offsetY): void
```
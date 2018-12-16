# UXRadialGradient

- **класс** `UXRadialGradient` (`php\gui\paint\UXRadialGradient`) **унаследован от** [`UXPaint`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/paint/UXRadialGradient.php`

**Описание**

Class UXRadialGradient

---

#### Свойства

- `->`[`centerX`](#prop-centerx) : `double`
- `->`[`centerY`](#prop-centery) : `double`
- `->`[`focusAngle`](#prop-focusangle) : `double`
- `->`[`focusDistance`](#prop-focusdistance) : `double`
- `->`[`radius`](#prop-radius) : `double`
- `->`[`stops`](#prop-stops) : `UXStop[]`
- `->`[`proportional`](#prop-proportional) : `boolean`
- `->`[`cycleMethod`](#prop-cyclemethod) : `string`
- *См. также в родительском классе* [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md).

---

#### Статичные Методы

- `UXRadialGradient ::`[`of()`](#method-of)
- См. также в родительском классе [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXRadialGradient constructor._
- См. также в родительском классе [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)

---
# Статичные Методы

<a name="method-of"></a>

### of()
```php
UXRadialGradient::of(string $value): UXRadialGradient
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(float $focusAngle, float $focusDistance, float $centerX, float $centerY, float $radius, float $proportional, string $cycleMethod, array $stops): void
```
UXRadialGradient constructor.
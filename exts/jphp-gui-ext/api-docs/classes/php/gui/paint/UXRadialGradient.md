# UXRadialGradient

- **class** `UXRadialGradient` (`php\gui\paint\UXRadialGradient`) **extends** [`UXPaint`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)
- **package** `gui`
- **source** `php/gui/paint/UXRadialGradient.php`

**Description**

Class UXRadialGradient

---

#### Properties

- `->`[`centerX`](#prop-centerx) : `double`
- `->`[`centerY`](#prop-centery) : `double`
- `->`[`focusAngle`](#prop-focusangle) : `double`
- `->`[`focusDistance`](#prop-focusdistance) : `double`
- `->`[`radius`](#prop-radius) : `double`
- `->`[`stops`](#prop-stops) : `UXStop[]`
- `->`[`proportional`](#prop-proportional) : `boolean`
- `->`[`cycleMethod`](#prop-cyclemethod) : `string`
- *See also in the parent class* [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md).

---

#### Static Methods

- `UXRadialGradient ::`[`of()`](#method-of)
- See also in the parent class [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXRadialGradient constructor._
- See also in the parent class [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)

---
# Static Methods

<a name="method-of"></a>

### of()
```php
UXRadialGradient::of(string $value): UXRadialGradient
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(float $focusAngle, float $focusDistance, float $centerX, float $centerY, float $radius, float $proportional, string $cycleMethod, array $stops): void
```
UXRadialGradient constructor.
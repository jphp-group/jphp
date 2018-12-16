# UXLinearGradient

- **class** `UXLinearGradient` (`php\gui\paint\UXLinearGradient`) **extends** [`UXPaint`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)
- **package** `gui`
- **source** `php/gui/paint/UXLinearGradient.php`

**Description**

Class UXLinearGradient

---

#### Properties

- `->`[`endX`](#prop-endx) : `double`
- `->`[`endY`](#prop-endy) : `double`
- `->`[`startX`](#prop-startx) : `double`
- `->`[`startY`](#prop-starty) : `double`
- `->`[`stops`](#prop-stops) : `UXStop[]`
- `->`[`proportional`](#prop-proportional) : `boolean`
- `->`[`cycleMethod`](#prop-cyclemethod) : `string`
- *See also in the parent class* [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md).

---

#### Static Methods

- `UXLinearGradient ::`[`of()`](#method-of)
- See also in the parent class [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXLinearGradient constructor._
- See also in the parent class [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.md)

---
# Static Methods

<a name="method-of"></a>

### of()
```php
UXLinearGradient::of(string $value): UXLinearGradient
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(float $startX, float $startY, float $endX, float $endY, boolean $proportional, string $cycleMethod, array $stops): void
```
UXLinearGradient constructor.
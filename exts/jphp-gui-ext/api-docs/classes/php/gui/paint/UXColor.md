# UXColor

- **class** `UXColor` (`php\gui\paint\UXColor`)
- **package** `gui`
- **source** `php/gui/paint/UXColor.php`

**Description**

Class UXColor

---

#### Properties

- `->`[`red`](#prop-red) : `double` - _Уровень красного (от 0 до 1)_
- `->`[`blue`](#prop-blue) : `double` - _Уровень синего (от 0 до 1)._
- `->`[`green`](#prop-green) : `double` - _Уровень зеленого (от 0 до 1)._
- `->`[`opacity`](#prop-opacity) : `double` - _Уровень прозрачности (от 0 до 1)._
- `->`[`brightness`](#prop-brightness) : `double` - _Уровень яркости._
- `->`[`hue`](#prop-hue) : `double`
- `->`[`saturation`](#prop-saturation) : `double`
- `->`[`webValue`](#prop-webvalue) : `string` - _WEB значение цвета._

---

#### Static Methods

- `UXColor ::`[`of()`](#method-of)
- `UXColor ::`[`rgb()`](#method-rgb)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`grayscale()`](#method-grayscale)
- `->`[`invert()`](#method-invert)
- `->`[`saturate()`](#method-saturate)
- `->`[`desaturate()`](#method-desaturate)
- `->`[`interpolate()`](#method-interpolate)
- `->`[`getRGB()`](#method-getrgb)
- `->`[`getWebValue()`](#method-getwebvalue)

---
# Static Methods

<a name="method-of"></a>

### of()
```php
UXColor::of(string $colorString): UXColor
```

---

<a name="method-rgb"></a>

### rgb()
```php
UXColor::rgb(int $r, int $g, int $b, double $opacity): UXColor
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(double $r, double $g, double $b, double $opacity): void
```

---

<a name="method-grayscale"></a>

### grayscale()
```php
grayscale(): UXColor
```

---

<a name="method-invert"></a>

### invert()
```php
invert(): UXColor
```

---

<a name="method-saturate"></a>

### saturate()
```php
saturate(): UXColor
```

---

<a name="method-desaturate"></a>

### desaturate()
```php
desaturate(): UXColor
```

---

<a name="method-interpolate"></a>

### interpolate()
```php
interpolate(php\gui\paint\UXColor $color, double $t): UXColor
```

---

<a name="method-getrgb"></a>

### getRGB()
```php
getRGB(): int
```

---

<a name="method-getwebvalue"></a>

### getWebValue()
```php
getWebValue(): string
```
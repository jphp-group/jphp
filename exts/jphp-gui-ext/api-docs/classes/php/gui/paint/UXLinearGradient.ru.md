# UXLinearGradient

- **класс** `UXLinearGradient` (`php\gui\paint\UXLinearGradient`) **унаследован от** [`UXPaint`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/paint/UXLinearGradient.php`

**Описание**

Class UXLinearGradient

---

#### Свойства

- `->`[`endX`](#prop-endx) : `double`
- `->`[`endY`](#prop-endy) : `double`
- `->`[`startX`](#prop-startx) : `double`
- `->`[`startY`](#prop-starty) : `double`
- `->`[`stops`](#prop-stops) : `UXStop[]`
- `->`[`proportional`](#prop-proportional) : `boolean`
- `->`[`cycleMethod`](#prop-cyclemethod) : `string`
- *См. также в родительском классе* [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md).

---

#### Статичные Методы

- `UXLinearGradient ::`[`of()`](#method-of)
- См. также в родительском классе [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXLinearGradient constructor._
- См. также в родительском классе [UXPaint](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXPaint.ru.md)

---
# Статичные Методы

<a name="method-of"></a>

### of()
```php
UXLinearGradient::of(string $value): UXLinearGradient
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(float $startX, float $startY, float $endX, float $endY, boolean $proportional, string $cycleMethod, array $stops): void
```
UXLinearGradient constructor.
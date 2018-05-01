# UXPathAnimation

- **class** `UXPathAnimation` (`php\gui\animation\UXPathAnimation`) **extends** [`UXAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.md)
- **package** `gui`
- **source** `php/gui/animation/UXPathAnimation.php`

**Description**

Class UXPathAnimation

---

#### Properties

- `->`[`duration`](#prop-duration) : `int`
- `->`[`orientation`](#prop-orientation) : `string` - _NONE or ORTHOGONAL_TO_TANGENT

ORTHOGONAL_TO_TANGENT - The targeted node's rotation matrix is set to keep node
perpendicular to the path's tangent along the geometric path._
- *See also in the parent class* [UXAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`addMoveTo()`](#method-addmoveto)
- `->`[`addLineTo()`](#method-addlineto)
- `->`[`clearPath()`](#method-clearpath)
- See also in the parent class [UXAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $duration, php\gui\animation\UXNode $node): void
```

---

<a name="method-addmoveto"></a>

### addMoveTo()
```php
addMoveTo(double $x, double $y): $this
```

---

<a name="method-addlineto"></a>

### addLineTo()
```php
addLineTo(double $x, double $y): $this
```

---

<a name="method-clearpath"></a>

### clearPath()
```php
clearPath(): void
```
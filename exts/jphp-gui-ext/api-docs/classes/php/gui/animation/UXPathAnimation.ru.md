# UXPathAnimation

- **класс** `UXPathAnimation` (`php\gui\animation\UXPathAnimation`) **унаследован от** [`UXAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/animation/UXPathAnimation.php`

**Описание**

Class UXPathAnimation

---

#### Свойства

- `->`[`duration`](#prop-duration) : `int`
- `->`[`orientation`](#prop-orientation) : `string` - _NONE or ORTHOGONAL_TO_TANGENT

ORTHOGONAL_TO_TANGENT - The targeted node's rotation matrix is set to keep node
perpendicular to the path's tangent along the geometric path._
- *См. также в родительском классе* [UXAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`addMoveTo()`](#method-addmoveto)
- `->`[`addLineTo()`](#method-addlineto)
- `->`[`clearPath()`](#method-clearpath)
- См. также в родительском классе [UXAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.ru.md)

---
# Методы

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
# UXHBox

- **class** `UXHBox` (`php\gui\layout\UXHBox`) **extends** [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)
- **package** `gui`
- **source** `php/gui/layout/UXHBox.php`

**Description**

Class UXHBox

---

#### Properties

- `->`[`alignment`](#prop-alignment) : `string` - _TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, ... CENTER, ... BOTTOM_RIGHT,
BASELINE_LEFT, BASELINE_CENTER, BASELINE_RIGHT_
- `->`[`spacing`](#prop-spacing) : `float`
- `->`[`fillHeight`](#prop-fillheight) : `bool`
- *See also in the parent class* [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md).

---

#### Static Methods

- `UXHBox ::`[`setHgrow()`](#method-sethgrow)
- `UXHBox ::`[`getHgrow()`](#method-gethgrow)
- `UXHBox ::`[`setMargin()`](#method-setmargin)
- See also in the parent class [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`requestLayout()`](#method-requestlayout)
- See also in the parent class [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)

---
# Static Methods

<a name="method-sethgrow"></a>

### setHgrow()
```php
UXHBox::setHgrow(php\gui\UXNode $node, string $value): void
```

---

<a name="method-gethgrow"></a>

### getHgrow()
```php
UXHBox::getHgrow(php\gui\UXNode $node): void
```

---

<a name="method-setmargin"></a>

### setMargin()
```php
UXHBox::setMargin(php\gui\UXNode $node, double[]|double $margins): void
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(UXNode[] $nodes, int $spacing): void
```

---

<a name="method-requestlayout"></a>

### requestLayout()
```php
requestLayout(): void
```
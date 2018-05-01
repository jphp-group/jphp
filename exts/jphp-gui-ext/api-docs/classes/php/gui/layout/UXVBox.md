# UXVBox

- **class** `UXVBox` (`php\gui\layout\UXVBox`) **extends** [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)
- **package** `gui`
- **source** `php/gui/layout/UXVBox.php`

**Child Classes**

> [UXFragmentPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFragmentPane.md), [UXRadioGroupPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXRadioGroupPane.md)

**Description**

Class UXVBox

---

#### Properties

- `->`[`alignment`](#prop-alignment) : `string` - _TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, ... CENTER, ... BOTTOM_RIGHT,
BASELINE_LEFT, BASELINE_CENTER, BASELINE_RIGHT_
- `->`[`spacing`](#prop-spacing) : `float`
- `->`[`fillWidth`](#prop-fillwidth) : `bool`
- *See also in the parent class* [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md).

---

#### Static Methods

- `UXVBox ::`[`setVgrow()`](#method-setvgrow)
- `UXVBox ::`[`getVgrow()`](#method-getvgrow)
- `UXVBox ::`[`setMargin()`](#method-setmargin)
- See also in the parent class [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`requestLayout()`](#method-requestlayout)
- See also in the parent class [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)

---
# Static Methods

<a name="method-setvgrow"></a>

### setVgrow()
```php
UXVBox::setVgrow(php\gui\UXNode $node, string $value): void
```

---

<a name="method-getvgrow"></a>

### getVgrow()
```php
UXVBox::getVgrow(php\gui\UXNode $node): string
```

---

<a name="method-setmargin"></a>

### setMargin()
```php
UXVBox::setMargin(php\gui\UXNode $node, double[]|double $margins): void
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(UXNode[] $nodes, float|int $spacing): void
```

---

<a name="method-requestlayout"></a>

### requestLayout()
```php
requestLayout(): void
```
# UXSplitPane

- **class** `UXSplitPane` (`php\gui\UXSplitPane`) **extends** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)
- **package** `gui`
- **source** `php/gui/UXSplitPane.php`

**Description**

Class UXSplitPane

---

#### Properties

- `->`[`items`](#prop-items) : `UXList list of UXNode`
- `->`[`orientation`](#prop-orientation) : `string`
- `->`[`dividerPositions`](#prop-dividerpositions) : `double[]`
- *See also in the parent class* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md).

---

#### Static Methods

- `UXSplitPane ::`[`setResizeWithParent()`](#method-setresizewithparent)
- See also in the parent class [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`setDividerPosition()`](#method-setdividerposition)
- See also in the parent class [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)

---
# Static Methods

<a name="method-setresizewithparent"></a>

### setResizeWithParent()
```php
UXSplitPane::setResizeWithParent(php\gui\UXNode $node, bool $value): void
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(UXNode[] $items): void
```

---

<a name="method-setdividerposition"></a>

### setDividerPosition()
```php
setDividerPosition(int $index, double $position): void
```
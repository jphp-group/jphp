# UXScrollPane

- **class** `UXScrollPane` (`php\gui\layout\UXScrollPane`) **extends** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)
- **package** `gui`
- **source** `php/gui/layout/UXScrollPane.php`

**Description**

Class UXScrollPane

---

#### Properties

- `->`[`content`](#prop-content) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`scrollX`](#prop-scrollx) : `double`
- `->`[`scrollY`](#prop-scrolly) : `double`
- `->`[`viewportBounds`](#prop-viewportbounds) : `array`
- `->`[`scrollMinX`](#prop-scrollminx) : `double`
- `->`[`scrollMinY`](#prop-scrollminy) : `double`
- `->`[`scrollMaxX`](#prop-scrollmaxx) : `float`
- `->`[`scrollMaxY`](#prop-scrollmaxy) : `float`
- `->`[`fitToWidth`](#prop-fittowidth) : `bool`
- `->`[`fitToHeight`](#prop-fittoheight) : `bool`
- `->`[`vbarPolicy`](#prop-vbarpolicy) : `string AS_NEEDED, ALWAYS, NEVER`
- `->`[`hbarPolicy`](#prop-hbarpolicy) : `string AS_NEEDED, ALWAYS, NEVER`
- *See also in the parent class* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`scrollToNode()`](#method-scrolltonode)
- See also in the parent class [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXNode $node): void
```

---

<a name="method-scrolltonode"></a>

### scrollToNode()
```php
scrollToNode(php\gui\UXNode $node): void
```
# UXScrollPane

- **класс** `UXScrollPane` (`php\gui\layout\UXScrollPane`) **унаследован от** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/layout/UXScrollPane.php`

**Описание**

Class UXScrollPane

---

#### Свойства

- `->`[`content`](#prop-content) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
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
- *См. также в родительском классе* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`scrollToNode()`](#method-scrolltonode)
- См. также в родительском классе [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)

---
# Методы

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
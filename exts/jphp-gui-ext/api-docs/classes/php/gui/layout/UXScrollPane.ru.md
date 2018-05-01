# UXScrollPane

- **класс** `UXScrollPane` (`php\gui\layout\UXScrollPane`) **унаследован от** `UXControl` (`php\gui\UXControl`)
- **пакет** `gui`
- **исходники** `php/gui/layout/UXScrollPane.php`

**Описание**

Class UXScrollPane

---

#### Свойства

- `->`[`content`](#prop-content) : `UXNode`
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

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`scrollToNode()`](#method-scrolltonode)

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
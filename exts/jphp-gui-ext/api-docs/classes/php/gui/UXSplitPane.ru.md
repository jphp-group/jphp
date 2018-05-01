# UXSplitPane

- **класс** `UXSplitPane` (`php\gui\UXSplitPane`) **унаследован от** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXSplitPane.php`

**Описание**

Class UXSplitPane

---

#### Свойства

- `->`[`items`](#prop-items) : `UXList list of UXNode`
- `->`[`orientation`](#prop-orientation) : `string`
- `->`[`dividerPositions`](#prop-dividerpositions) : `double[]`
- *См. также в родительском классе* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md).

---

#### Статичные Методы

- `UXSplitPane ::`[`setResizeWithParent()`](#method-setresizewithparent)
- См. также в родительском классе [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`setDividerPosition()`](#method-setdividerposition)
- См. также в родительском классе [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)

---
# Статичные Методы

<a name="method-setresizewithparent"></a>

### setResizeWithParent()
```php
UXSplitPane::setResizeWithParent(php\gui\UXNode $node, bool $value): void
```

---
# Методы

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
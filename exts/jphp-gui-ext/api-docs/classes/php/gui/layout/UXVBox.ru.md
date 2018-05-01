# UXVBox

- **класс** `UXVBox` (`php\gui\layout\UXVBox`) **унаследован от** [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/layout/UXVBox.php`

**Классы наследники**

> [UXFragmentPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFragmentPane.ru.md), [UXRadioGroupPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXRadioGroupPane.ru.md)

**Описание**

Class UXVBox

---

#### Свойства

- `->`[`alignment`](#prop-alignment) : `string` - _TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, ... CENTER, ... BOTTOM_RIGHT,
BASELINE_LEFT, BASELINE_CENTER, BASELINE_RIGHT_
- `->`[`spacing`](#prop-spacing) : `float`
- `->`[`fillWidth`](#prop-fillwidth) : `bool`
- *См. также в родительском классе* [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md).

---

#### Статичные Методы

- `UXVBox ::`[`setVgrow()`](#method-setvgrow)
- `UXVBox ::`[`getVgrow()`](#method-getvgrow)
- `UXVBox ::`[`setMargin()`](#method-setmargin)
- См. также в родительском классе [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`requestLayout()`](#method-requestlayout)
- См. также в родительском классе [UXPane](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)

---
# Статичные Методы

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
# Методы

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
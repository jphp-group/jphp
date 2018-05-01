# UXCell

- **класс** `UXCell` (`php\gui\UXCell`) **унаследован от** [`UXLabeled`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXCell.php`

**Классы наследники**

> [UXListCell](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListCell.ru.md), [UXTableCell](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableCell.ru.md)

**Описание**

Class UXCell

---

#### Свойства

- `->`[`item`](#prop-item) : `mixed`
- `->`[`editable`](#prop-editable) : `bool`
- `->`[`editing`](#prop-editing) : `bool`
- `->`[`empty`](#prop-empty) : `bool`
- `->`[`selected`](#prop-selected) : `bool`
- *См. также в родительском классе* [UXLabeled](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.ru.md).

---

#### Методы

- `->`[`updateSelected()`](#method-updateselected)
- `->`[`startEdit()`](#method-startedit)
- `->`[`cancelEdit()`](#method-canceledit)
- `->`[`commitEdit()`](#method-commitedit)
- См. также в родительском классе [UXLabeled](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.ru.md)

---
# Методы

<a name="method-updateselected"></a>

### updateSelected()
```php
updateSelected(bool $value): void
```

---

<a name="method-startedit"></a>

### startEdit()
```php
startEdit(): void
```

---

<a name="method-canceledit"></a>

### cancelEdit()
```php
cancelEdit(): void
```

---

<a name="method-commitedit"></a>

### commitEdit()
```php
commitEdit(mixed $value): void
```
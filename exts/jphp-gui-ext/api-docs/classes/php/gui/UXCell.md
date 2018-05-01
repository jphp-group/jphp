# UXCell

- **class** `UXCell` (`php\gui\UXCell`) **extends** [`UXLabeled`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.md)
- **package** `gui`
- **source** `php/gui/UXCell.php`

**Child Classes**

> [UXListCell](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListCell.md), [UXTableCell](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableCell.md)

**Description**

Class UXCell

---

#### Properties

- `->`[`item`](#prop-item) : `mixed`
- `->`[`editable`](#prop-editable) : `bool`
- `->`[`editing`](#prop-editing) : `bool`
- `->`[`empty`](#prop-empty) : `bool`
- `->`[`selected`](#prop-selected) : `bool`
- *See also in the parent class* [UXLabeled](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.md).

---

#### Methods

- `->`[`updateSelected()`](#method-updateselected)
- `->`[`startEdit()`](#method-startedit)
- `->`[`cancelEdit()`](#method-canceledit)
- `->`[`commitEdit()`](#method-commitedit)
- See also in the parent class [UXLabeled](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.md)

---
# Methods

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
# UXAlert

- **class** `UXAlert` (`php\gui\UXAlert`)
- **package** `gui`
- **source** `php/gui/UXAlert.php`

**Description**

Class UXAlert

---

#### Properties

- `->`[`contentText`](#prop-contenttext) : `string`
- `->`[`headerText`](#prop-headertext) : `string`
- `->`[`title`](#prop-title) : `string`
- `->`[`expanded`](#prop-expanded) : `bool`
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`expandableContent`](#prop-expandablecontent) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`setButtonTypes()`](#method-setbuttontypes)
- `->`[`show()`](#method-show) - _Показать диалог._
- `->`[`hide()`](#method-hide) - _Скрыть диалог._
- `->`[`showAndWait()`](#method-showandwait)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(mixed $alertType): void
```

---

<a name="method-setbuttontypes"></a>

### setButtonTypes()
```php
setButtonTypes(array $buttons): void
```

---

<a name="method-show"></a>

### show()
```php
show(): void
```
Показать диалог.

---

<a name="method-hide"></a>

### hide()
```php
hide(): void
```
Скрыть диалог.

---

<a name="method-showandwait"></a>

### showAndWait()
```php
showAndWait(): mixed
```
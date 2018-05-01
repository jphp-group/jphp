# UXAlert

- **класс** `UXAlert` (`php\gui\UXAlert`)
- **пакет** `gui`
- **исходники** `php/gui/UXAlert.php`

**Описание**

Класс для отображения всплывающих диалогов с кнопками.

---

#### Свойства

- `->`[`contentText`](#prop-contenttext) : `string`
- `->`[`headerText`](#prop-headertext) : `string`
- `->`[`title`](#prop-title) : `string`
- `->`[`expanded`](#prop-expanded) : `bool`
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`expandableContent`](#prop-expandablecontent) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`setButtonTypes()`](#method-setbuttontypes)
- `->`[`show()`](#method-show) - _Показать диалог._
- `->`[`hide()`](#method-hide) - _Скрыть диалог._
- `->`[`showAndWait()`](#method-showandwait)

---
# Методы

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
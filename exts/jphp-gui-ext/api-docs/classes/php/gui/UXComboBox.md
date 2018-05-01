# UXComboBox

- **class** `UXComboBox` (`php\gui\UXComboBox`) **extends** `UXComboBoxBase` (`php\gui\UXComboBoxBase`)
- **package** `gui`
- **source** `php/gui/UXComboBox.php`

**Description**

Class UXComboBox

---

#### Properties

- `->`[`editor`](#prop-editor) : `UXTextField`
- `->`[`items`](#prop-items) : `UXList` - _Список._
- `->`[`itemsText`](#prop-itemstext) : `string` - _Список в виде текста._
- `->`[`selected`](#prop-selected) : `mixed` - _Выбранный элемент._
- `->`[`selectedIndex`](#prop-selectedindex) : `int` - _Выбранный индекс, начиная с 0.
-1 ничего не выбрано._
- `->`[`visibleRowCount`](#prop-visiblerowcount) : `int` - _Видимое количество пунктов._

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`onCellRender()`](#method-oncellrender)
- `->`[`onButtonRender()`](#method-onbuttonrender)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(array|Traversable $items): void
```

---

<a name="method-oncellrender"></a>

### onCellRender()
```php
onCellRender(callable|null $handler): void
```

---

<a name="method-onbuttonrender"></a>

### onButtonRender()
```php
onButtonRender(callable|null $handler): void
```
# UXComboBox

- **class** `UXComboBox` (`php\gui\UXComboBox`) **extends** [`UXComboBoxBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.md)
- **package** `gui`
- **source** `php/gui/UXComboBox.php`

**Description**

Class UXComboBox

---

#### Properties

- `->`[`editor`](#prop-editor) : [`UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.md)
- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.md) - _Список._
- `->`[`itemsText`](#prop-itemstext) : `string` - _Список в виде текста._
- `->`[`selected`](#prop-selected) : `mixed` - _Выбранный элемент._
- `->`[`selectedIndex`](#prop-selectedindex) : `int` - _Выбранный индекс, начиная с 0.
-1 ничего не выбрано._
- `->`[`visibleRowCount`](#prop-visiblerowcount) : `int` - _Видимое количество пунктов._
- *See also in the parent class* [UXComboBoxBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`onCellRender()`](#method-oncellrender)
- `->`[`onButtonRender()`](#method-onbuttonrender)
- See also in the parent class [UXComboBoxBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.md)

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
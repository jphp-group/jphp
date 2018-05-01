# UXComboBox

- **класс** `UXComboBox` (`php\gui\UXComboBox`) **унаследован от** [`UXComboBoxBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXComboBox.php`

**Описание**

Class UXComboBox

---

#### Свойства

- `->`[`editor`](#prop-editor) : [`UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.ru.md)
- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md) - _Список._
- `->`[`itemsText`](#prop-itemstext) : `string` - _Список в виде текста._
- `->`[`selected`](#prop-selected) : `mixed` - _Выбранный элемент._
- `->`[`selectedIndex`](#prop-selectedindex) : `int` - _Выбранный индекс, начиная с 0.
-1 ничего не выбрано._
- `->`[`visibleRowCount`](#prop-visiblerowcount) : `int` - _Видимое количество пунктов._
- *См. также в родительском классе* [UXComboBoxBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`onCellRender()`](#method-oncellrender)
- `->`[`onButtonRender()`](#method-onbuttonrender)
- См. также в родительском классе [UXComboBoxBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.ru.md)

---
# Методы

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
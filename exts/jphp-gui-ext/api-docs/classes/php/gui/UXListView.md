# UXListView

- **class** `UXListView` (`php\gui\UXListView`) **extends** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)
- **package** `gui`
- **source** `php/gui/UXListView.php`

**Description**

Class UXListView

---

#### Properties

- `->`[`editable`](#prop-editable) : `bool` - _Редактируемый._
- `->`[`editingIndex`](#prop-editingindex) : `int`
- `->`[`fixedCellSize`](#prop-fixedcellsize) : `double` - _Фиксированный размер (высота) строк._
- `->`[`placeholder`](#prop-placeholder) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.md) - _Список._
- `->`[`orientation`](#prop-orientation) : `string HORIZONTAL or VERTICAL` - _Ориентация._
- `->`[`multipleSelection`](#prop-multipleselection) : `bool` - _Множественное выделение._
- `->`[`selectedIndexes`](#prop-selectedindexes) : `int[]` - _Выделенные индексы (массив), начиная с 0._
- `->`[`selectedIndex`](#prop-selectedindex) : `int` - _Выделенный индекс, начиная с 0._
- `->`[`focusedIndex`](#prop-focusedindex) : `int` - _Сфокусированный индекс._
- `->`[`selectedItems`](#prop-selecteditems) : `mixed[]` - _Выделенные элементы._
- `->`[`selectedItem`](#prop-selecteditem) : `mixed` - _Выделенный элемент._
- `->`[`focusedItem`](#prop-focuseditem) : `mixed` - _Сфокусированный элемент._
- `->`[`itemsText`](#prop-itemstext) : `string`
- *See also in the parent class* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md).

---

#### Methods

- `->`[`scrollTo()`](#method-scrollto) - _Скролить к индексу._
- `->`[`edit()`](#method-edit)
- `->`[`setCellFactory()`](#method-setcellfactory)
- `->`[`setDraggableCellFactory()`](#method-setdraggablecellfactory)
- `->`[`update()`](#method-update) - _Визуально обновить список._
- See also in the parent class [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)

---
# Methods

<a name="method-scrollto"></a>

### scrollTo()
```php
scrollTo(int $index): void
```
Скролить к индексу.

---

<a name="method-edit"></a>

### edit()
```php
edit(int $index): void
```

---

<a name="method-setcellfactory"></a>

### setCellFactory()
```php
setCellFactory(callable|null $handler): void
```

---

<a name="method-setdraggablecellfactory"></a>

### setDraggableCellFactory()
```php
setDraggableCellFactory(callable|null $handler, callable|null $dragDoneHandler): void
```

---

<a name="method-update"></a>

### update()
```php
update(): void
```
Визуально обновить список.
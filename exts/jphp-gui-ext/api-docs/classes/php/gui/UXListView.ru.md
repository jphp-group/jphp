# UXListView

- **класс** `UXListView` (`php\gui\UXListView`) **унаследован от** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXListView.php`

**Описание**

Class UXListView

---

#### Свойства

- `->`[`editable`](#prop-editable) : `bool` - _Редактируемый._
- `->`[`editingIndex`](#prop-editingindex) : `int`
- `->`[`fixedCellSize`](#prop-fixedcellsize) : `double` - _Фиксированный размер (высота) строк._
- `->`[`placeholder`](#prop-placeholder) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md) - _Список._
- `->`[`orientation`](#prop-orientation) : `string HORIZONTAL or VERTICAL` - _Ориентация._
- `->`[`multipleSelection`](#prop-multipleselection) : `bool` - _Множественное выделение._
- `->`[`selectedIndexes`](#prop-selectedindexes) : `int[]` - _Выделенные индексы (массив), начиная с 0._
- `->`[`selectedIndex`](#prop-selectedindex) : `int` - _Выделенный индекс, начиная с 0._
- `->`[`focusedIndex`](#prop-focusedindex) : `int` - _Сфокусированный индекс._
- `->`[`selectedItems`](#prop-selecteditems) : `mixed[]` - _Выделенные элементы._
- `->`[`selectedItem`](#prop-selecteditem) : `mixed` - _Выделенный элемент._
- `->`[`focusedItem`](#prop-focuseditem) : `mixed` - _Сфокусированный элемент._
- `->`[`itemsText`](#prop-itemstext) : `string`
- *См. также в родительском классе* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md).

---

#### Методы

- `->`[`scrollTo()`](#method-scrollto) - _Скролить к индексу._
- `->`[`edit()`](#method-edit)
- `->`[`setCellFactory()`](#method-setcellfactory)
- `->`[`setDraggableCellFactory()`](#method-setdraggablecellfactory)
- `->`[`update()`](#method-update) - _Визуально обновить список._
- См. также в родительском классе [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)

---
# Методы

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
# UXTableColumn

- **класс** `UXTableColumn` (`php\gui\UXTableColumn`)
- **пакет** `gui`
- **исходники** `php/gui/UXTableColumn.php`

**Описание**

Class UXTableColumn

---

#### Свойства

- `->`[`modelIndex`](#prop-modelindex) : `int`
- `->`[`editable`](#prop-editable) : `bool`
- `->`[`resizable`](#prop-resizable) : `bool`
- `->`[`sortable`](#prop-sortable) : `bool`
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`text`](#prop-text) : `string`
- `->`[`width`](#prop-width) : `int`
- `->`[`maxWidth`](#prop-maxwidth) : `int`
- `->`[`minWidth`](#prop-minwidth) : `int`
- `->`[`style`](#prop-style) : `string`
- `->`[`id`](#prop-id) : `string`
- `->`[`visible`](#prop-visible) : `bool`

---

#### Методы

- `->`[`sizeWidthToFit()`](#method-sizewidthtofit) - _..._
- `->`[`data()`](#method-data)
- `->`[`setCellValueFactoryForArrays()`](#method-setcellvaluefactoryforarrays) - _Set cell value factory for array value_
- `->`[`setCellValueFactory()`](#method-setcellvaluefactory)
- `->`[`setCellFactory()`](#method-setcellfactory)

---
# Методы

<a name="method-sizewidthtofit"></a>

### sizeWidthToFit()
```php
sizeWidthToFit(): void
```
...

---

<a name="method-data"></a>

### data()
```php
data(mixed $name, mixed $value): void
```

---

<a name="method-setcellvaluefactoryforarrays"></a>

### setCellValueFactoryForArrays()
```php
setCellValueFactoryForArrays(): void
```
Set cell value factory for array value

---

<a name="method-setcellvaluefactory"></a>

### setCellValueFactory()
```php
setCellValueFactory(callable|null $factory): void
```

---

<a name="method-setcellfactory"></a>

### setCellFactory()
```php
setCellFactory(callable|null $factory): void
```
# UXTableColumn

- **class** `UXTableColumn` (`php\gui\UXTableColumn`)
- **package** `gui`
- **source** `php/gui/UXTableColumn.php`

**Description**

Class UXTableColumn

---

#### Properties

- `->`[`modelIndex`](#prop-modelindex) : `int`
- `->`[`editable`](#prop-editable) : `bool`
- `->`[`resizable`](#prop-resizable) : `bool`
- `->`[`sortable`](#prop-sortable) : `bool`
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`text`](#prop-text) : `string`
- `->`[`width`](#prop-width) : `int`
- `->`[`maxWidth`](#prop-maxwidth) : `int`
- `->`[`minWidth`](#prop-minwidth) : `int`
- `->`[`style`](#prop-style) : `string`
- `->`[`id`](#prop-id) : `string`
- `->`[`visible`](#prop-visible) : `bool`

---

#### Methods

- `->`[`sizeWidthToFit()`](#method-sizewidthtofit) - _..._
- `->`[`data()`](#method-data)
- `->`[`setCellValueFactoryForArrays()`](#method-setcellvaluefactoryforarrays) - _Set cell value factory for array value_
- `->`[`setCellValueFactory()`](#method-setcellvaluefactory)
- `->`[`setCellFactory()`](#method-setcellfactory)

---
# Methods

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
# UXSpinner

- **class** `UXSpinner` (`php\gui\UXSpinner`) **extends** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)
- **package** `gui`
- **source** `php/gui/UXSpinner.php`

**Child Classes**

> [UXNumberSpinner](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNumberSpinner.md)

**Description**

Class UXSpinner

---

#### Properties

- `->`[`editable`](#prop-editable) : `bool`
- `->`[`editor`](#prop-editor) : [`UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.md)
- `->`[`value`](#prop-value) : `mixed`
- `->`[`alignment`](#prop-alignment) : `string` - _Text field alignment (pos)._
- *See also in the parent class* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md).

---

#### Methods

- `->`[`increment()`](#method-increment)
- `->`[`decrement()`](#method-decrement)
- `->`[`setValueFactory()`](#method-setvaluefactory)
- `->`[`setIntegerValueFactory()`](#method-setintegervaluefactory)
- See also in the parent class [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)

---
# Methods

<a name="method-increment"></a>

### increment()
```php
increment(int $steps): void
```

---

<a name="method-decrement"></a>

### decrement()
```php
decrement(int $steps): void
```

---

<a name="method-setvaluefactory"></a>

### setValueFactory()
```php
setValueFactory(callable|null $incrementHandler, callable|null $decrementHandler): void
```

---

<a name="method-setintegervaluefactory"></a>

### setIntegerValueFactory()
```php
setIntegerValueFactory(int $min, int $max, int $initial, int $step): void
```
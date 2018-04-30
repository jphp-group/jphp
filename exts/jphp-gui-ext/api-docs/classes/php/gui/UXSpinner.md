# UXSpinner

- **class** `UXSpinner` (`php\gui\UXSpinner`) **extends** [`UXControl`](api-docs/classes/php/gui/UXControl.md)
- **package** `gui`
- **source** [`php/gui/UXSpinner.php`](./src/main/resources/JPHP-INF/sdk/php/gui/UXSpinner.php)

**Description**

Class UXSpinner

---

#### Properties

- `->`[`editable`](#prop-editable) : `bool`
- `->`[`editor`](#prop-editor) : `UXTextField`
- `->`[`value`](#prop-value) : `mixed`
- `->`[`alignment`](#prop-alignment) : `string` - _Text field alignment (pos)._

---

#### Methods

- `->`[`increment()`](#method-increment)
- `->`[`decrement()`](#method-decrement)
- `->`[`setValueFactory()`](#method-setvaluefactory)
- `->`[`setIntegerValueFactory()`](#method-setintegervaluefactory)

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

---

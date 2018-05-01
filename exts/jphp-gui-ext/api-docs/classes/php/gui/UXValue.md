# UXValue

- **class** `UXValue` (`php\gui\UXValue`)
- **package** `gui`
- **source** `php/gui/UXValue.php`

**Description**

Class UXValue

---

#### Methods

- `->`[`getValue()`](#method-getvalue)
- `->`[`addListener()`](#method-addlistener)
- `->`[`addOnceListener()`](#method-addoncelistener) - _Add listener which call only once!_
- `->`[`removeListener()`](#method-removelistener)

---
# Methods

<a name="method-getvalue"></a>

### getValue()
```php
getValue(): mixed
```

---

<a name="method-addlistener"></a>

### addListener()
```php
addListener(callable $handle): Invoker
```

---

<a name="method-addoncelistener"></a>

### addOnceListener()
```php
addOnceListener(callable $handle): Invoker
```
Add listener which call only once!

---

<a name="method-removelistener"></a>

### removeListener()
```php
removeListener(php\lang\Invoker $invoker): bool
```
# Shared

- **class** `Shared` (`php\util\Shared`)
- **package** `std`
- **source** `php/util/Shared.php`

**Description**

Class to work with shared memory of Environments

Class Shared

---

#### Static Methods

- `Shared ::`[`value()`](#method-value) - _Get or create if does not exist and get a shared value_
- `Shared ::`[`reset()`](#method-reset) - _Removes the value by $name._
- `Shared ::`[`resetAll()`](#method-resetall) - _Removes the all shared memory values._

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-value"></a>

### value()
```php
Shared::value(string $name, callable $creator): SharedValue
```
Get or create if does not exist and get a shared value

---

<a name="method-reset"></a>

### reset()
```php
Shared::reset(String $name): SharedValue
```
Removes the value by $name.

---

<a name="method-resetall"></a>

### resetAll()
```php
Shared::resetAll(): void
```
Removes the all shared memory values.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
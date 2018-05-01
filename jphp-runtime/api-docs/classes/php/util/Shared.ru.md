# Shared

- **класс** `Shared` (`php\util\Shared`)
- **пакет** `std`
- **исходники** `php/util/Shared.php`

**Описание**

Class to work with shared memory of Environments

Class Shared

---

#### Статичные Методы

- `Shared ::`[`value()`](#method-value) - _Get or create if does not exist and get a shared value_
- `Shared ::`[`reset()`](#method-reset) - _Removes the value by $name._
- `Shared ::`[`resetAll()`](#method-resetall) - _Removes the all shared memory values._

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

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
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
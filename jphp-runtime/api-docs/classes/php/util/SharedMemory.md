# SharedMemory

- **class** `SharedMemory` (`php\util\SharedMemory`)
- **package** `std`
- **source** `php/util/SharedMemory.php`

**Child Classes**

> [SharedCollection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.md), [SharedValue](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedValue.md)

**Description**

Class SharedMemory

---

#### Methods

- `->`[`synchronize()`](#method-synchronize) - _You can use a shared value as a mutex_

---
# Methods

<a name="method-synchronize"></a>

### synchronize()
```php
synchronize(callable $callback): mixed
```
You can use a shared value as a mutex
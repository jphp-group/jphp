# SharedMemory

- **класс** `SharedMemory` (`php\util\SharedMemory`)
- **пакет** `std`
- **исходники** `php/util/SharedMemory.php`

**Классы наследники**

> [SharedCollection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md), [SharedValue](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedValue.ru.md)

**Описание**

Class SharedMemory

---

#### Методы

- `->`[`synchronize()`](#method-synchronize) - _You can use a shared value as a mutex_

---
# Методы

<a name="method-synchronize"></a>

### synchronize()
```php
synchronize(callable $callback): mixed
```
You can use a shared value as a mutex
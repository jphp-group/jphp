# SharedValue

- **класс** `SharedValue` (`php\util\SharedValue`) **унаследован от** [`SharedMemory`](api-docs/classes/php/util/SharedMemory.ru.md)
- **пакет** `std`
- **исходники** [`php/util/SharedValue.php`](./src/main/resources/JPHP-INF/sdk/php/util/SharedValue.php)

**Описание**

Class SharedValue

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`get()`](#method-get)
- `->`[`set()`](#method-set)
- `->`[`remove()`](#method-remove)
- `->`[`isEmpty()`](#method-isempty)
- `->`[`getAndSet()`](#method-getandset)
- `->`[`setAndGet()`](#method-setandget)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(mixed $value): void
```

---

<a name="method-get"></a>

### get()
```php
get(): mixed
```

---

<a name="method-set"></a>

### set()
```php
set(mixed $value, bool $override): mixed
```

---

<a name="method-remove"></a>

### remove()
```php
remove(): mixed
```

---

<a name="method-isempty"></a>

### isEmpty()
```php
isEmpty(): bool
```

---

<a name="method-getandset"></a>

### getAndSet()
```php
getAndSet(callable $updateCallback): mixed
```

---

<a name="method-setandget"></a>

### setAndGet()
```php
setAndGet(callable $updateCallback): mixed
```

---

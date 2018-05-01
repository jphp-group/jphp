# SharedMap

- **класс** `SharedMap` (`php\util\SharedMap`) **унаследован от** [`SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md)
- **пакет** `std`
- **исходники** `php/util/SharedMap.php`

**Описание**

Class SharedMap

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`get()`](#method-get)
- `->`[`getOrCreate()`](#method-getorcreate)
- `->`[`has()`](#method-has)
- `->`[`count()`](#method-count)
- `->`[`set()`](#method-set)
- `->`[`remove()`](#method-remove)
- `->`[`clear()`](#method-clear) - _Remove all items._
- `->`[`isEmpty()`](#method-isempty)
- См. также в родительском классе [SharedCollection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(array|Traversable $array): void
```

---

<a name="method-get"></a>

### get()
```php
get(string $key, mixed $default): mixed
```

---

<a name="method-getorcreate"></a>

### getOrCreate()
```php
getOrCreate(string $key, callable $createCallback): mixed
```

---

<a name="method-has"></a>

### has()
```php
has(string $key): bool
```

---

<a name="method-count"></a>

### count()
```php
count(): int
```

---

<a name="method-set"></a>

### set()
```php
set(string $key, mixed $value, bool $override): mixed
```

---

<a name="method-remove"></a>

### remove()
```php
remove(string $key): mixed
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```
Remove all items.

---

<a name="method-isempty"></a>

### isEmpty()
```php
isEmpty(): bool
```
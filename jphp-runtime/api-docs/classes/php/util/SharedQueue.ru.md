# SharedQueue

- **класс** `SharedQueue` (`php\util\SharedQueue`) **унаследован от** [`SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md)
- **пакет** `std`
- **исходники** `php/util/SharedQueue.php`

**Описание**

Class SharedQueue

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`isEmpty()`](#method-isempty)
- `->`[`count()`](#method-count)
- `->`[`clear()`](#method-clear) - _Remove all elements._
- `->`[`add()`](#method-add)
- `->`[`peek()`](#method-peek)
- `->`[`poll()`](#method-poll) - _Retrieves and removes the head of this queue._
- См. также в родительском классе [SharedCollection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(array|Traversable $array): void
```

---

<a name="method-isempty"></a>

### isEmpty()
```php
isEmpty(): bool
```

---

<a name="method-count"></a>

### count()
```php
count(): int
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```
Remove all elements.

---

<a name="method-add"></a>

### add()
```php
add(mixed $value): bool
```

---

<a name="method-peek"></a>

### peek()
```php
peek(): mixed
```

---

<a name="method-poll"></a>

### poll()
```php
poll(): mixed
```
Retrieves and removes the head of this queue.
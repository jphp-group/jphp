# SharedQueue

- **class** `SharedQueue` (`php\util\SharedQueue`) **extends** [`SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.md)
- **package** `std`
- **source** `php/util/SharedQueue.php`

**Description**

Class SharedQueue

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`isEmpty()`](#method-isempty)
- `->`[`count()`](#method-count)
- `->`[`clear()`](#method-clear) - _Remove all elements._
- `->`[`add()`](#method-add)
- `->`[`peek()`](#method-peek)
- `->`[`poll()`](#method-poll) - _Retrieves and removes the head of this queue._
- See also in the parent class [SharedCollection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.md)

---
# Methods

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
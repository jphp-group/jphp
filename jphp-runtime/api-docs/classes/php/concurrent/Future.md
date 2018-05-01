# Future

- **class** `Future` (`php\concurrent\Future`)
- **package** `std`
- **source** `php/concurrent/Future.php`

**Description**

Class Future

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`isCancelled()`](#method-iscancelled)
- `->`[`isDone()`](#method-isdone)
- `->`[`cancel()`](#method-cancel)
- `->`[`get()`](#method-get)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-iscancelled"></a>

### isCancelled()
```php
isCancelled(): bool
```

---

<a name="method-isdone"></a>

### isDone()
```php
isDone(): bool
```

---

<a name="method-cancel"></a>

### cancel()
```php
cancel(bool $mayInterruptIfRunning): bool
```

---

<a name="method-get"></a>

### get()
```php
get(null|int $timeout): mixed
```
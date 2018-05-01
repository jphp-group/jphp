# Future

- **класс** `Future` (`php\concurrent\Future`)
- **пакет** `std`
- **исходники** `php/concurrent/Future.php`

**Описание**

Class Future

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`isCancelled()`](#method-iscancelled)
- `->`[`isDone()`](#method-isdone)
- `->`[`cancel()`](#method-cancel)
- `->`[`get()`](#method-get)

---
# Методы

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
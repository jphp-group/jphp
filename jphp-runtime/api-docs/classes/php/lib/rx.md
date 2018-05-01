# rx

- **class** `rx` (`php\lib\rx`)
- **source** `php/lib/rx.php`

**Description**

Class rx

---

#### Static Methods

- `rx ::`[`observable()`](#method-observable)
- `rx ::`[`subscribe()`](#method-subscribe)
- `rx ::`[`unsubscribe()`](#method-unsubscribe)
- `rx ::`[`subscribers()`](#method-subscribers)
- `rx ::`[`unsubscribeAll()`](#method-unsubscribeall)

---
# Static Methods

<a name="method-observable"></a>

### observable()
```php
rx::observable(mixed $value): mixed
```

---

<a name="method-subscribe"></a>

### subscribe()
```php
rx::subscribe(mixed $observable, callable $callback): void
```

---

<a name="method-unsubscribe"></a>

### unsubscribe()
```php
rx::unsubscribe(mixed $observable, callable $callback): void
```

---

<a name="method-subscribers"></a>

### subscribers()
```php
rx::subscribers(mixed $observable): callable[]
```

---

<a name="method-unsubscribeall"></a>

### unsubscribeAll()
```php
rx::unsubscribeAll(mixed $observable): void
```
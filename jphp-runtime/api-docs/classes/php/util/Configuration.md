# Configuration

- **class** `Configuration` (`php\util\Configuration`)
- **package** `std`
- **source** `php/util/Configuration.php`

**Description**

Class Configuration

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`has()`](#method-has)
- `->`[`get()`](#method-get)
- `->`[`getArray()`](#method-getarray)
- `->`[`getBoolean()`](#method-getboolean)
- `->`[`getNumber()`](#method-getnumber)
- `->`[`getInteger()`](#method-getinteger)
- `->`[`set()`](#method-set)
- `->`[`put()`](#method-put)
- `->`[`clear()`](#method-clear) - _Remove all values._
- `->`[`load()`](#method-load)
- `->`[`save()`](#method-save)
- `->`[`toArray()`](#method-toarray)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|Stream $source, string $encoding): void
```

---

<a name="method-has"></a>

### has()
```php
has(string $key): bool
```

---

<a name="method-get"></a>

### get()
```php
get(string $key, null|string $def): string
```

---

<a name="method-getarray"></a>

### getArray()
```php
getArray(string $key, array $def): string[]
```

---

<a name="method-getboolean"></a>

### getBoolean()
```php
getBoolean(string $key, bool $def): bool
```

---

<a name="method-getnumber"></a>

### getNumber()
```php
getNumber(string $key, int|float $def): int|float
```

---

<a name="method-getinteger"></a>

### getInteger()
```php
getInteger(string $key, int $def): int
```

---

<a name="method-set"></a>

### set()
```php
set(string $key, string|array $value): string
```

---

<a name="method-put"></a>

### put()
```php
put(array|Traversable $values): void
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```
Remove all values.

---

<a name="method-load"></a>

### load()
```php
load(string|Stream $in, string $encoding): void
```

---

<a name="method-save"></a>

### save()
```php
save(string|Stream $out, string $encoding): void
```

---

<a name="method-toarray"></a>

### toArray()
```php
toArray(): array
```
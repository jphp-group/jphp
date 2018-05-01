# MiscStream

- **class** `MiscStream` (`php\io\MiscStream`) **extends** [`Stream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.md)
- **package** `std`
- **source** `php/io/MiscStream.php`

**Child Classes**

> [MemoryStream](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MemoryStream.md)

**Description**

Class MiscStream

---

#### Methods

- `->`[`read()`](#method-read)
- `->`[`readFully()`](#method-readfully)
- `->`[`write()`](#method-write)
- `->`[`eof()`](#method-eof)
- `->`[`seek()`](#method-seek)
- `->`[`getPosition()`](#method-getposition)
- `->`[`close()`](#method-close)
- `->`[`length()`](#method-length)
- `->`[`flush()`](#method-flush)
- See also in the parent class [Stream](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.md)

---
# Methods

<a name="method-read"></a>

### read()
```php
read(int $length): mixed
```

---

<a name="method-readfully"></a>

### readFully()
```php
readFully(): mixed
```

---

<a name="method-write"></a>

### write()
```php
write(string $value, null|int $length): int
```

---

<a name="method-eof"></a>

### eof()
```php
eof(): bool
```

---

<a name="method-seek"></a>

### seek()
```php
seek(int $position): mixed
```

---

<a name="method-getposition"></a>

### getPosition()
```php
getPosition(): int
```

---

<a name="method-close"></a>

### close()
```php
close(): mixed
```

---

<a name="method-length"></a>

### length()
```php
length(): int
```

---

<a name="method-flush"></a>

### flush()
```php
flush(): void
```
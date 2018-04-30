# SourceTokenizer

- **class** `SourceTokenizer` (`phpx\parser\SourceTokenizer`)
- **source** [`phpx/parser/SourceTokenizer.php`](./src/main/resources/JPHP-INF/sdk/phpx/parser/SourceTokenizer.php)

**Description**

Class SourceTokenizer

---

#### Static Methods

- `SourceTokenizer ::`[`parseAll()`](#method-parseall)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`next()`](#method-next)
- `->`[`fetchAll()`](#method-fetchall)
- `->`[`close()`](#method-close)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|Stream $source, string $name, string $charset): void
```

---

<a name="method-next"></a>

### next()
```php
next(): SourceToken
```

---

<a name="method-fetchall"></a>

### fetchAll()
```php
fetchAll(): SourceToken[]
```

---

<a name="method-close"></a>

### close()
```php
close(): void
```

---

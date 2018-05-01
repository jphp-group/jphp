# SourceTokenizer

- **класс** `SourceTokenizer` (`phpx\parser\SourceTokenizer`)
- **исходники** `phpx/parser/SourceTokenizer.php`

**Описание**

Class SourceTokenizer

---

#### Статичные Методы

- `SourceTokenizer ::`[`parseAll()`](#method-parseall)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`next()`](#method-next)
- `->`[`fetchAll()`](#method-fetchall)
- `->`[`close()`](#method-close)

---
# Статичные Методы

<a name="method-parseall"></a>

### parseAll()
```php
SourceTokenizer::parseAll(mixed $string): SourceToken[]
```

---
# Методы

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
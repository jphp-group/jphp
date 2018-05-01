# SqlStatement

- **класс** `SqlStatement` (`php\sql\SqlStatement`)
- **исходники** `php/sql/SqlStatement.php`

**Описание**

Class SqlStatement

---

#### Методы

- `->`[`bind()`](#method-bind)
- `->`[`bindDate()`](#method-binddate)
- `->`[`bindTime()`](#method-bindtime)
- `->`[`bindTimestamp()`](#method-bindtimestamp)
- `->`[`bindBlob()`](#method-bindblob)
- `->`[`fetch()`](#method-fetch) - _Returns null if rows does not exist._
- `->`[`update()`](#method-update)
- `->`[`getLastInsertId()`](#method-getlastinsertid)
- `->`[`getGeneratedKeys()`](#method-getgeneratedkeys)
- `->`[`current()`](#method-current)
- `->`[`next()`](#method-next) - _{@inheritdoc}_
- `->`[`key()`](#method-key) - _{@inheritdoc}_
- `->`[`valid()`](#method-valid) - _{@inheritdoc}_
- `->`[`rewind()`](#method-rewind) - _{@inheritdoc}_

---
# Методы

<a name="method-bind"></a>

### bind()
```php
bind(int $index, mixed $value): void
```

---

<a name="method-binddate"></a>

### bindDate()
```php
bindDate(int $index, php\time\Time $time): void
```

---

<a name="method-bindtime"></a>

### bindTime()
```php
bindTime(int $index, php\time\Time $time): void
```

---

<a name="method-bindtimestamp"></a>

### bindTimestamp()
```php
bindTimestamp(int $index, Time|int $time): void
```

---

<a name="method-bindblob"></a>

### bindBlob()
```php
bindBlob(int $index, string|File|Stream $blob): void
```

---

<a name="method-fetch"></a>

### fetch()
```php
fetch(): SqlResult|null
```
Returns null if rows does not exist.

---

<a name="method-update"></a>

### update()
```php
update(): int
```

---

<a name="method-getlastinsertid"></a>

### getLastInsertId()
```php
getLastInsertId(): mixed
```

---

<a name="method-getgeneratedkeys"></a>

### getGeneratedKeys()
```php
getGeneratedKeys(): SqlResult
```

---

<a name="method-current"></a>

### current()
```php
current(): SqlResult
```

---

<a name="method-next"></a>

### next()
```php
next(): void
```
{@inheritdoc}

---

<a name="method-key"></a>

### key()
```php
key(): void
```
{@inheritdoc}

---

<a name="method-valid"></a>

### valid()
```php
valid(): void
```
{@inheritdoc}

---

<a name="method-rewind"></a>

### rewind()
```php
rewind(): void
```
{@inheritdoc}
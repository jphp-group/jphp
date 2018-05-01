# SqlResult

- **класс** `SqlResult` (`php\sql\SqlResult`)
- **исходники** `php/sql/SqlResult.php`

**Описание**

Class SqlResult

---

#### Методы

- `->`[`isLast()`](#method-islast)
- `->`[`isFirst()`](#method-isfirst)
- `->`[`delete()`](#method-delete) - _Deletes current row._
- `->`[`isDeleted()`](#method-isdeleted)
- `->`[`refresh()`](#method-refresh)
- `->`[`get()`](#method-get)
- `->`[`toArray()`](#method-toarray)

---
# Методы

<a name="method-islast"></a>

### isLast()
```php
isLast(): bool
```

---

<a name="method-isfirst"></a>

### isFirst()
```php
isFirst(): bool
```

---

<a name="method-delete"></a>

### delete()
```php
delete(): void
```
Deletes current row.

---

<a name="method-isdeleted"></a>

### isDeleted()
```php
isDeleted(): bool
```

---

<a name="method-refresh"></a>

### refresh()
```php
refresh(): void
```

---

<a name="method-get"></a>

### get()
```php
get(string $column): mixed|Stream|Time
```

---

<a name="method-toarray"></a>

### toArray()
```php
toArray(bool $assoc): array
```
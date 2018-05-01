# SqlConnection

- **class** `SqlConnection` (`php\sql\SqlConnection`)
- **source** `php/sql/SqlConnection.php`

**Description**

Class SqlConnection

---

#### Properties

- `->`[`autoCommit`](#prop-autocommit) : `bool` - _Disable this to use transaction mode._
- `->`[`readOnly`](#prop-readonly) : `bool`
- `->`[`transactionIsolation`](#prop-transactionisolation) : `int` - _See SqlConnection::TRANSACTION_* constants_
- `->`[`catalog`](#prop-catalog) : `string`
- `->`[`schema`](#prop-schema) : `string`

---

#### Methods

- `->`[`identifier()`](#method-identifier)
- `->`[`query()`](#method-query)
- `->`[`commit()`](#method-commit) - _Makes all changes made since the previous_
- `->`[`rollback()`](#method-rollback) - _Undoes all changes made in the current transaction_
- `->`[`close()`](#method-close)
- `->`[`getCatalogs()`](#method-getcatalogs)
- `->`[`getSchemas()`](#method-getschemas)
- `->`[`getMetaData()`](#method-getmetadata)

---
# Methods

<a name="method-identifier"></a>

### identifier()
```php
identifier(string $name): string
```

---

<a name="method-query"></a>

### query()
```php
query(string $sql, array $arguments): SqlStatement
```

---

<a name="method-commit"></a>

### commit()
```php
commit(): void
```
Makes all changes made since the previous
commit/rollback permanent and releases any database locks
currently held by this Connection object.

---

<a name="method-rollback"></a>

### rollback()
```php
rollback(): void
```
Undoes all changes made in the current transaction
and releases any database locks currently held
by this Connection object.

---

<a name="method-close"></a>

### close()
```php
close(): void
```

---

<a name="method-getcatalogs"></a>

### getCatalogs()
```php
getCatalogs(): array
```

---

<a name="method-getschemas"></a>

### getSchemas()
```php
getSchemas(): array
```

---

<a name="method-getmetadata"></a>

### getMetaData()
```php
getMetaData(): array
```
# SqlDriverManager

- **класс** `SqlDriverManager` (`php\sql\SqlDriverManager`)
- **исходники** `php/sql/SqlDriverManager.php`

**Описание**

Class DriverManager

---

#### Статичные Методы

- `SqlDriverManager ::`[`install()`](#method-install)
- `SqlDriverManager ::`[`getConnection()`](#method-getconnection)
- `SqlDriverManager ::`[`getPool()`](#method-getpool)

---
# Статичные Методы

<a name="method-install"></a>

### install()
```php
SqlDriverManager::install(string $driverName): void
```

---

<a name="method-getconnection"></a>

### getConnection()
```php
SqlDriverManager::getConnection(string $url, array $options): SqlConnection
```

---

<a name="method-getpool"></a>

### getPool()
```php
SqlDriverManager::getPool(string $url, string $driverName, array $options): SqlConnectionPool
```
# SqlConnectionPool

- **class** `SqlConnectionPool` (`php\sql\SqlConnectionPool`)
- **source** `php/sql/SqlConnectionPool.php`

**Description**

Class SqlConnectionPool

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`getConnection()`](#method-getconnection)
- `->`[`setUser()`](#method-setuser)
- `->`[`setPassword()`](#method-setpassword)
- `->`[`setMaxPoolSize()`](#method-setmaxpoolsize)
- `->`[`setIdleTimeout()`](#method-setidletimeout)
- `->`[`setMaxLifetime()`](#method-setmaxlifetime)
- `->`[`setMinimumIdle()`](#method-setminimumidle)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\sql\SqlConnectionPool $parent): void
```

---

<a name="method-getconnection"></a>

### getConnection()
```php
getConnection(): SqlConnection
```

---

<a name="method-setuser"></a>

### setUser()
```php
setUser(string $username): $this
```

---

<a name="method-setpassword"></a>

### setPassword()
```php
setPassword(string $password): $this
```

---

<a name="method-setmaxpoolsize"></a>

### setMaxPoolSize()
```php
setMaxPoolSize(int $value): $this
```

---

<a name="method-setidletimeout"></a>

### setIdleTimeout()
```php
setIdleTimeout(int $millis): $this
```

---

<a name="method-setmaxlifetime"></a>

### setMaxLifetime()
```php
setMaxLifetime(int $millis): $this
```

---

<a name="method-setminimumidle"></a>

### setMinimumIdle()
```php
setMinimumIdle(int $millis): $this
```
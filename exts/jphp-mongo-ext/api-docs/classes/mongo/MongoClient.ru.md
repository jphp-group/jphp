# MongoClient

- **класс** `MongoClient` (`mongo\MongoClient`)
- **исходники** `mongo/MongoClient.php`

**Описание**

Class MongoClient

---

#### Статичные Методы

- `MongoClient ::`[`createFromURI()`](#method-createfromuri)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _MongoClient constructor._
- `->`[`database()`](#method-database)
- `->`[`close()`](#method-close)

---
# Статичные Методы

<a name="method-createfromuri"></a>

### createFromURI()
```php
MongoClient::createFromURI(string $uri): mongo\MongoClient
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $host, int $port): void
```
MongoClient constructor.

---

<a name="method-database"></a>

### database()
```php
database(string $name): mongo\MongoDatabase
```

---

<a name="method-close"></a>

### close()
```php
close(): void
```
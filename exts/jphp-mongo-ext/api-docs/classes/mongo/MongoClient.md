# MongoClient

- **class** `MongoClient` (`mongo\MongoClient`)
- **source** `mongo/MongoClient.php`

**Description**

Class MongoClient

---

#### Static Methods

- `MongoClient ::`[`createFromURI()`](#method-createfromuri)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _MongoClient constructor._
- `->`[`database()`](#method-database)
- `->`[`databases()`](#method-databases) - _Get All Databases of Mongo Server._
- `->`[`close()`](#method-close)

---
# Static Methods

<a name="method-createfromuri"></a>

### createFromURI()
```php
MongoClient::createFromURI(string $uri): mongo\MongoClient
```

---
# Methods

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

<a name="method-databases"></a>

### databases()
```php
databases(): mongo\MongoIterable
```
Get All Databases of Mongo Server.

---

<a name="method-close"></a>

### close()
```php
close(): void
```
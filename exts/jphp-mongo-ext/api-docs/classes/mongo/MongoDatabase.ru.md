# MongoDatabase

- **класс** `MongoDatabase` (`mongo\MongoDatabase`)
- **исходники** `mongo/MongoDatabase.php`

**Описание**

Class MongoDatabase

---

#### Свойства

- `->`[`name`](#prop-name) : `string`

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`drop()`](#method-drop) - _Drops this database._
- `->`[`collection()`](#method-collection)
- `->`[`collections()`](#method-collections) - _Finds all the collections in this database._
- `->`[`createCollection()`](#method-createcollection) - _Create a new collection with the given name._
- `->`[`runCommand()`](#method-runcommand) - _Executes the given command in the context of the current database with a read preference of ReadPreference#primary()._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-drop"></a>

### drop()
```php
drop(): void
```
Drops this database.

---

<a name="method-collection"></a>

### collection()
```php
collection(string $name): mongo\MongoCollection
```

---

<a name="method-collections"></a>

### collections()
```php
collections(): mongo\MongoIterable
```
Finds all the collections in this database.

---

<a name="method-createcollection"></a>

### createCollection()
```php
createCollection(string $name): void
```
Create a new collection with the given name.

---

<a name="method-runcommand"></a>

### runCommand()
```php
runCommand(array $command): array
```
Executes the given command in the context of the current database with a read preference of ReadPreference#primary().
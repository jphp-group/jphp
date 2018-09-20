# MongoCollection

- **класс** `MongoCollection` (`mongo\MongoCollection`)
- **исходники** `mongo/MongoCollection.php`

**Описание**

Class MongoCollection

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`createIndex()`](#method-createindex)
- `->`[`dropIndex()`](#method-dropindex)
- `->`[`dropIndexByName()`](#method-dropindexbyname)
- `->`[`dropIndexes()`](#method-dropindexes)
- `->`[`deleteOne()`](#method-deleteone)
- `->`[`deleteMany()`](#method-deletemany)
- `->`[`count()`](#method-count)
- `->`[`find()`](#method-find)
- `->`[`findOneAndDelete()`](#method-findoneanddelete)
- `->`[`findOneAndUpdate()`](#method-findoneandupdate)
- `->`[`insertOne()`](#method-insertone)
- `->`[`insertMany()`](#method-insertmany)
- `->`[`updateOne()`](#method-updateone)
- `->`[`updateMany()`](#method-updatemany)
- `->`[`drop()`](#method-drop) - _Drop collection._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-createindex"></a>

### createIndex()
```php
createIndex(array $keys, array $options): void
```

---

<a name="method-dropindex"></a>

### dropIndex()
```php
dropIndex(array $keys): void
```

---

<a name="method-dropindexbyname"></a>

### dropIndexByName()
```php
dropIndexByName(string $name): void
```

---

<a name="method-dropindexes"></a>

### dropIndexes()
```php
dropIndexes(): void
```

---

<a name="method-deleteone"></a>

### deleteOne()
```php
deleteOne(array $filter): bool
```

---

<a name="method-deletemany"></a>

### deleteMany()
```php
deleteMany(array $filter): int
```

---

<a name="method-count"></a>

### count()
```php
count(array $filter, array $options): int
```

---

<a name="method-find"></a>

### find()
```php
find(array $filter): mongo\MongoIterable
```

---

<a name="method-findoneanddelete"></a>

### findOneAndDelete()
```php
findOneAndDelete(array $filter): array|null
```

---

<a name="method-findoneandupdate"></a>

### findOneAndUpdate()
```php
findOneAndUpdate(array $filter, array $update): array|null
```

---

<a name="method-insertone"></a>

### insertOne()
```php
insertOne(array $document): array
```

---

<a name="method-insertmany"></a>

### insertMany()
```php
insertMany(array $documents): void
```

---

<a name="method-updateone"></a>

### updateOne()
```php
updateOne(array $filter, array $document): array
```

---

<a name="method-updatemany"></a>

### updateMany()
```php
updateMany(array $filter, array $documents): void
```

---

<a name="method-drop"></a>

### drop()
```php
drop(): void
```
Drop collection.
# ClassRecord

- **class** `ClassRecord` (`phpx\parser\ClassRecord`) **extends** [`AbstractSourceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.md)
- **source** [`phpx/parser/ClassRecord.php`](./src/main/resources/JPHP-INF/sdk/phpx/parser/ClassRecord.php)

**Description**

Class ClassRecord

---

#### Properties

- `->`[`namespaceRecord`](#prop-namespacerecord) : `NamespaceRecord`
- `->`[`parent`](#prop-parent) : `ClassRecord`
- `->`[`type`](#prop-type) : `string CLASS, INTERFACE, TRAIT`
- `->`[`shortParentName`](#prop-shortparentname) : `bool`
- `->`[`abstract`](#prop-abstract) : `bool`

---

#### Methods

- `->`[`addInterface()`](#method-addinterface)
- `->`[`addMethod()`](#method-addmethod)
- `->`[`addProperty()`](#method-addproperty)
- `->`[`getMethod()`](#method-getmethod)
- `->`[`getProperty()`](#method-getproperty)
- `->`[`removeMethod()`](#method-removemethod)
- `->`[`getMethods()`](#method-getmethods)
- `->`[`getProperties()`](#method-getproperties)

---
# Methods

<a name="method-addinterface"></a>

### addInterface()
```php
addInterface(phpx\parser\ClassRecord $record): void
```

---

<a name="method-addmethod"></a>

### addMethod()
```php
addMethod(phpx\parser\MethodRecord $method): void
```

---

<a name="method-addproperty"></a>

### addProperty()
```php
addProperty(phpx\parser\PropertyRecord $property): void
```

---

<a name="method-getmethod"></a>

### getMethod()
```php
getMethod(mixed $name): MethodRecord
```

---

<a name="method-getproperty"></a>

### getProperty()
```php
getProperty(string $name): phpx\parser\PropertyRecord
```

---

<a name="method-removemethod"></a>

### removeMethod()
```php
removeMethod(mixed $name): void
```

---

<a name="method-getmethods"></a>

### getMethods()
```php
getMethods(): MethodRecord[]
```

---

<a name="method-getproperties"></a>

### getProperties()
```php
getProperties(): PropertyRecord[]
```
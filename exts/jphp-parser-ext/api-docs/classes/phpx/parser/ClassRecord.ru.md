# ClassRecord

- **класс** `ClassRecord` (`phpx\parser\ClassRecord`) **унаследован от** [`AbstractSourceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.ru.md)
- **исходники** `phpx/parser/ClassRecord.php`

**Описание**

Class ClassRecord

---

#### Свойства

- `->`[`namespaceRecord`](#prop-namespacerecord) : [`NamespaceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/NamespaceRecord.ru.md)
- `->`[`parent`](#prop-parent) : [`ClassRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/ClassRecord.ru.md)
- `->`[`type`](#prop-type) : `string CLASS, INTERFACE, TRAIT`
- `->`[`shortParentName`](#prop-shortparentname) : `bool`
- `->`[`abstract`](#prop-abstract) : `bool`
- *См. также в родительском классе* [AbstractSourceRecord](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.ru.md).

---

#### Методы

- `->`[`addInterface()`](#method-addinterface)
- `->`[`addMethod()`](#method-addmethod)
- `->`[`addProperty()`](#method-addproperty)
- `->`[`getMethod()`](#method-getmethod)
- `->`[`getProperty()`](#method-getproperty)
- `->`[`removeMethod()`](#method-removemethod)
- `->`[`getMethods()`](#method-getmethods)
- `->`[`getProperties()`](#method-getproperties)
- См. также в родительском классе [AbstractSourceRecord](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.ru.md)

---
# Методы

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
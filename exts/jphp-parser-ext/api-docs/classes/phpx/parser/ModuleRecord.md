# ModuleRecord

- **class** `ModuleRecord` (`phpx\parser\ModuleRecord`) **extends** [`AbstractSourceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.md)
- **source** `phpx/parser/ModuleRecord.php`

**Description**

Class ModuleRecord

---

#### Methods

- `->`[`getNamespaces()`](#method-getnamespaces)
- `->`[`getClasses()`](#method-getclasses)
- `->`[`getFunctions()`](#method-getfunctions)
- `->`[`findClass()`](#method-findclass)
- `->`[`findMethod()`](#method-findmethod) - _Alias of findFunction()._
- `->`[`findFunction()`](#method-findfunction)
- See also in the parent class [AbstractSourceRecord](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.md)

---
# Methods

<a name="method-getnamespaces"></a>

### getNamespaces()
```php
getNamespaces(): NamespaceRecord[]
```

---

<a name="method-getclasses"></a>

### getClasses()
```php
getClasses(): ClassRecord[]
```

---

<a name="method-getfunctions"></a>

### getFunctions()
```php
getFunctions(): MethodRecord[]
```

---

<a name="method-findclass"></a>

### findClass()
```php
findClass(string $name): phpx\parser\ClassRecord
```

---

<a name="method-findmethod"></a>

### findMethod()
```php
findMethod(string $name): phpx\parser\MethodRecord
```
Alias of findFunction().

---

<a name="method-findfunction"></a>

### findFunction()
```php
findFunction(string $name): phpx\parser\MethodRecord
```
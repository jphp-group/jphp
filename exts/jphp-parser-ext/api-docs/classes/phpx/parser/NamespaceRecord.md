# NamespaceRecord

- **class** `NamespaceRecord` (`phpx\parser\NamespaceRecord`) **extends** [`AbstractSourceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.md)
- **source** `phpx/parser/NamespaceRecord.php`

**Description**

Class NamespaceRecord

---

#### Methods

- `->`[`isGlobally()`](#method-isglobally)
- `->`[`addUseImport()`](#method-adduseimport)
- `->`[`addFunction()`](#method-addfunction)
- `->`[`addClass()`](#method-addclass)
- See also in the parent class [AbstractSourceRecord](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.md)

---
# Methods

<a name="method-isglobally"></a>

### isGlobally()
```php
isGlobally(): bool
```

---

<a name="method-adduseimport"></a>

### addUseImport()
```php
addUseImport(phpx\parser\UseImportRecord $use): void
```

---

<a name="method-addfunction"></a>

### addFunction()
```php
addFunction(phpx\parser\MethodRecord $function): void
```

---

<a name="method-addclass"></a>

### addClass()
```php
addClass(phpx\parser\ClassRecord $class): void
```
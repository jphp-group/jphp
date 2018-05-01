# NamespaceRecord

- **класс** `NamespaceRecord` (`phpx\parser\NamespaceRecord`) **унаследован от** [`AbstractSourceRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.ru.md)
- **исходники** `phpx/parser/NamespaceRecord.php`

**Описание**

Class NamespaceRecord

---

#### Методы

- `->`[`isGlobally()`](#method-isglobally)
- `->`[`addUseImport()`](#method-adduseimport)
- `->`[`addFunction()`](#method-addfunction)
- `->`[`addClass()`](#method-addclass)
- См. также в родительском классе [AbstractSourceRecord](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/AbstractSourceRecord.ru.md)

---
# Методы

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
# SourceFile

- **class** `SourceFile` (`phpx\parser\SourceFile`)
- **source** `phpx/parser/SourceFile.php`

**Description**

Class SourceFile

---

#### Properties

- `->`[`moduleRecord`](#prop-modulerecord) : [`ModuleRecord`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-parser-ext/api-docs/classes/phpx/parser/ModuleRecord.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`isReadOnly()`](#method-isreadonly)
- `->`[`fetchFullName()`](#method-fetchfullname) - _Returns full name with namespace for class, func and constant name._
- `->`[`update()`](#method-update)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|Stream $path, string $uniqueId): void
```

---

<a name="method-isreadonly"></a>

### isReadOnly()
```php
isReadOnly(): bool
```

---

<a name="method-fetchfullname"></a>

### fetchFullName()
```php
fetchFullName(string $name, phpx\parser\NamespaceRecord $namespace, string $useType): string
```
Returns full name with namespace for class, func and constant name.

---

<a name="method-update"></a>

### update()
```php
update(phpx\parser\SourceManager $manager): void
```
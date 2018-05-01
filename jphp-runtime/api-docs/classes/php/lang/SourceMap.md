# SourceMap

- **class** `SourceMap` (`php\lang\SourceMap`)
- **package** `std`
- **source** `php/lang/SourceMap.php`

**Description**

Class SourceMap

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`getModuleName()`](#method-getmodulename)
- `->`[`getSourceLine()`](#method-getsourceline)
- `->`[`getCompiledLine()`](#method-getcompiledline)
- `->`[`insertLines()`](#method-insertlines)
- `->`[`addLine()`](#method-addline)
- `->`[`clear()`](#method-clear) - _Remove all lines._
- `->`[`toArray()`](#method-toarray)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $moduleName): void
```

---

<a name="method-getmodulename"></a>

### getModuleName()
```php
getModuleName(): string
```

---

<a name="method-getsourceline"></a>

### getSourceLine()
```php
getSourceLine(int $compiledLine): int
```

---

<a name="method-getcompiledline"></a>

### getCompiledLine()
```php
getCompiledLine(int $sourceLine): int
```

---

<a name="method-insertlines"></a>

### insertLines()
```php
insertLines(array $inserts, int $allCountLines): void
```

---

<a name="method-addline"></a>

### addLine()
```php
addLine(int $sourceLine, int $compiledLine): void
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```
Remove all lines.

---

<a name="method-toarray"></a>

### toArray()
```php
toArray(): array
```
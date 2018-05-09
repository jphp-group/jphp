# SemVersion

- **класс** `SemVersion` (`semver\SemVersion`)
- **исходники** `semver/SemVersion.php`

**Описание**

Class SemVersion

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _SemVersion constructor._
- `->`[`isStable()`](#method-isstable)
- `->`[`getMajorNum()`](#method-getmajornum)
- `->`[`getMinorNum()`](#method-getminornum)
- `->`[`getPatchNum()`](#method-getpatchnum)
- `->`[`getPreReleaseString()`](#method-getprereleasestring)
- `->`[`toNormal()`](#method-tonormal)
- `->`[`__toString()`](#method-__tostring)
- `->`[`incMajorNum()`](#method-incmajornum)
- `->`[`incMinorNum()`](#method-incminornum)
- `->`[`incPatchNum()`](#method-incpatchnum)
- `->`[`satisfies()`](#method-satisfies)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $value): void
```
SemVersion constructor.

---

<a name="method-isstable"></a>

### isStable()
```php
isStable(): bool
```

---

<a name="method-getmajornum"></a>

### getMajorNum()
```php
getMajorNum(): int
```

---

<a name="method-getminornum"></a>

### getMinorNum()
```php
getMinorNum(): int
```

---

<a name="method-getpatchnum"></a>

### getPatchNum()
```php
getPatchNum(): int
```

---

<a name="method-getprereleasestring"></a>

### getPreReleaseString()
```php
getPreReleaseString(): string
```

---

<a name="method-tonormal"></a>

### toNormal()
```php
toNormal(): string
```

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```

---

<a name="method-incmajornum"></a>

### incMajorNum()
```php
incMajorNum(): semver\SemVersion
```

---

<a name="method-incminornum"></a>

### incMinorNum()
```php
incMinorNum(): semver\SemVersion
```

---

<a name="method-incpatchnum"></a>

### incPatchNum()
```php
incPatchNum(): semver\SemVersion
```

---

<a name="method-satisfies"></a>

### satisfies()
```php
satisfies(string $expr): bool
```
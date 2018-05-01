# SemVersion

- **class** `SemVersion` (`semver\SemVersion`)
- **source** `semver/SemVersion.php`

**Description**

Class SemVersion

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _SemVersion constructor._
- `->`[`getMajorNum()`](#method-getmajornum)
- `->`[`getMinorNum()`](#method-getminornum)
- `->`[`getPatchNum()`](#method-getpatchnum)
- `->`[`getPreReleaseString()`](#method-getprereleasestring)
- `->`[`getBuildString()`](#method-getbuildstring)
- `->`[`toNormal()`](#method-tonormal)
- `->`[`__toString()`](#method-__tostring)
- `->`[`incMajorNum()`](#method-incmajornum)
- `->`[`incMinorNum()`](#method-incminornum)
- `->`[`incPatchNum()`](#method-incpatchnum)
- `->`[`incBuildString()`](#method-incbuildstring)
- `->`[`incPreRelease()`](#method-incprerelease)
- `->`[`satisfies()`](#method-satisfies)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $value): void
```
SemVersion constructor.

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

<a name="method-getbuildstring"></a>

### getBuildString()
```php
getBuildString(): string
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
incMajorNum(string $preRelease): semver\SemVersion
```

---

<a name="method-incminornum"></a>

### incMinorNum()
```php
incMinorNum(string $preRelease): semver\SemVersion
```

---

<a name="method-incpatchnum"></a>

### incPatchNum()
```php
incPatchNum(string $preRelease): semver\SemVersion
```

---

<a name="method-incbuildstring"></a>

### incBuildString()
```php
incBuildString(): semver\SemVersion
```

---

<a name="method-incprerelease"></a>

### incPreRelease()
```php
incPreRelease(): semver\SemVersion
```

---

<a name="method-satisfies"></a>

### satisfies()
```php
satisfies(string $expr): bool
```
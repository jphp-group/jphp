# ArchiveInput

- **class** `ArchiveInput` (`compress\ArchiveInput`)
- **package** `compress`
- **source** `compress/ArchiveInput.php`

**Child Classes**

> [TarArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/TarArchiveInput.md), [ZipArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ZipArchiveInput.md)

---

#### Methods

- `->`[`stream()`](#method-stream)
- `->`[`nextEntry()`](#method-nextentry)
- `->`[`canReadEntryData()`](#method-canreadentrydata)

---
# Methods

<a name="method-stream"></a>

### stream()
```php
stream(): php\io\Stream
```

---

<a name="method-nextentry"></a>

### nextEntry()
```php
nextEntry(): ArchiveEntry
```

---

<a name="method-canreadentrydata"></a>

### canReadEntryData()
```php
canReadEntryData(compress\ArchiveEntry $entry): bool
```
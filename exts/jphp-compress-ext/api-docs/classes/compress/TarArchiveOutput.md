# TarArchiveOutput

- **class** `TarArchiveOutput` (`compress\TarArchiveOutput`) **extends** [`ArchiveOutput`](api-docs/classes/compress/ArchiveOutput.md)
- **package** `compress`
- **source** [`compress/TarArchiveOutput.php`](./src/main/resources/JPHP-INF/sdk/compress/TarArchiveOutput.php)

**Description**

Class TarArchiveOutputStream

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`createEntry()`](#method-createentry)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(Stream|string|File $output, int $blockSize, string|null $encoding): void
```
TarArchiveOutput constructor.

---

<a name="method-createentry"></a>

### createEntry()
```php
createEntry( $file, string $entryName): TarArchiveEntry
```

---

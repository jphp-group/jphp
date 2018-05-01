# ZipArchiveOutput

- **class** `ZipArchiveOutput` (`compress\ZipArchiveOutput`) **extends** [`ArchiveOutput`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveOutput.md)
- **package** `compress`
- **source** `compress/ZipArchiveOutput.php`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`createEntry()`](#method-createentry)
- See also in the parent class [ArchiveOutput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveOutput.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(Stream|string|File $output): void
```
TarArchiveOutput constructor.

---

<a name="method-createentry"></a>

### createEntry()
```php
createEntry(mixed $file, string $entryName): ZipArchiveEntry
```
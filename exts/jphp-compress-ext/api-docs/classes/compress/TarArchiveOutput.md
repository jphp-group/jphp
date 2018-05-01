# TarArchiveOutput

- **class** `TarArchiveOutput` (`compress\TarArchiveOutput`) **extends** [`ArchiveOutput`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveOutput.md)
- **package** `compress`
- **source** `compress/TarArchiveOutput.php`

**Description**

Class TarArchiveOutputStream

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
__construct(Stream|string|File $output, int $blockSize, string|null $encoding): void
```
TarArchiveOutput constructor.

---

<a name="method-createentry"></a>

### createEntry()
```php
createEntry(mixed $file, string $entryName): TarArchiveEntry
```
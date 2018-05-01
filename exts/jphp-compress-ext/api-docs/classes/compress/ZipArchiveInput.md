# ZipArchiveInput

- **class** `ZipArchiveInput` (`compress\ZipArchiveInput`) **extends** [`ArchiveInput`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveInput.md)
- **package** `compress`
- **source** `compress/ZipArchiveInput.php`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`nextEntry()`](#method-nextentry)
- See also in the parent class [ArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveInput.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(Stream|string|File $output, string|null $encoding): void
```
TarArchiveOutput constructor.

---

<a name="method-nextentry"></a>

### nextEntry()
```php
nextEntry(): ZipArchiveEntry
```
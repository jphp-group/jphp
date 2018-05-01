# ZipArchiveInput

- **класс** `ZipArchiveInput` (`compress\ZipArchiveInput`) **унаследован от** [`ArchiveInput`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveInput.ru.md)
- **пакет** `compress`
- **исходники** `compress/ZipArchiveInput.php`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`nextEntry()`](#method-nextentry)
- См. также в родительском классе [ArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveInput.ru.md)

---
# Методы

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
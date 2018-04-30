# ZipArchiveOutput

- **класс** `ZipArchiveOutput` (`compress\ZipArchiveOutput`) **унаследован от** [`ArchiveOutput`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveOutput.ru.md)
- **пакет** `compress`
- **исходники** [`compress/ZipArchiveOutput.php`](./src/main/resources/JPHP-INF/sdk/compress/ZipArchiveOutput.php)


---

#### Методы

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`createEntry()`](#method-createentry)

---
# Методы

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
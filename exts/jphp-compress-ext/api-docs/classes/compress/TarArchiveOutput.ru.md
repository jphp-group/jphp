# TarArchiveOutput

- **класс** `TarArchiveOutput` (`compress\TarArchiveOutput`) **унаследован от** [`ArchiveOutput`](https://github.com/jphp-compiler/jphp/blob/master/jphp-compress-ext/api-docs/classes/compress/ArchiveOutput.ru.md)
- **пакет** `compress`
- **исходники** [`compress/TarArchiveOutput.php`](./src/main/resources/JPHP-INF/sdk/compress/TarArchiveOutput.php)

**Описание**

Class TarArchiveOutputStream

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _TarArchiveOutput constructor._
- `->`[`createEntry()`](#method-createentry)

---
# Методы

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
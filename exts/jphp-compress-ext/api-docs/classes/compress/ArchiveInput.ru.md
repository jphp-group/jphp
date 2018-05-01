# ArchiveInput

- **класс** `ArchiveInput` (`compress\ArchiveInput`)
- **пакет** `compress`
- **исходники** `compress/ArchiveInput.php`

**Классы наследники**

> [TarArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/TarArchiveInput.ru.md), [ZipArchiveInput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ZipArchiveInput.ru.md)

---

#### Методы

- `->`[`stream()`](#method-stream)
- `->`[`nextEntry()`](#method-nextentry)
- `->`[`canReadEntryData()`](#method-canreadentrydata)

---
# Методы

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
# ZipArchiveEntry

- **class** `ZipArchiveEntry` (`compress\ZipArchiveEntry`) **extends** [`ArchiveEntry`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.md)
- **package** `compress`
- **source** `compress/ZipArchiveEntry.php`

**Description**

Class TarArchiveEntry

---

#### Properties

- `->`[`unixMode`](#prop-unixmode) : `int`
- `->`[`comment`](#prop-comment) : `string`
- `->`[`method`](#prop-method) : `int`
- `->`[`crc`](#prop-crc) : `int`
- `->`[`time`](#prop-time) : `int`
- *See also in the parent class* [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.md).

---

#### Static Methods

- `ZipArchiveEntry ::`[`ofFile()`](#method-offile)
- See also in the parent class [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TarArchiveEntry constructor._
- See also in the parent class [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.md)

---
# Static Methods

<a name="method-offile"></a>

### ofFile()
```php
ZipArchiveEntry::ofFile(File|string $file, string $fileName): compress\ZipArchiveEntry
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $name, int $size): void
```
TarArchiveEntry constructor.
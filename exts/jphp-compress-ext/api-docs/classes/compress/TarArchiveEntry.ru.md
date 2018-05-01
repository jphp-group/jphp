# TarArchiveEntry

- **класс** `TarArchiveEntry` (`compress\TarArchiveEntry`) **унаследован от** [`ArchiveEntry`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.ru.md)
- **пакет** `compress`
- **исходники** `compress/TarArchiveEntry.php`

**Описание**

Class TarArchiveEntry

---

#### Свойства

- `->`[`userId`](#prop-userid) : `int`
- `->`[`userName`](#prop-username) : `string`
- `->`[`groupId`](#prop-groupid) : `int`
- `->`[`groupName`](#prop-groupname) : `string`
- `->`[`linkName`](#prop-linkname) : `string`
- `->`[`mode`](#prop-mode) : `int`
- `->`[`modTime`](#prop-modtime) : `int`
- `->`[`realSize`](#prop-realsize) : `int`
- `->`[`devMinor`](#prop-devminor) : `int`
- `->`[`devMajor`](#prop-devmajor) : `int`
- *См. также в родительском классе* [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.ru.md).

---

#### Статичные Методы

- `TarArchiveEntry ::`[`ofFile()`](#method-offile)
- См. также в родительском классе [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.ru.md)

---

#### Методы

- `->`[`isCheckSumOK()`](#method-ischecksumok)
- `->`[`isBlockDevice()`](#method-isblockdevice)
- `->`[`isFIFO()`](#method-isfifo)
- `->`[`isSparse()`](#method-issparse)
- `->`[`isCharacterDevice()`](#method-ischaracterdevice)
- `->`[`isLink()`](#method-islink)
- `->`[`isSymbolicLink()`](#method-issymboliclink)
- `->`[`isFile()`](#method-isfile)
- `->`[`isGlobalPaxHeader()`](#method-isglobalpaxheader)
- `->`[`isPaxHeader()`](#method-ispaxheader)
- `->`[`isGNULongNameEntry()`](#method-isgnulongnameentry)
- `->`[`isGNULongLinkEntry()`](#method-isgnulonglinkentry)
- `->`[`isStarSparse()`](#method-isstarsparse)
- `->`[`isPaxGNUSparse()`](#method-ispaxgnusparse)
- `->`[`isOldGNUSparse()`](#method-isoldgnusparse)
- `->`[`isGNUSparse()`](#method-isgnusparse)
- `->`[`isExtended()`](#method-isextended)
- `->`[`__construct()`](#method-__construct) - _TarArchiveEntry constructor._
- `->`[`addPaxHeader()`](#method-addpaxheader)
- `->`[`clearExtraPaxHeaders()`](#method-clearextrapaxheaders)
- `->`[`getExtraPaxHeader()`](#method-getextrapaxheader)
- `->`[`getExtraPaxHeaders()`](#method-getextrapaxheaders)
- См. также в родительском классе [ArchiveEntry](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ArchiveEntry.ru.md)

---
# Статичные Методы

<a name="method-offile"></a>

### ofFile()
```php
TarArchiveEntry::ofFile(File|string $file, string $fileName): compress\TarArchiveEntry
```

---
# Методы

<a name="method-ischecksumok"></a>

### isCheckSumOK()
```php
isCheckSumOK(): bool
```

---

<a name="method-isblockdevice"></a>

### isBlockDevice()
```php
isBlockDevice(): bool
```

---

<a name="method-isfifo"></a>

### isFIFO()
```php
isFIFO(): bool
```

---

<a name="method-issparse"></a>

### isSparse()
```php
isSparse(): bool
```

---

<a name="method-ischaracterdevice"></a>

### isCharacterDevice()
```php
isCharacterDevice(): bool
```

---

<a name="method-islink"></a>

### isLink()
```php
isLink(): bool
```

---

<a name="method-issymboliclink"></a>

### isSymbolicLink()
```php
isSymbolicLink(): bool
```

---

<a name="method-isfile"></a>

### isFile()
```php
isFile(): bool
```

---

<a name="method-isglobalpaxheader"></a>

### isGlobalPaxHeader()
```php
isGlobalPaxHeader(): bool
```

---

<a name="method-ispaxheader"></a>

### isPaxHeader()
```php
isPaxHeader(): bool
```

---

<a name="method-isgnulongnameentry"></a>

### isGNULongNameEntry()
```php
isGNULongNameEntry(): bool
```

---

<a name="method-isgnulonglinkentry"></a>

### isGNULongLinkEntry()
```php
isGNULongLinkEntry(): bool
```

---

<a name="method-isstarsparse"></a>

### isStarSparse()
```php
isStarSparse(): bool
```

---

<a name="method-ispaxgnusparse"></a>

### isPaxGNUSparse()
```php
isPaxGNUSparse(): bool
```

---

<a name="method-isoldgnusparse"></a>

### isOldGNUSparse()
```php
isOldGNUSparse(): bool
```

---

<a name="method-isgnusparse"></a>

### isGNUSparse()
```php
isGNUSparse(): bool
```

---

<a name="method-isextended"></a>

### isExtended()
```php
isExtended(): bool
```

---

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $name, int $size): void
```
TarArchiveEntry constructor.

---

<a name="method-addpaxheader"></a>

### addPaxHeader()
```php
addPaxHeader(string $name, string $value): void
```

---

<a name="method-clearextrapaxheaders"></a>

### clearExtraPaxHeaders()
```php
clearExtraPaxHeaders(): void
```

---

<a name="method-getextrapaxheader"></a>

### getExtraPaxHeader()
```php
getExtraPaxHeader(string $name): string
```

---

<a name="method-getextrapaxheaders"></a>

### getExtraPaxHeaders()
```php
getExtraPaxHeaders(): array
```
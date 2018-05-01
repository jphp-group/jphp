# ArchiveOutput

- **class** `ArchiveOutput` (`compress\ArchiveOutput`)
- **package** `compress`
- **source** `compress/ArchiveOutput.php`

**Child Classes**

> [TarArchiveOutput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/TarArchiveOutput.md), [ZipArchiveOutput](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/ZipArchiveOutput.md)

**Description**

Class ArchiveOutputStream

---

#### Methods

- `->`[`stream()`](#method-stream)
- `->`[`putEntry()`](#method-putentry) - _Writes the headers for an archive entry to the output stream._
- `->`[`closeEntry()`](#method-closeentry) - _Closes the archive entry, writing any trailer information that may_
- `->`[`canWriteEntryData()`](#method-canwriteentrydata) - _Whether this stream is able to write the given entry._
- `->`[`createEntry()`](#method-createentry) - _Create an archive entry using the inputFile and entryName provided._
- `->`[`finish()`](#method-finish) - _Finishes the addition of entries to this stream, without closing it._
- `->`[`getBytesWritten()`](#method-getbyteswritten) - _Returns the current number of bytes written to this stream._

---
# Methods

<a name="method-stream"></a>

### stream()
```php
stream(): php\io\Stream
```

---

<a name="method-putentry"></a>

### putEntry()
```php
putEntry(compress\ArchiveEntry $entry): void
```
Writes the headers for an archive entry to the output stream.
The caller must then write the content to the stream and call
closeArchiveEntry() to complete the process.

---

<a name="method-closeentry"></a>

### closeEntry()
```php
closeEntry(): void
```
Closes the archive entry, writing any trailer information that may
be required.

---

<a name="method-canwriteentrydata"></a>

### canWriteEntryData()
```php
canWriteEntryData(compress\ArchiveEntry $entry): bool
```
Whether this stream is able to write the given entry.

Some archive formats support variants or details that are
not supported (yet).

---

<a name="method-createentry"></a>

### createEntry()
```php
createEntry(mixed $file, string $entryName): ArchiveEntry
```
Create an archive entry using the inputFile and entryName provided.

---

<a name="method-finish"></a>

### finish()
```php
finish(): void
```
Finishes the addition of entries to this stream, without closing it.
Additional data can be written, if the format supports it.

---

<a name="method-getbyteswritten"></a>

### getBytesWritten()
```php
getBytesWritten(): int
```
Returns the current number of bytes written to this stream.
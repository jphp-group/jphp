# ZipFile

- **класс** `ZipFile` (`php\compress\ZipFile`)
- **исходники** `php/compress/ZipFile.php`

**Описание**

Class ZipFile

---

#### Статичные Методы

- `ZipFile ::`[`create()`](#method-create) - _Create ZIP Archive._

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _ZipFile constructor._
- `->`[`unpack()`](#method-unpack) - _Extract zip archive content to directory._
- `->`[`read()`](#method-read) - _Read one zip entry from archive._
- `->`[`readAll()`](#method-readall) - _Read all zip entries from archive._
- `->`[`stat()`](#method-stat) - _Returns stat of one zip entry by path._
- `->`[`statAll()`](#method-statall) - _Returns all stats of zip archive._
- `->`[`has()`](#method-has) - _Checks zip entry exist by path._
- `->`[`add()`](#method-add) - _Add stream or file to archive._
- `->`[`addDirectory()`](#method-adddirectory) - _Add all files of directory to archive._
- `->`[`addFromString()`](#method-addfromstring) - _Add zip entry from string._
- `->`[`remove()`](#method-remove) - _Remove zip entry by its path._
- `->`[`getPath()`](#method-getpath)

---
# Статичные Методы

<a name="method-create"></a>

### create()
```php
ZipFile::create(string $file, bool $rewrite): ZipFile
```
Create ZIP Archive.

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(mixed $file, bool $create): void
```
ZipFile constructor.

---

<a name="method-unpack"></a>

### unpack()
```php
unpack(string $toDirectory, string $charset, callable $callback): void
```
Extract zip archive content to directory.

---

<a name="method-read"></a>

### read()
```php
read(string $path, callable $reader): void
```
Read one zip entry from archive.

---

<a name="method-readall"></a>

### readAll()
```php
readAll(callable $reader): void
```
Read all zip entries from archive.

---

<a name="method-stat"></a>

### stat()
```php
stat(string $path): array
```
Returns stat of one zip entry by path.
[name, size, compressedSize, time, crc, comment, method, directory]

---

<a name="method-statall"></a>

### statAll()
```php
statAll(): array[]
```
Returns all stats of zip archive.

---

<a name="method-has"></a>

### has()
```php
has(mixed $path): bool
```
Checks zip entry exist by path.

---

<a name="method-add"></a>

### add()
```php
add(string $path, Stream|File|string $source, int $compressLevel): void
```
Add stream or file to archive.

---

<a name="method-adddirectory"></a>

### addDirectory()
```php
addDirectory(string $dir, int $compressLevel, callable $callback): void
```
Add all files of directory to archive.

---

<a name="method-addfromstring"></a>

### addFromString()
```php
addFromString(string $path, string $string, int $compressLevel): void
```
Add zip entry from string.

---

<a name="method-remove"></a>

### remove()
```php
remove(string|array $path): void
```
Remove zip entry by its path.

---

<a name="method-getpath"></a>

### getPath()
```php
getPath(): string
```
# fs

- **class** `fs` (`php\lib\fs`)
- **package** `std`
- **source** `php/lib/fs.php`

**Description**

File System class.

Class fs

---

#### Static Methods

- `fs ::`[`separator()`](#method-separator) - _Return the local filesystem's name-separator character._
- `fs ::`[`pathSeparator()`](#method-pathseparator) - _Return the local filesystem's path-separator character._
- `fs ::`[`valid()`](#method-valid) - _Validate file name._
- `fs ::`[`abs()`](#method-abs) - _Returns absolute real path._
- `fs ::`[`name()`](#method-name) - _Returns name of the path._
- `fs ::`[`nameNoExt()`](#method-namenoext) - _Returns name of the path without extension._
- `fs ::`[`pathNoExt()`](#method-pathnoext) - _Returns path without extension._
- `fs ::`[`relativize()`](#method-relativize)
- `fs ::`[`ext()`](#method-ext) - _Returns extension of path._
- `fs ::`[`hasExt()`](#method-hasext) - _Check that $path has an extension from the extension set._
- `fs ::`[`parent()`](#method-parent) - _Returns parent directory._
- `fs ::`[`ensureParent()`](#method-ensureparent) - _Checks parent of path and if it is not exists, tries to create parent directory._
- `fs ::`[`normalize()`](#method-normalize) - _Normalizes file path for current OS._
- `fs ::`[`exists()`](#method-exists) - _Checks that file is exists._
- `fs ::`[`size()`](#method-size) - _Returns size of file in bytes._
- `fs ::`[`isFile()`](#method-isfile) - _Checks that path is file._
- `fs ::`[`isDir()`](#method-isdir) - _Checks that path is directory._
- `fs ::`[`isHidden()`](#method-ishidden) - _Checks that path is hidden._
- `fs ::`[`time()`](#method-time) - _Returns last modification time of file or directory._
- `fs ::`[`makeDir()`](#method-makedir) - _Creates empty directory (mkdirs) if not exists._
- `fs ::`[`makeFile()`](#method-makefile) - _Creates empty file, if file already exists then rewrite it._
- `fs ::`[`delete()`](#method-delete) - _Deletes file or empty directory._
- `fs ::`[`clean()`](#method-clean) - _Deletes all files in path. This method does not delete the $path directory._
- `fs ::`[`scan()`](#method-scan) - _Scans the path with callback or array filter and can returns found list_
- `fs ::`[`crc32()`](#method-crc32) - _Calculates crc32 sum of file or stream, returns null if it's failed._
- `fs ::`[`hash()`](#method-hash) - _Calculates hash of file or stream._
- `fs ::`[`copy()`](#method-copy) - _Copies $source stream to $dest stream._
- `fs ::`[`move()`](#method-move) - _Renames or moves a file or empty dir._
- `fs ::`[`rename()`](#method-rename) - _Set name for file, returns true if success._
- `fs ::`[`get()`](#method-get) - _Reads fully data from source and returns it as binary string._
- `fs ::`[`parseAs()`](#method-parseas) - _Read fully data from source, parse as format and return result._
- `fs ::`[`parse()`](#method-parse) - _Read fully data from source, parse as format by extensions and return result._
- `fs ::`[`formatAs()`](#method-formatas) - _Write formatted data to source (path)._
- `fs ::`[`format()`](#method-format) - _Write formatted (based on path extension) data to source (path)._
- `fs ::`[`match()`](#method-match) - _Tells if given path matches this matcher's pattern._

---
# Static Methods

<a name="method-separator"></a>

### separator()
```php
fs::separator(): string
```
Return the local filesystem's name-separator character.

---

<a name="method-pathseparator"></a>

### pathSeparator()
```php
fs::pathSeparator(): string
```
Return the local filesystem's path-separator character.

---

<a name="method-valid"></a>

### valid()
```php
fs::valid(mixed $name): bool
```
Validate file name.

---

<a name="method-abs"></a>

### abs()
```php
fs::abs(mixed $path): string
```
Returns absolute real path.

---

<a name="method-name"></a>

### name()
```php
fs::name(mixed $path): string
```
Returns name of the path.

---

<a name="method-namenoext"></a>

### nameNoExt()
```php
fs::nameNoExt(mixed $path): string
```
Returns name of the path without extension.

---

<a name="method-pathnoext"></a>

### pathNoExt()
```php
fs::pathNoExt(string $path): string
```
Returns path without extension.

---

<a name="method-relativize"></a>

### relativize()
```php
fs::relativize(string $path, string $basePath): string
```

---

<a name="method-ext"></a>

### ext()
```php
fs::ext(mixed $path): string
```
Returns extension of path.

---

<a name="method-hasext"></a>

### hasExt()
```php
fs::hasExt(string $path, string|array $extensions, bool $ignoreCase): bool
```
Check that $path has an extension from the extension set.

---

<a name="method-parent"></a>

### parent()
```php
fs::parent(mixed $path): string
```
Returns parent directory.

---

<a name="method-ensureparent"></a>

### ensureParent()
```php
fs::ensureParent(string $path): bool
```
Checks parent of path and if it is not exists, tries to create parent directory.
See makeDir().

---

<a name="method-normalize"></a>

### normalize()
```php
fs::normalize(mixed $path): string
```
Normalizes file path for current OS.

---

<a name="method-exists"></a>

### exists()
```php
fs::exists(mixed $path): string
```
Checks that file is exists.

---

<a name="method-size"></a>

### size()
```php
fs::size(mixed $path): int
```
Returns size of file in bytes.

---

<a name="method-isfile"></a>

### isFile()
```php
fs::isFile(mixed $path): bool
```
Checks that path is file.

---

<a name="method-isdir"></a>

### isDir()
```php
fs::isDir(mixed $path): bool
```
Checks that path is directory.

---

<a name="method-ishidden"></a>

### isHidden()
```php
fs::isHidden(mixed $path): bool
```
Checks that path is hidden.

---

<a name="method-time"></a>

### time()
```php
fs::time(mixed $path): int
```
Returns last modification time of file or directory.

---

<a name="method-makedir"></a>

### makeDir()
```php
fs::makeDir(string $path): bool
```
Creates empty directory (mkdirs) if not exists.

---

<a name="method-makefile"></a>

### makeFile()
```php
fs::makeFile(mixed $path): bool
```
Creates empty file, if file already exists then rewrite it.

---

<a name="method-delete"></a>

### delete()
```php
fs::delete(mixed $path): bool
```
Deletes file or empty directory.

---

<a name="method-clean"></a>

### clean()
```php
fs::clean(string $path, callable|array $filter): array
```
Deletes all files in path. This method does not delete the $path directory.
Returns array with error, success and skip file list.

Array filter, e.g.:
[
namePattern => string (regex),
extensions => [...],
excludeExtensions => [...],
excludeDirs => bool,
excludeFiles => bool,
excludeHidden => bool,

minSize => int
maxSize => int
minTime => int, millis
maxTime => int, millis

callback => function (File $file, $depth) { }
]


---

<a name="method-scan"></a>

### scan()
```php
fs::scan(string $path, callable|array $filter, int $maxDepth, bool $subIsFirst): array
```
Scans the path with callback or array filter and can returns found list
if the callback returns any result or if the callback is null.

Array filter, e.g.:
[
namePattern => string (regex),
extensions => [...],
excludeExtensions => [...],
excludeDirs => bool,
excludeFiles => bool,
excludeHidden => bool,

minSize => int
maxSize => int
minTime => int, millis
maxTime => int, millis

callback => function (File $file, $depth) { }
]


---

<a name="method-crc32"></a>

### crc32()
```php
fs::crc32(string|Stream $source): int|null
```
Calculates crc32 sum of file or stream, returns null if it's failed.

---

<a name="method-hash"></a>

### hash()
```php
fs::hash(string|Stream $source, string $algo, callable $onProgress): string|null
```
Calculates hash of file or stream.

---

<a name="method-copy"></a>

### copy()
```php
fs::copy(string|File|Stream $source, string|File|Stream $dest, callable $onProgress, int $bufferSize): int
```
Copies $source stream to $dest stream.

---

<a name="method-move"></a>

### move()
```php
fs::move(string $fromPath, string $toPath): bool
```
Renames or moves a file or empty dir.

---

<a name="method-rename"></a>

### rename()
```php
fs::rename(string $pathToFile, string $newName): bool
```
Set name for file, returns true if success.

---

<a name="method-get"></a>

### get()
```php
fs::get(string $source, null|string $charset, string $mode): string
```
Reads fully data from source and returns it as binary string.

---

<a name="method-parseas"></a>

### parseAs()
```php
fs::parseAs(mixed $path, string $format, int $flags): mixed
```
Read fully data from source, parse as format and return result.

---

<a name="method-parse"></a>

### parse()
```php
fs::parse(mixed $path, int $flags): mixed
```
Read fully data from source, parse as format by extensions and return result.

---

<a name="method-formatas"></a>

### formatAs()
```php
fs::formatAs(mixed $path, mixed $value, string $format, int $flags): void
```
Write formatted data to source (path).

---

<a name="method-format"></a>

### format()
```php
fs::format(mixed $path, mixed $value, int $flags): void
```
Write formatted (based on path extension) data to source (path).

---

<a name="method-match"></a>

### match()
```php
fs::match(string $path, string $fsPattern): bool
```
Tells if given path matches this matcher's pattern.
# File

- **class** `File` (`php\io\File`)
- **package** `std`
- **source** `php/io/File.php`

**Description**

Class File

---

#### Static Methods

- `File ::`[`createTemp()`](#method-createtemp)
- `File ::`[`listRoots()`](#method-listroots) - _List the available filesystem roots._
- `File ::`[`of()`](#method-of)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`exists()`](#method-exists)
- `->`[`canExecute()`](#method-canexecute)
- `->`[`canWrite()`](#method-canwrite)
- `->`[`canRead()`](#method-canread)
- `->`[`getName()`](#method-getname)
- `->`[`getAbsolutePath()`](#method-getabsolutepath)
- `->`[`getCanonicalPath()`](#method-getcanonicalpath)
- `->`[`getParent()`](#method-getparent)
- `->`[`getPath()`](#method-getpath)
- `->`[`getAbsoluteFile()`](#method-getabsolutefile)
- `->`[`getCanonicalFile()`](#method-getcanonicalfile)
- `->`[`getParentFile()`](#method-getparentfile)
- `->`[`mkdir()`](#method-mkdir)
- `->`[`mkdirs()`](#method-mkdirs)
- `->`[`isFile()`](#method-isfile)
- `->`[`isDirectory()`](#method-isdirectory)
- `->`[`isAbsolute()`](#method-isabsolute)
- `->`[`isHidden()`](#method-ishidden)
- `->`[`matches()`](#method-matches)
- `->`[`delete()`](#method-delete)
- `->`[`deleteOnExit()`](#method-deleteonexit)
- `->`[`createNewFile()`](#method-createnewfile)
- `->`[`lastModified()`](#method-lastmodified)
- `->`[`length()`](#method-length)
- `->`[`crc32()`](#method-crc32)
- `->`[`toUrl()`](#method-tourl)
- `->`[`hash()`](#method-hash)
- `->`[`renameTo()`](#method-renameto)
- `->`[`setExecutable()`](#method-setexecutable)
- `->`[`setWritable()`](#method-setwritable)
- `->`[`setReadable()`](#method-setreadable)
- `->`[`setReadOnly()`](#method-setreadonly)
- `->`[`setLastModified()`](#method-setlastmodified)
- `->`[`compareTo()`](#method-compareto)
- `->`[`find()`](#method-find)
- `->`[`findFiles()`](#method-findfiles)
- `->`[`open()`](#method-open)
- `->`[`parseAs()`](#method-parseas)
- `->`[`formatTo()`](#method-formatto)
- `->`[`__toString()`](#method-__tostring)

---
# Static Methods

<a name="method-createtemp"></a>

### createTemp()
```php
File::createTemp(string $prefix, string $suffix, null|File|string $directory): File
```

---

<a name="method-listroots"></a>

### listRoots()
```php
File::listRoots(): File[]
```
List the available filesystem roots.
Returns an array of objects denoting the available filesystem roots,
or empty array if the set of roots could not be determined.
The array will be empty if there are no filesystem roots.

---

<a name="method-of"></a>

### of()
```php
File::of(string $path): File
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $path, null|string $child): void
```

---

<a name="method-exists"></a>

### exists()
```php
exists(): bool
```

---

<a name="method-canexecute"></a>

### canExecute()
```php
canExecute(): bool
```

---

<a name="method-canwrite"></a>

### canWrite()
```php
canWrite(): bool
```

---

<a name="method-canread"></a>

### canRead()
```php
canRead(): bool
```

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-getabsolutepath"></a>

### getAbsolutePath()
```php
getAbsolutePath(): string
```

---

<a name="method-getcanonicalpath"></a>

### getCanonicalPath()
```php
getCanonicalPath(): string
```

---

<a name="method-getparent"></a>

### getParent()
```php
getParent(): string
```

---

<a name="method-getpath"></a>

### getPath()
```php
getPath(): string
```

---

<a name="method-getabsolutefile"></a>

### getAbsoluteFile()
```php
getAbsoluteFile(): File
```

---

<a name="method-getcanonicalfile"></a>

### getCanonicalFile()
```php
getCanonicalFile(): File
```

---

<a name="method-getparentfile"></a>

### getParentFile()
```php
getParentFile(): File
```

---

<a name="method-mkdir"></a>

### mkdir()
```php
mkdir(): bool
```

---

<a name="method-mkdirs"></a>

### mkdirs()
```php
mkdirs(): bool
```

---

<a name="method-isfile"></a>

### isFile()
```php
isFile(): bool
```

---

<a name="method-isdirectory"></a>

### isDirectory()
```php
isDirectory(): bool
```

---

<a name="method-isabsolute"></a>

### isAbsolute()
```php
isAbsolute(): bool
```

---

<a name="method-ishidden"></a>

### isHidden()
```php
isHidden(): bool
```

---

<a name="method-matches"></a>

### matches()
```php
matches(string $pattern): bool
```

---

<a name="method-delete"></a>

### delete()
```php
delete(): bool
```

---

<a name="method-deleteonexit"></a>

### deleteOnExit()
```php
deleteOnExit(): void
```

---

<a name="method-createnewfile"></a>

### createNewFile()
```php
createNewFile(bool $withDirs): bool
```

---

<a name="method-lastmodified"></a>

### lastModified()
```php
lastModified(): int
```

---

<a name="method-length"></a>

### length()
```php
length(): int
```

---

<a name="method-crc32"></a>

### crc32()
```php
crc32(): int|null
```

---

<a name="method-tourl"></a>

### toUrl()
```php
toUrl(): string
```

---

<a name="method-hash"></a>

### hash()
```php
hash(string $algorithm, callable $onProgress): null|string
```

---

<a name="method-renameto"></a>

### renameTo()
```php
renameTo(string $newName): bool
```

---

<a name="method-setexecutable"></a>

### setExecutable()
```php
setExecutable(bool $value, bool $ownerOnly): bool
```

---

<a name="method-setwritable"></a>

### setWritable()
```php
setWritable(bool $value, bool $ownerOnly): bool
```

---

<a name="method-setreadable"></a>

### setReadable()
```php
setReadable(bool $value, bool $ownerOnly): bool
```

---

<a name="method-setreadonly"></a>

### setReadOnly()
```php
setReadOnly(): bool
```

---

<a name="method-setlastmodified"></a>

### setLastModified()
```php
setLastModified(int $time): bool
```

---

<a name="method-compareto"></a>

### compareTo()
```php
compareTo(string|File $file): int
```

---

<a name="method-find"></a>

### find()
```php
find(callable $filter): string[]
```

---

<a name="method-findfiles"></a>

### findFiles()
```php
findFiles(callable $filter): File[]
```

---

<a name="method-open"></a>

### open()
```php
open(string $flags): php\io\Stream
```

---

<a name="method-parseas"></a>

### parseAs()
```php
parseAs(string $format, int $flags): mixed
```

---

<a name="method-formatto"></a>

### formatTo()
```php
formatTo(mixed $value, string $format, int $flags): void
```

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```
# ZipArchive

- **класс** `ZipArchive` (`compress\ZipArchive`) **унаследован от** [`Archive`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/Archive.ru.md)
- **пакет** `compress`
- **исходники** `compress/ZipArchive.php`

---

#### Методы

- `->`[`createInput()`](#method-createinput)
- `->`[`createOutput()`](#method-createoutput)
- `->`[`entries()`](#method-entries)
- `->`[`entry()`](#method-entry)
- `->`[`readAll()`](#method-readall)
- `->`[`read()`](#method-read)
- См. также в родительском классе [Archive](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/Archive.ru.md)

---
# Методы

<a name="method-createinput"></a>

### createInput()
```php
createInput(): ArchiveInput
```

---

<a name="method-createoutput"></a>

### createOutput()
```php
createOutput(): ArchiveOutput
```

---

<a name="method-entries"></a>

### entries()
```php
entries(): ZipArchiveEntry[]
```

---

<a name="method-entry"></a>

### entry()
```php
entry(string $path): ZipArchiveEntry
```

---

<a name="method-readall"></a>

### readAll()
```php
readAll(callable $callback): ZipArchiveEntry[]
```

---

<a name="method-read"></a>

### read()
```php
read(string $path, callable $callback): ZipArchiveEntry
```
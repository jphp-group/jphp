# TarArchive

- **class** `TarArchive` (`compress\TarArchive`) **extends** [`Archive`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/Archive.md)
- **package** `compress`
- **source** `compress/TarArchive.php`

**Description**

Class TarArchive

---

#### Methods

- `->`[`createInput()`](#method-createinput)
- `->`[`createOutput()`](#method-createoutput)
- `->`[`entries()`](#method-entries)
- `->`[`entry()`](#method-entry)
- `->`[`readAll()`](#method-readall)
- `->`[`read()`](#method-read)
- See also in the parent class [Archive](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-compress-ext/api-docs/classes/compress/Archive.md)

---
# Methods

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
entries(): TarArchiveEntry[]
```

---

<a name="method-entry"></a>

### entry()
```php
entry(string $path): TarArchiveEntry
```

---

<a name="method-readall"></a>

### readAll()
```php
readAll(callable $callback): TarArchiveEntry[]
```

---

<a name="method-read"></a>

### read()
```php
read(string $path, callable $callback): TarArchiveEntry
```
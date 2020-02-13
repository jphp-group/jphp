# fs

- **класс** `fs` (`php\lib\fs`)
- **пакет** `std`
- **исходники** `php/lib/fs.php`

**Описание**

File System class.

Class fs

---

#### Статичные Методы

- `fs ::`[`separator()`](#method-separator) - _Возвращает символ разделитель для имен файлов на текущей ОС._
- `fs ::`[`pathSeparator()`](#method-pathseparator) - _Возвращает символ разделитель для файловых-путей на текущей ОС._
- `fs ::`[`valid()`](#method-valid) - _Проверяет имя файла на корректность._
- `fs ::`[`abs()`](#method-abs) - _Возвращает абсолютный путь._
- `fs ::`[`name()`](#method-name) - _Возвращает имя файла пути._
- `fs ::`[`nameNoExt()`](#method-namenoext) - _Возвращает имя файла пути отсекая расшерение с точкой._
- `fs ::`[`pathNoExt()`](#method-pathnoext) - _Возвращает путь отсекая расшерение с точкой._
- `fs ::`[`relativize()`](#method-relativize)
- `fs ::`[`ext()`](#method-ext) - _Возвращает расширение пути или файла без точки._
- `fs ::`[`hasExt()`](#method-hasext) - _Check that $path has an extension from the extension set._
- `fs ::`[`parent()`](#method-parent) - _Возвращает родительскую директорию._
- `fs ::`[`ensureParent()`](#method-ensureparent) - _Проверяет - есть ли родительские директории для пути и пытается их создать если их нет._
- `fs ::`[`normalize()`](#method-normalize) - _Приводит файловый путь к родному виду текущей ОС._
- `fs ::`[`exists()`](#method-exists) - _Проверяет, существует ли файл._
- `fs ::`[`size()`](#method-size) - _Возвращает размер файла в байтах._
- `fs ::`[`isFile()`](#method-isfile) - _Проверяет, является ли указанный путь файлом._
- `fs ::`[`isDir()`](#method-isdir) - _Проверяет, является ли указанный путь папкой._
- `fs ::`[`isHidden()`](#method-ishidden) - _Проверяет, является ли указанный путь скрытым системой._
- `fs ::`[`time()`](#method-time) - _Возвращает последнее время модификации пути в unix timestamp (млсек)._
- `fs ::`[`makeDir()`](#method-makedir) - _Создает папку по указаному пути если их еще нет._
- `fs ::`[`makeFile()`](#method-makefile) - _Создает пустой файл, если файл уже существует, перезаписывает его._
- `fs ::`[`delete()`](#method-delete) - _Удаляет файл или папку (с очисткой)._
- `fs ::`[`clean()`](#method-clean) - _Удаляет все файлы найденные по указанному пути. Метод не удаляет саму указанную директорию._
- `fs ::`[`scan()`](#method-scan) - _Сканирует директорию с коллбэком или фильтром, и может возвращать список найденного, если_
- `fs ::`[`crc32()`](#method-crc32) - _Возвращает crc32 сумму файла или потока (stream), null если неудача!_
- `fs ::`[`hash()`](#method-hash) - _Возвращает хеш файла или потока (stream), по-умолчанию MD5._
- `fs ::`[`copy()`](#method-copy) - _Копирует из одного файла/потока(stream) в другой файл/поток._
- `fs ::`[`move()`](#method-move) - _Переименновывает или перемещает файл, либо пустую папку._
- `fs ::`[`rename()`](#method-rename) - _Задает файлу новое название, возвращает true при успехе._
- `fs ::`[`get()`](#method-get) - _Возвращает данные полученные из потока или файла в виде бинарной строки._
- `fs ::`[`parseAs()`](#method-parseas) - _Читает данные в переданном формате из источника и возвращает результат._
- `fs ::`[`parse()`](#method-parse) - _Читает данные в формате на основе расширения пути из источника и возвращает результат._
- `fs ::`[`formatAs()`](#method-formatas) - _Записывает данные в нужном формате._
- `fs ::`[`format()`](#method-format) - _Записывает данные в нужном формате на основе расширения._
- `fs ::`[`match()`](#method-match) - _Tells if given path matches this matcher's pattern._

---
# Статичные Методы

<a name="method-separator"></a>

### separator()
```php
fs::separator(): string
```
Возвращает символ разделитель для имен файлов на текущей ОС.

---

<a name="method-pathseparator"></a>

### pathSeparator()
```php
fs::pathSeparator(): string
```
Возвращает символ разделитель для файловых-путей на текущей ОС.

---

<a name="method-valid"></a>

### valid()
```php
fs::valid(mixed $name): bool
```
Проверяет имя файла на корректность.

---

<a name="method-abs"></a>

### abs()
```php
fs::abs(mixed $path): string
```
Возвращает абсолютный путь.

---

<a name="method-name"></a>

### name()
```php
fs::name(mixed $path): string
```
Возвращает имя файла пути.

---

<a name="method-namenoext"></a>

### nameNoExt()
```php
fs::nameNoExt(mixed $path): string
```
Возвращает имя файла пути отсекая расшерение с точкой.

---

<a name="method-pathnoext"></a>

### pathNoExt()
```php
fs::pathNoExt(string $path): string
```
Возвращает путь отсекая расшерение с точкой.

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
Возвращает расширение пути или файла без точки.

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
Возвращает родительскую директорию.

---

<a name="method-ensureparent"></a>

### ensureParent()
```php
fs::ensureParent(string $path): bool
```
Проверяет - есть ли родительские директории для пути и пытается их создать если их нет.
См. также: makeDir().

---

<a name="method-normalize"></a>

### normalize()
```php
fs::normalize(mixed $path): string
```
Приводит файловый путь к родному виду текущей ОС.

---

<a name="method-exists"></a>

### exists()
```php
fs::exists(mixed $path): string
```
Проверяет, существует ли файл.

---

<a name="method-size"></a>

### size()
```php
fs::size(mixed $path): int
```
Возвращает размер файла в байтах.

---

<a name="method-isfile"></a>

### isFile()
```php
fs::isFile(mixed $path): bool
```
Проверяет, является ли указанный путь файлом.

---

<a name="method-isdir"></a>

### isDir()
```php
fs::isDir(mixed $path): bool
```
Проверяет, является ли указанный путь папкой.

---

<a name="method-ishidden"></a>

### isHidden()
```php
fs::isHidden(mixed $path): bool
```
Проверяет, является ли указанный путь скрытым системой.

---

<a name="method-time"></a>

### time()
```php
fs::time(mixed $path): int
```
Возвращает последнее время модификации пути в unix timestamp (млсек).

---

<a name="method-makedir"></a>

### makeDir()
```php
fs::makeDir(string $path): bool
```
Создает папку по указаному пути если их еще нет.

---

<a name="method-makefile"></a>

### makeFile()
```php
fs::makeFile(mixed $path): bool
```
Создает пустой файл, если файл уже существует, перезаписывает его.

---

<a name="method-delete"></a>

### delete()
```php
fs::delete(mixed $path): bool
```
Удаляет файл или папку (с очисткой).

---

<a name="method-clean"></a>

### clean()
```php
fs::clean(string $path, callable|array $filter): array
```
Удаляет все файлы найденные по указанному пути. Метод не удаляет саму указанную директорию.
Возвращает массив с ключами error, success и skip, в которых список файлов.

Фильтр может быть в виде массива:
[
namePattern => string (regex),
extensions => [...],
excludeExtensions => [...],
excludeDirs => bool,
excludeFiles => bool,
excludeHidden => bool,

minSize => int (мин. размер файла, включительно)
maxSize => int (макс. размер файла, включительно),
minTime => int, millis (мин. время изменения файла, включительно)
maxTime => int, millis (макс. время изменения файла, включительно)

callback => function (File $file, $depth) { }
]

---

<a name="method-scan"></a>

### scan()
```php
fs::scan(string $path, callable|array $filter, int $maxDepth, bool $subIsFirst): array
```
Сканирует директорию с коллбэком или фильтром, и может возвращать список найденного, если
из коллбэка возвращать результат или если коллбэк не передан.

Фильтр в виде массива:
[
namePattern => string (regex),
extensions => [...],
excludeExtensions => [...],
excludeDirs => bool,
excludeFiles => bool,
excludeHidden => bool,

minSize => int (мин. размер файла, включительно)
maxSize => int (макс. размер файла, включительно),
minTime => int, millis (мин. время изменения файла, включительно)
maxTime => int, millis (макс. время изменения файла, включительно)

callback => function (File $file, $depth) { }
]

---

<a name="method-crc32"></a>

### crc32()
```php
fs::crc32(string|Stream $source): int|null
```
Возвращает crc32 сумму файла или потока (stream), null если неудача!

---

<a name="method-hash"></a>

### hash()
```php
fs::hash(string|Stream $source, string $algo, callable $onProgress): string|null
```
Возвращает хеш файла или потока (stream), по-умолчанию MD5.

---

<a name="method-copy"></a>

### copy()
```php
fs::copy(string|File|Stream $source, string|File|Stream $dest, callable $onProgress, int $bufferSize): int
```
Копирует из одного файла/потока(stream) в другой файл/поток.

---

<a name="method-move"></a>

### move()
```php
fs::move(string $fromPath, string $toPath): bool
```
Переименновывает или перемещает файл, либо пустую папку.

---

<a name="method-rename"></a>

### rename()
```php
fs::rename(string $pathToFile, string $newName): bool
```
Задает файлу новое название, возвращает true при успехе.

---

<a name="method-get"></a>

### get()
```php
fs::get(string $source, null|string $charset, string $mode): string
```
Возвращает данные полученные из потока или файла в виде бинарной строки.

---

<a name="method-parseas"></a>

### parseAs()
```php
fs::parseAs(mixed $path, string $format, int $flags): mixed
```
Читает данные в переданном формате из источника и возвращает результат.

---

<a name="method-parse"></a>

### parse()
```php
fs::parse(mixed $path, int $flags): mixed
```
Читает данные в формате на основе расширения пути из источника и возвращает результат.

---

<a name="method-formatas"></a>

### formatAs()
```php
fs::formatAs(mixed $path, mixed $value, string $format, int $flags): void
```
Записывает данные в нужном формате.

---

<a name="method-format"></a>

### format()
```php
fs::format(mixed $path, mixed $value, int $flags): void
```
Записывает данные в нужном формате на основе расширения.

---

<a name="method-match"></a>

### match()
```php
fs::match(string $path, string $fsPattern): bool
```
Tells if given path matches this matcher's pattern.
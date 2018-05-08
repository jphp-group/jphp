# System

- **class** `System` (`php\lang\System`)
- **package** `std`
- **source** `php/lang/System.php`

**Description**

Class System

---

#### Static Methods

- `System ::`[`halt()`](#method-halt) - _Exit from program with status globally_
- `System ::`[`gc()`](#method-gc) - _Runs the garbage collector_
- `System ::`[`getEnv()`](#method-getenv)
- `System ::`[`getProperty()`](#method-getproperty) - _Gets a system property by name_
- `System ::`[`setProperty()`](#method-setproperty) - _Sets a system property by name and value._
- `System ::`[`getProperties()`](#method-getproperties)
- `System ::`[`setProperties()`](#method-setproperties)
- `System ::`[`in()`](#method-in)
- `System ::`[`out()`](#method-out)
- `System ::`[`err()`](#method-err)
- `System ::`[`setIn()`](#method-setin) - _Set stdin stream._
- `System ::`[`setOut()`](#method-setout) - _Set stdout stream._
- `System ::`[`setErr()`](#method-seterr) - _Set stderr stream._
- `System ::`[`tempDirectory()`](#method-tempdirectory) - _Returns temp directory that has write access._
- `System ::`[`userDirectory()`](#method-userdirectory)
- `System ::`[`userHome()`](#method-userhome) - _Returns user.home directory._
- `System ::`[`userName()`](#method-username) - _Returns os user name which logged._
- `System ::`[`osName()`](#method-osname) - _Returns Operation System Name, eg:  `Windows`._
- `System ::`[`osVersion()`](#method-osversion) - _Returns Operation System Version._
- `System ::`[`addClassPath()`](#method-addclasspath) - _Add jar from file or classpath dir at runtime to runtime._

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-halt"></a>

### halt()
```php
System::halt(int $status): void
```
Exit from program with status globally

---

<a name="method-gc"></a>

### gc()
```php
System::gc(): void
```
Runs the garbage collector

---

<a name="method-getenv"></a>

### getEnv()
```php
System::getEnv(): string[]
```

---

<a name="method-getproperty"></a>

### getProperty()
```php
System::getProperty(mixed $name, string $def): string
```
Gets a system property by name

---

<a name="method-setproperty"></a>

### setProperty()
```php
System::setProperty(string $name, string $value): string
```
Sets a system property by name and value.

---

<a name="method-getproperties"></a>

### getProperties()
```php
System::getProperties(): array
```

---

<a name="method-setproperties"></a>

### setProperties()
```php
System::setProperties(array $properties): void
```

---

<a name="method-in"></a>

### in()
```php
System::in(): php\io\Stream
```

---

<a name="method-out"></a>

### out()
```php
System::out(): php\io\Stream
```

---

<a name="method-err"></a>

### err()
```php
System::err(): php\io\Stream
```

---

<a name="method-setin"></a>

### setIn()
```php
System::setIn([ php\io\Stream $in): void
```
Set stdin stream.

---

<a name="method-setout"></a>

### setOut()
```php
System::setOut([ php\io\Stream $out, string|null $encoding): void
```
Set stdout stream.

---

<a name="method-seterr"></a>

### setErr()
```php
System::setErr([ php\io\Stream $err, string|null $encoding): void
```
Set stderr stream.

---

<a name="method-tempdirectory"></a>

### tempDirectory()
```php
System::tempDirectory(): string
```
Returns temp directory that has write access.

---

<a name="method-userdirectory"></a>

### userDirectory()
```php
System::userDirectory(): string
```

---

<a name="method-userhome"></a>

### userHome()
```php
System::userHome(): string
```
Returns user.home directory.

---

<a name="method-username"></a>

### userName()
```php
System::userName(): string
```
Returns os user name which logged.

---

<a name="method-osname"></a>

### osName()
```php
System::osName(): string
```
Returns Operation System Name, eg:  `Windows`.

---

<a name="method-osversion"></a>

### osVersion()
```php
System::osVersion(): string
```
Returns Operation System Version.

---

<a name="method-addclasspath"></a>

### addClassPath()
```php
System::addClassPath(mixed $file): void
```
Add jar from file or classpath dir at runtime to runtime.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
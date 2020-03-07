# Module

- **класс** `Module` (`php\lang\Module`)
- **пакет** `std`
- **исходники** `php/lang/Module.php`

**Описание**

Class Module

---

#### Статичные Методы

- `Module ::`[`package()`](#method-package)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _Register all functions and classes of module in current environment_
- `->`[`getName()`](#method-getname)
- `->`[`call()`](#method-call) - _Include module and return result_
- `->`[`dumpJVMClasses()`](#method-dumpjvmclasses) - _Dump all the jvm classes of the module as .class files into targetDir_
- `->`[`dump()`](#method-dump)
- `->`[`getData()`](#method-getdata) - _Java Bytecode data (.class file)_
- `->`[`cleanData()`](#method-cleandata) - _Remove bytecode data._
- `->`[`getClasses()`](#method-getclasses)
- `->`[`getFunctions()`](#method-getfunctions)
- `->`[`getConstants()`](#method-getconstants)

---
# Статичные Методы

<a name="method-package"></a>

### package()
```php
Module::package(string $name, array $classes): void
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(File|Stream|string $source, bool $compiled, bool $debugInformation): void
```
Register all functions and classes of module in current environment

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-call"></a>

### call()
```php
call(array $variables): mixed
```
Include module and return result

---

<a name="method-dumpjvmclasses"></a>

### dumpJVMClasses()
```php
dumpJVMClasses(string $targetDir, bool $saveDebugInfo): array
```
Dump all the jvm classes of the module as .class files into targetDir

---

<a name="method-dump"></a>

### dump()
```php
dump(File|Stream|string $target, bool $saveDebugInfo, bool $includeJvmData): void
```

---

<a name="method-getdata"></a>

### getData()
```php
getData(): string
```
Java Bytecode data (.class file)

---

<a name="method-cleandata"></a>

### cleanData()
```php
cleanData(): void
```
Remove bytecode data.

---

<a name="method-getclasses"></a>

### getClasses()
```php
getClasses(): \ReflectionClass[]
```

---

<a name="method-getfunctions"></a>

### getFunctions()
```php
getFunctions(): \ReflectionFunction[]
```

---

<a name="method-getconstants"></a>

### getConstants()
```php
getConstants(): array
```
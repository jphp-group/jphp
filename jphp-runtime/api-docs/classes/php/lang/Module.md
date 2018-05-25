# Module

- **class** `Module` (`php\lang\Module`)
- **package** `std`
- **source** `php/lang/Module.php`

**Description**

Class Module

---

#### Static Methods

- `Module ::`[`package()`](#method-package)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Register all functions and classes of module in current environment_
- `->`[`getName()`](#method-getname)
- `->`[`call()`](#method-call) - _Include module and return result_
- `->`[`dump()`](#method-dump)
- `->`[`cleanData()`](#method-cleandata) - _Remove bytecode data._
- `->`[`getClasses()`](#method-getclasses)
- `->`[`getFunctions()`](#method-getfunctions)
- `->`[`getConstants()`](#method-getconstants)

---
# Static Methods

<a name="method-package"></a>

### package()
```php
Module::package(string $name, array $classes): void
```

---
# Methods

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

<a name="method-dump"></a>

### dump()
```php
dump(File|Stream|string $target, bool $saveDebugInfo): void
```

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
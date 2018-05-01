# Environment

- **class** `Environment` (`php\lang\Environment`)
- **package** `std`
- **source** `php/lang/Environment.php`

**Description**

Class Environment

---

#### Static Methods

- `Environment ::`[`current()`](#method-current) - _Get environment of current execution_

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`registerSourceMap()`](#method-registersourcemap)
- `->`[`unregisterSourceMap()`](#method-unregistersourcemap)
- `->`[`execute()`](#method-execute) - _Executes $runnable in the environment_
- `->`[`importClass()`](#method-importclass) - _Imports the $className to the environment_
- `->`[`exportClass()`](#method-exportclass) - _Exports the $className from th environment_
- `->`[`importFunction()`](#method-importfunction) - _Imports the $functionName to the environment_
- `->`[`exportFunction()`](#method-exportfunction) - _Exports the $functionName from the environment_
- `->`[`importAutoLoaders()`](#method-importautoloaders) - _Imports the all spl auto loaders to the environment._
- `->`[`defineConstant()`](#method-defineconstant)
- `->`[`onMessage()`](#method-onmessage) - _Handles messages that sent to the environment_
- `->`[`onOutput()`](#method-onoutput)
- `->`[`sendMessage()`](#method-sendmessage) - _Send message to the environment_
- `->`[`findModule()`](#method-findmodule)
- `->`[`getPackages()`](#method-getpackages)
- `->`[`hasPackage()`](#method-haspackage)
- `->`[`getPackage()`](#method-getpackage)
- `->`[`setPackage()`](#method-setpackage)
- `->`[`registerExtension()`](#method-registerextension)
- `->`[`addSuperGlobal()`](#method-addsuperglobal)
- `->`[`hasSuperGlobal()`](#method-hassuperglobal)
- `->`[`getSuperGlobals()`](#method-getsuperglobals)
- `->`[`getGlobals()`](#method-getglobals) - _$GLOBALS of environment._
- `->`[`getGlobal()`](#method-getglobal)
- `->`[`hasGlobal()`](#method-hasglobal)
- `->`[`setGlobal()`](#method-setglobal)

---
# Static Methods

<a name="method-current"></a>

### current()
```php
Environment::current(): Environment
```
Get environment of current execution

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\lang\Environment $parent, int $flags): void
```

---

<a name="method-registersourcemap"></a>

### registerSourceMap()
```php
registerSourceMap(php\lang\SourceMap $sourceMap): void
```

---

<a name="method-unregistersourcemap"></a>

### unregisterSourceMap()
```php
unregisterSourceMap(php\lang\SourceMap $sourceMap): void
```

---

<a name="method-execute"></a>

### execute()
```php
execute(callable $runnable): mixed
```
Executes $runnable in the environment

---

<a name="method-importclass"></a>

### importClass()
```php
importClass(string $className): void
```
Imports the $className to the environment

---

<a name="method-exportclass"></a>

### exportClass()
```php
exportClass(string $className): void
```
Exports the $className from th environment

---

<a name="method-importfunction"></a>

### importFunction()
```php
importFunction(string $functionName): void
```
Imports the $functionName to the environment

---

<a name="method-exportfunction"></a>

### exportFunction()
```php
exportFunction(string $functionName): void
```
Exports the $functionName from the environment

---

<a name="method-importautoloaders"></a>

### importAutoLoaders()
```php
importAutoLoaders(): void
```
Imports the all spl auto loaders to the environment.

---

<a name="method-defineconstant"></a>

### defineConstant()
```php
defineConstant(string $name, mixed $value, bool $caseSensitive): void
```

---

<a name="method-onmessage"></a>

### onMessage()
```php
onMessage(callable $callback): void
```
Handles messages that sent to the environment

---

<a name="method-onoutput"></a>

### onOutput()
```php
onOutput(callable|null $callback): void
```

---

<a name="method-sendmessage"></a>

### sendMessage()
```php
sendMessage(mixed $message): mixed
```
Send message to the environment

---

<a name="method-findmodule"></a>

### findModule()
```php
findModule(string $path): Module|null
```

---

<a name="method-getpackages"></a>

### getPackages()
```php
getPackages(): Package[]
```

---

<a name="method-haspackage"></a>

### hasPackage()
```php
hasPackage(string $name): bool
```

---

<a name="method-getpackage"></a>

### getPackage()
```php
getPackage(string $name): Package
```

---

<a name="method-setpackage"></a>

### setPackage()
```php
setPackage(string $name, php\lang\Package $package): void
```

---

<a name="method-registerextension"></a>

### registerExtension()
```php
registerExtension(string $extensionId): void
```

---

<a name="method-addsuperglobal"></a>

### addSuperGlobal()
```php
addSuperGlobal(string $name, mixed $value): void
```

---

<a name="method-hassuperglobal"></a>

### hasSuperGlobal()
```php
hasSuperGlobal(string $name): bool
```

---

<a name="method-getsuperglobals"></a>

### getSuperGlobals()
```php
getSuperGlobals(): array
```

---

<a name="method-getglobals"></a>

### getGlobals()
```php
getGlobals(): array
```
$GLOBALS of environment.

---

<a name="method-getglobal"></a>

### getGlobal()
```php
getGlobal(string $name): mixed
```

---

<a name="method-hasglobal"></a>

### hasGlobal()
```php
hasGlobal(string $name): bool
```

---

<a name="method-setglobal"></a>

### setGlobal()
```php
setGlobal(string $name, mixed $value): void
```
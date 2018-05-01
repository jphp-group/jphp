# Environment

- **класс** `Environment` (`php\lang\Environment`)
- **пакет** `std`
- **исходники** `php/lang/Environment.php`

**Описание**

Class Environment

---

#### Статичные Методы

- `Environment ::`[`current()`](#method-current) - _Взять окружение текущего выполнения_

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`registerSourceMap()`](#method-registersourcemap)
- `->`[`unregisterSourceMap()`](#method-unregistersourcemap)
- `->`[`execute()`](#method-execute) - _Выполняет $runnable в текущем своем окружении_
- `->`[`importClass()`](#method-importclass) - _Импортирует класс в свое окружение_
- `->`[`exportClass()`](#method-exportclass) - _Экмпортирует класс из своего окружения_
- `->`[`importFunction()`](#method-importfunction) - _Импортирует функцию в свое окружение_
- `->`[`exportFunction()`](#method-exportfunction) - _Экспортирует функцию из своего окружения_
- `->`[`importAutoLoaders()`](#method-importautoloaders) - _Imports the all spl auto loaders to the environment._
- `->`[`defineConstant()`](#method-defineconstant)
- `->`[`onMessage()`](#method-onmessage) - _Обрабатывает сообщения, что были посланы в окружение_
- `->`[`onOutput()`](#method-onoutput)
- `->`[`sendMessage()`](#method-sendmessage) - _Послать сообщение окружению_
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
# Статичные Методы

<a name="method-current"></a>

### current()
```php
Environment::current(): Environment
```
Взять окружение текущего выполнения

---
# Методы

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
Выполняет $runnable в текущем своем окружении

---

<a name="method-importclass"></a>

### importClass()
```php
importClass(string $className): void
```
Импортирует класс в свое окружение

---

<a name="method-exportclass"></a>

### exportClass()
```php
exportClass(string $className): void
```
Экмпортирует класс из своего окружения

---

<a name="method-importfunction"></a>

### importFunction()
```php
importFunction(string $functionName): void
```
Импортирует функцию в свое окружение

---

<a name="method-exportfunction"></a>

### exportFunction()
```php
exportFunction(string $functionName): void
```
Экспортирует функцию из своего окружения

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
Обрабатывает сообщения, что были посланы в окружение

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
Послать сообщение окружению

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
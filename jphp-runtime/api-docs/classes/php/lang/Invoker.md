# Invoker

- **class** `Invoker` (`php\lang\Invoker`)
- **package** `std`
- **source** `php/lang/Invoker.php`

**Description**

Class for calling methods/functions/etc.

---

#### Static Methods

- `Invoker ::`[`of()`](#method-of)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`callArray()`](#method-callarray) - _Call with array arguments_
- `->`[`call()`](#method-call) - _Call the current callback_
- `->`[`__invoke()`](#method-__invoke) - _Alias of call() method_
- `->`[`canAccess()`](#method-canaccess) - _Check access to invoke the method at a moment_
- `->`[`getDescription()`](#method-getdescription) - _Returns description of the method - name + argument info_
- `->`[`getArgumentCount()`](#method-getargumentcount) - _Returns argument count of the method_
- `->`[`isClosure()`](#method-isclosure) - _Checks it is a closure_
- `->`[`isNamedFunction()`](#method-isnamedfunction) - _Checks it is a named function_
- `->`[`isStaticCall()`](#method-isstaticcall) - _Checks it is a static call_
- `->`[`isDynamicCall()`](#method-isdynamiccall) - _Checks it is a dynamic call_

---
# Static Methods

<a name="method-of"></a>

### of()
```php
Invoker::of(mixed|callable $callback): Invoker|null
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(callable $callback): void
```

---

<a name="method-callarray"></a>

### callArray()
```php
callArray(array $args): Array
```
Call with array arguments

---

<a name="method-call"></a>

### call()
```php
call(array $args): int|mixed
```
Call the current callback

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(): void
```
Alias of call() method

---

<a name="method-canaccess"></a>

### canAccess()
```php
canAccess(): bool
```
Check access to invoke the method at a moment

---

<a name="method-getdescription"></a>

### getDescription()
```php
getDescription(): string
```
Returns description of the method - name + argument info

---

<a name="method-getargumentcount"></a>

### getArgumentCount()
```php
getArgumentCount(): int
```
Returns argument count of the method

---

<a name="method-isclosure"></a>

### isClosure()
```php
isClosure(): bool
```
Checks it is a closure

---

<a name="method-isnamedfunction"></a>

### isNamedFunction()
```php
isNamedFunction(): bool
```
Checks it is a named function

---

<a name="method-isstaticcall"></a>

### isStaticCall()
```php
isStaticCall(): bool
```
Checks it is a static call

---

<a name="method-isdynamiccall"></a>

### isDynamicCall()
```php
isDynamicCall(): bool
```
Checks it is a dynamic call
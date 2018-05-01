# Invoker

- **класс** `Invoker` (`php\lang\Invoker`)
- **пакет** `std`
- **исходники** `php/lang/Invoker.php`

**Описание**

Класс для вызова методов/функций/и т.д.

---

#### Статичные Методы

- `Invoker ::`[`of()`](#method-of)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`callArray()`](#method-callarray) - _Вызвать с массивом аргументов_
- `->`[`call()`](#method-call) - _Вызвать текущий callback_
- `->`[`__invoke()`](#method-__invoke) - _Синоним метода call()_
- `->`[`canAccess()`](#method-canaccess) - _Проверить - есть ли доступ для вызова метода в какой-то момент_
- `->`[`getDescription()`](#method-getdescription) - _Возвращает описание метода - название + информацию об аргументах_
- `->`[`getArgumentCount()`](#method-getargumentcount) - _Возвращает количество аргументов текущего метода_
- `->`[`isClosure()`](#method-isclosure) - _Проверяет - является ли метод замыканием_
- `->`[`isNamedFunction()`](#method-isnamedfunction) - _Проверяет - является ли это именованной функцией_
- `->`[`isStaticCall()`](#method-isstaticcall) - _Проверяет - является ли это статичным вызовом_
- `->`[`isDynamicCall()`](#method-isdynamiccall) - _Проверяет - является ли это динамичным вызовом_

---
# Статичные Методы

<a name="method-of"></a>

### of()
```php
Invoker::of(mixed|callable $callback): Invoker|null
```

---
# Методы

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
Вызвать с массивом аргументов

---

<a name="method-call"></a>

### call()
```php
call(array $args): int|mixed
```
Вызвать текущий callback

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(): void
```
Синоним метода call()

---

<a name="method-canaccess"></a>

### canAccess()
```php
canAccess(): bool
```
Проверить - есть ли доступ для вызова метода в какой-то момент

---

<a name="method-getdescription"></a>

### getDescription()
```php
getDescription(): string
```
Возвращает описание метода - название + информацию об аргументах

---

<a name="method-getargumentcount"></a>

### getArgumentCount()
```php
getArgumentCount(): int
```
Возвращает количество аргументов текущего метода

---

<a name="method-isclosure"></a>

### isClosure()
```php
isClosure(): bool
```
Проверяет - является ли метод замыканием

---

<a name="method-isnamedfunction"></a>

### isNamedFunction()
```php
isNamedFunction(): bool
```
Проверяет - является ли это именованной функцией

---

<a name="method-isstaticcall"></a>

### isStaticCall()
```php
isStaticCall(): bool
```
Проверяет - является ли это статичным вызовом

---

<a name="method-isdynamiccall"></a>

### isDynamicCall()
```php
isDynamicCall(): bool
```
Проверяет - является ли это динамичным вызовом
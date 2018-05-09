# JavaException

- **класс** `JavaException` (`php\lang\JavaException`) **унаследован от** `Exception` (`Exception`)
- **пакет** `std`
- **исходники** `php/lang/JavaException.php`

**Классы наследники**

> [TimeoutException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/TimeoutException.ru.md), [IOException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/IOException.ru.md), [IllegalArgumentException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalArgumentException.ru.md), [IllegalStateException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalStateException.ru.md), [InterruptedException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/InterruptedException.ru.md), [NumberFormatException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NumberFormatException.ru.md), [SocketException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/SocketException.ru.md), [RegexException](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/RegexException.ru.md)

**Описание**

Class JavaException

---

#### Методы

- `->`[`isRuntimeException()`](#method-isruntimeexception) - _Check exception instance of java.lang.RuntimeException_
- `->`[`isNullPointerException()`](#method-isnullpointerexception) - _Check exception instance of java.lang.NullPointerException_
- `->`[`isIllegalArgumentException()`](#method-isillegalargumentexception) - _Check exception instance of java.lang.IllegalArgumentException_
- `->`[`isNumberFormatException()`](#method-isnumberformatexception) - _Check exception instance of java.lang.NumberFormatException_
- `->`[`getExceptionClass()`](#method-getexceptionclass)
- `->`[`getJavaException()`](#method-getjavaexception)
- `->`[`printJVMStackTrace()`](#method-printjvmstacktrace) - _Print jvm stack trace_
- `->`[`getJVMStackTrace()`](#method-getjvmstacktrace) - _Get jvm stack trace as string._
- `->`[`getErrno()`](#method-geterrno)

---
# Методы

<a name="method-isruntimeexception"></a>

### isRuntimeException()
```php
isRuntimeException(): bool
```
Check exception instance of java.lang.RuntimeException

---

<a name="method-isnullpointerexception"></a>

### isNullPointerException()
```php
isNullPointerException(): bool
```
Check exception instance of java.lang.NullPointerException

---

<a name="method-isillegalargumentexception"></a>

### isIllegalArgumentException()
```php
isIllegalArgumentException(): bool
```
Check exception instance of java.lang.IllegalArgumentException

---

<a name="method-isnumberformatexception"></a>

### isNumberFormatException()
```php
isNumberFormatException(): bool
```
Check exception instance of java.lang.NumberFormatException

---

<a name="method-getexceptionclass"></a>

### getExceptionClass()
```php
getExceptionClass(): JavaClass
```

---

<a name="method-getjavaexception"></a>

### getJavaException()
```php
getJavaException(): JavaObject
```

---

<a name="method-printjvmstacktrace"></a>

### printJVMStackTrace()
```php
printJVMStackTrace(): void
```
Print jvm stack trace

---

<a name="method-getjvmstacktrace"></a>

### getJVMStackTrace()
```php
getJVMStackTrace(): string
```
Get jvm stack trace as string.

---

<a name="method-geterrno"></a>

### getErrno()
```php
getErrno(): void
```
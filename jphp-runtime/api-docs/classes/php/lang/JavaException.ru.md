# JavaException

- **класс** `JavaException` (`php\lang\JavaException`) **унаследован от** `Exception` (`Exception`)
- **пакет** `std`
- **исходники** [`php/lang/JavaException.php`](./src/main/resources/JPHP-INF/sdk/php/lang/JavaException.php)

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

<a name="method-geterrno"></a>

### getErrno()
```php
getErrno(): void
```
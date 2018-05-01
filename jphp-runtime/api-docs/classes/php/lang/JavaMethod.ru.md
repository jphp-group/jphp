# JavaMethod

- **класс** `JavaMethod` (`php\lang\JavaMethod`) **унаследован от** [`JavaReflection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaReflection.ru.md)
- **исходники** `php/lang/JavaMethod.php`

---

#### Методы

- `->`[`invoke()`](#method-invoke) - _Invoke method_
- `->`[`invokeArgs()`](#method-invokeargs)
- `->`[`getName()`](#method-getname)
- `->`[`isStatic()`](#method-isstatic)
- `->`[`isFinal()`](#method-isfinal)
- `->`[`isAbstract()`](#method-isabstract)
- `->`[`isPublic()`](#method-ispublic)
- `->`[`isProtected()`](#method-isprotected)
- `->`[`isPrivate()`](#method-isprivate)
- `->`[`isNative()`](#method-isnative)
- `->`[`isSynchronized()`](#method-issynchronized)
- `->`[`isVarArgs()`](#method-isvarargs)
- `->`[`getDeclaringClass()`](#method-getdeclaringclass)
- `->`[`getReturnedType()`](#method-getreturnedtype)
- `->`[`isAnnotationPresent()`](#method-isannotationpresent)
- `->`[`getParameterTypes()`](#method-getparametertypes)
- `->`[`getParameterCount()`](#method-getparametercount)
- См. также в родительском классе [JavaReflection](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaReflection.ru.md)

---
# Методы

<a name="method-invoke"></a>

### invoke()
```php
invoke(php\lang\JavaObject $object): void
```
Invoke method

---

<a name="method-invokeargs"></a>

### invokeArgs()
```php
invokeArgs(php\lang\JavaObject $object, array $arguments): void
```

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-isstatic"></a>

### isStatic()
```php
isStatic(): bool
```

---

<a name="method-isfinal"></a>

### isFinal()
```php
isFinal(): bool
```

---

<a name="method-isabstract"></a>

### isAbstract()
```php
isAbstract(): bool
```

---

<a name="method-ispublic"></a>

### isPublic()
```php
isPublic(): bool
```

---

<a name="method-isprotected"></a>

### isProtected()
```php
isProtected(): bool
```

---

<a name="method-isprivate"></a>

### isPrivate()
```php
isPrivate(): bool
```

---

<a name="method-isnative"></a>

### isNative()
```php
isNative(): bool
```

---

<a name="method-issynchronized"></a>

### isSynchronized()
```php
isSynchronized(): bool
```

---

<a name="method-isvarargs"></a>

### isVarArgs()
```php
isVarArgs(): bool
```

---

<a name="method-getdeclaringclass"></a>

### getDeclaringClass()
```php
getDeclaringClass(): JavaClass
```

---

<a name="method-getreturnedtype"></a>

### getReturnedType()
```php
getReturnedType(): JavaClass
```

---

<a name="method-isannotationpresent"></a>

### isAnnotationPresent()
```php
isAnnotationPresent(string $annotationClassName): bool
```

---

<a name="method-getparametertypes"></a>

### getParameterTypes()
```php
getParameterTypes(): JavaClass[]
```

---

<a name="method-getparametercount"></a>

### getParameterCount()
```php
getParameterCount(): int
```
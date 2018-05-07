# JavaClass

- **class** `JavaClass` (`php\lang\JavaClass`)
- **package** `std`
- **source** `php/lang/JavaClass.php`

---

#### Static Methods

- `JavaClass ::`[`primitive()`](#method-primitive)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`isStatic()`](#method-isstatic)
- `->`[`isFinal()`](#method-isfinal)
- `->`[`isAbstract()`](#method-isabstract)
- `->`[`isInterface()`](#method-isinterface)
- `->`[`isEnum()`](#method-isenum)
- `->`[`isAnnotation()`](#method-isannotation)
- `->`[`isArray()`](#method-isarray)
- `->`[`isPrimitive()`](#method-isprimitive)
- `->`[`isAnonymousClass()`](#method-isanonymousclass)
- `->`[`isMemberClass()`](#method-ismemberclass)
- `->`[`getName()`](#method-getname)
- `->`[`getSimpleName()`](#method-getsimplename)
- `->`[`getCanonicalName()`](#method-getcanonicalname)
- `->`[`getSuperClass()`](#method-getsuperclass)
- `->`[`getModifiers()`](#method-getmodifiers)
- `->`[`isAnnotationPresent()`](#method-isannotationpresent)
- `->`[`getInterfaces()`](#method-getinterfaces)
- `->`[`getDeclaredMethod()`](#method-getdeclaredmethod)
- `->`[`getDeclaredMethods()`](#method-getdeclaredmethods)
- `->`[`getDeclaredField()`](#method-getdeclaredfield)
- `->`[`getDeclaredFields()`](#method-getdeclaredfields)
- `->`[`newInstance()`](#method-newinstance)
- `->`[`newInstanceArgs()`](#method-newinstanceargs)
- `->`[`isAssignableFrom()`](#method-isassignablefrom)
- `->`[`isSubClass()`](#method-issubclass)
- `->`[`getEnumConstants()`](#method-getenumconstants)
- `->`[`getResource()`](#method-getresource)

---
# Static Methods

<a name="method-primitive"></a>

### primitive()
```php
JavaClass::primitive(string $name): void
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $className): void
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

<a name="method-isinterface"></a>

### isInterface()
```php
isInterface(): bool
```

---

<a name="method-isenum"></a>

### isEnum()
```php
isEnum(): bool
```

---

<a name="method-isannotation"></a>

### isAnnotation()
```php
isAnnotation(): bool
```

---

<a name="method-isarray"></a>

### isArray()
```php
isArray(): bool
```

---

<a name="method-isprimitive"></a>

### isPrimitive()
```php
isPrimitive(): bool
```

---

<a name="method-isanonymousclass"></a>

### isAnonymousClass()
```php
isAnonymousClass(): bool
```

---

<a name="method-ismemberclass"></a>

### isMemberClass()
```php
isMemberClass(): bool
```

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-getsimplename"></a>

### getSimpleName()
```php
getSimpleName(): string
```

---

<a name="method-getcanonicalname"></a>

### getCanonicalName()
```php
getCanonicalName(): string
```

---

<a name="method-getsuperclass"></a>

### getSuperClass()
```php
getSuperClass(): JavaClass|null
```

---

<a name="method-getmodifiers"></a>

### getModifiers()
```php
getModifiers(): int
```

---

<a name="method-isannotationpresent"></a>

### isAnnotationPresent()
```php
isAnnotationPresent(string $annotationClassName): bool
```

---

<a name="method-getinterfaces"></a>

### getInterfaces()
```php
getInterfaces(): JavaClass[]
```

---

<a name="method-getdeclaredmethod"></a>

### getDeclaredMethod()
```php
getDeclaredMethod(string $name, array $types): JavaMethod
```

---

<a name="method-getdeclaredmethods"></a>

### getDeclaredMethods()
```php
getDeclaredMethods(): JavaMethod[]
```

---

<a name="method-getdeclaredfield"></a>

### getDeclaredField()
```php
getDeclaredField(mixed $name): JavaField
```

---

<a name="method-getdeclaredfields"></a>

### getDeclaredFields()
```php
getDeclaredFields(): JavaField[]
```

---

<a name="method-newinstance"></a>

### newInstance()
```php
newInstance(): JavaObject
```

---

<a name="method-newinstanceargs"></a>

### newInstanceArgs()
```php
newInstanceArgs(array $types, array $arguments): JavaObject
```

---

<a name="method-isassignablefrom"></a>

### isAssignableFrom()
```php
isAssignableFrom(php\lang\JavaClass $class): bool
```

---

<a name="method-issubclass"></a>

### isSubClass()
```php
isSubClass(string $className): bool
```

---

<a name="method-getenumconstants"></a>

### getEnumConstants()
```php
getEnumConstants(): JavaObject[]
```

---

<a name="method-getresource"></a>

### getResource()
```php
getResource(string $name): string|null
```
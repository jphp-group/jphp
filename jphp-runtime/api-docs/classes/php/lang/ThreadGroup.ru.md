# ThreadGroup

- **класс** `ThreadGroup` (`php\lang\ThreadGroup`)
- **пакет** `std`
- **исходники** `php/lang/ThreadGroup.php`

**Описание**

Class ThreadGroup

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`getName()`](#method-getname)
- `->`[`getParent()`](#method-getparent)
- `->`[`getActiveCount()`](#method-getactivecount)
- `->`[`getActiveGroupCount()`](#method-getactivegroupcount)
- `->`[`isDaemon()`](#method-isdaemon)
- `->`[`setDaemon()`](#method-setdaemon)
- `->`[`isDestroyed()`](#method-isdestroyed)
- `->`[`getMaxPriority()`](#method-getmaxpriority)
- `->`[`setMaxPriority()`](#method-setmaxpriority)
- `->`[`destroy()`](#method-destroy) - _Destroys this thread group and all of its subgroups._
- `->`[`checkAccess()`](#method-checkaccess) - _Determines if the currently running thread has permission to_
- `->`[`interrupt()`](#method-interrupt) - _Interrupts all threads in this thread group._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(mixed $name, php\lang\ThreadGroup $parent): void
```

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-getparent"></a>

### getParent()
```php
getParent(): ThreadGroup|null
```

---

<a name="method-getactivecount"></a>

### getActiveCount()
```php
getActiveCount(): int
```

---

<a name="method-getactivegroupcount"></a>

### getActiveGroupCount()
```php
getActiveGroupCount(): int
```

---

<a name="method-isdaemon"></a>

### isDaemon()
```php
isDaemon(): bool
```

---

<a name="method-setdaemon"></a>

### setDaemon()
```php
setDaemon(bool $value): void
```

---

<a name="method-isdestroyed"></a>

### isDestroyed()
```php
isDestroyed(): bool
```

---

<a name="method-getmaxpriority"></a>

### getMaxPriority()
```php
getMaxPriority(): int
```

---

<a name="method-setmaxpriority"></a>

### setMaxPriority()
```php
setMaxPriority(int $value): void
```

---

<a name="method-destroy"></a>

### destroy()
```php
destroy(): void
```
Destroys this thread group and all of its subgroups.

---

<a name="method-checkaccess"></a>

### checkAccess()
```php
checkAccess(): void
```
Determines if the currently running thread has permission to
modify this thread group.

---

<a name="method-interrupt"></a>

### interrupt()
```php
interrupt(): void
```
Interrupts all threads in this thread group.
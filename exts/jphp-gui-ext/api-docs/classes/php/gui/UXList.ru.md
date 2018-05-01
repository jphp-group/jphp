# UXList

- **класс** `UXList` (`php\gui\UXList`)
- **пакет** `gui`
- **исходники** `php/gui/UXList.php`

**Описание**

Class UXList

---

#### Свойства

- `->`[`count`](#prop-count) : `int`

---

#### Методы

- `->`[`isEmpty()`](#method-isempty)
- `->`[`isNotEmpty()`](#method-isnotempty)
- `->`[`indexOf()`](#method-indexof)
- `->`[`has()`](#method-has)
- `->`[`add()`](#method-add)
- `->`[`insert()`](#method-insert)
- `->`[`replace()`](#method-replace)
- `->`[`addAll()`](#method-addall)
- `->`[`set()`](#method-set)
- `->`[`setAll()`](#method-setall)
- `->`[`insertAll()`](#method-insertall)
- `->`[`remove()`](#method-remove)
- `->`[`removeByIndex()`](#method-removebyindex)
- `->`[`clear()`](#method-clear) - _..._
- `->`[`last()`](#method-last)
- `->`[`current()`](#method-current) - _{@inheritdoc}_
- `->`[`next()`](#method-next) - _{@inheritdoc}_
- `->`[`key()`](#method-key) - _{@inheritdoc}_
- `->`[`valid()`](#method-valid) - _{@inheritdoc}_
- `->`[`rewind()`](#method-rewind) - _{@inheritdoc}_
- `->`[`count()`](#method-count) - _{@inheritdoc}_
- `->`[`offsetExists()`](#method-offsetexists) - _{@inheritdoc}_
- `->`[`offsetGet()`](#method-offsetget) - _{@inheritdoc}_
- `->`[`offsetSet()`](#method-offsetset) - _{@inheritdoc}_
- `->`[`offsetUnset()`](#method-offsetunset) - _{@inheritdoc}_
- `->`[`addListener()`](#method-addlistener)
- `->`[`toArray()`](#method-toarray)
- `->`[`__clone()`](#method-__clone) - _Available to clone._

---
# Методы

<a name="method-isempty"></a>

### isEmpty()
```php
isEmpty(): bool
```

---

<a name="method-isnotempty"></a>

### isNotEmpty()
```php
isNotEmpty(): bool
```

---

<a name="method-indexof"></a>

### indexOf()
```php
indexOf(mixed $object): int
```

---

<a name="method-has"></a>

### has()
```php
has(mixed $object): bool
```

---

<a name="method-add"></a>

### add()
```php
add(mixed $object): void
```

---

<a name="method-insert"></a>

### insert()
```php
insert(int $index, mixed $object): void
```

---

<a name="method-replace"></a>

### replace()
```php
replace(mixed $object, mixed $newObject): void
```

---

<a name="method-addall"></a>

### addAll()
```php
addAll(iterable $objects): void
```

---

<a name="method-set"></a>

### set()
```php
set(int $index, mixed $object): void
```

---

<a name="method-setall"></a>

### setAll()
```php
setAll(iterable $objects): void
```

---

<a name="method-insertall"></a>

### insertAll()
```php
insertAll(int $index, iterable $objects): void
```

---

<a name="method-remove"></a>

### remove()
```php
remove(mixed $object): void
```

---

<a name="method-removebyindex"></a>

### removeByIndex()
```php
removeByIndex(int $index): void
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```
...

---

<a name="method-last"></a>

### last()
```php
last(): mixed|null
```

---

<a name="method-current"></a>

### current()
```php
current(): void
```
{@inheritdoc}

---

<a name="method-next"></a>

### next()
```php
next(): void
```
{@inheritdoc}

---

<a name="method-key"></a>

### key()
```php
key(): void
```
{@inheritdoc}

---

<a name="method-valid"></a>

### valid()
```php
valid(): void
```
{@inheritdoc}

---

<a name="method-rewind"></a>

### rewind()
```php
rewind(): void
```
{@inheritdoc}

---

<a name="method-count"></a>

### count()
```php
count(): void
```
{@inheritdoc}

---

<a name="method-offsetexists"></a>

### offsetExists()
```php
offsetExists(mixed $offset): void
```
{@inheritdoc}

---

<a name="method-offsetget"></a>

### offsetGet()
```php
offsetGet(mixed $offset): void
```
{@inheritdoc}

---

<a name="method-offsetset"></a>

### offsetSet()
```php
offsetSet(mixed $offset, mixed $value): void
```
{@inheritdoc}

---

<a name="method-offsetunset"></a>

### offsetUnset()
```php
offsetUnset(mixed $offset): void
```
{@inheritdoc}

---

<a name="method-addlistener"></a>

### addListener()
```php
addListener(callable $callback): void
```

---

<a name="method-toarray"></a>

### toArray()
```php
toArray(): array
```

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Available to clone.
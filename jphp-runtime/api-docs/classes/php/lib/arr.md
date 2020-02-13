# arr

- **class** `arr` (`php\lib\arr`)
- **package** `std`
- **source** `php/lib/arr.php`

**Child Classes**

> [items](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/items.md)

**Description**

Library for working with collections - arrays, iterators, etc.

---

#### Static Methods

- `arr ::`[`count()`](#method-count) - _Returns element count of the collection_
- `arr ::`[`has()`](#method-has)
- `arr ::`[`toArray()`](#method-toarray) - _Converts $collection to array_
- `arr ::`[`of()`](#method-of) - _Alias of toArray()_
- `arr ::`[`toList()`](#method-tolist) - _Example: items::toList(['x' => 10, 20], 30, ['x' => 50, 60]) -> [10, 20, 30, 50, 60]_
- `arr ::`[`keys()`](#method-keys) - _Returns all keys of collection_
- `arr ::`[`values()`](#method-values) - _Returns all values of collection_
- `arr ::`[`combine()`](#method-combine) - _Combines two collections to array._
- `arr ::`[`map()`](#method-map)
- `arr ::`[`flatten()`](#method-flatten) - _Returns a new array that is a one-dimensional flattening of this collection (recursively)._
- `arr ::`[`sort()`](#method-sort) - _Sorts the specified list into ascending order_
- `arr ::`[`sortByKeys()`](#method-sortbykeys) - _Sorts the specified list into ascending order by keys_
- `arr ::`[`peak()`](#method-peak) - _Returns the last element of array._
- `arr ::`[`push()`](#method-push)
- `arr ::`[`pop()`](#method-pop)
- `arr ::`[`shift()`](#method-shift)
- `arr ::`[`unshift()`](#method-unshift)
- `arr ::`[`first()`](#method-first)
- `arr ::`[`firstKey()`](#method-firstkey)
- `arr ::`[`last()`](#method-last) - _Alias to peek()._
- `arr ::`[`lastKey()`](#method-lastkey)
- `arr ::`[`reverse()`](#method-reverse)
- `arr ::`[`merge()`](#method-merge)

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-count"></a>

### count()
```php
arr::count(array|Countable|Iterator $collection): int
```
Returns element count of the collection

.. warning:: for iterators it will iterate all elements to return the result


---

<a name="method-has"></a>

### has()
```php
arr::has(array|Traversable $collection, mixed $value, bool $strict): bool
```

---

<a name="method-toarray"></a>

### toArray()
```php
arr::toArray(array|\Traversable $collection, bool $withKeys): array
```
Converts $collection to array

---

<a name="method-of"></a>

### of()
```php
arr::of(array|Iterator $collection, bool|false $withKeys): array
```
Alias of toArray()

---

<a name="method-tolist"></a>

### toList()
```php
arr::toList(mixed $collection, array $others): array
```
Example: items::toList(['x' => 10, 20], 30, ['x' => 50, 60]) -> [10, 20, 30, 50, 60]

---

<a name="method-keys"></a>

### keys()
```php
arr::keys(iterable|array $collection): array
```
Returns all keys of collection

---

<a name="method-values"></a>

### values()
```php
arr::values(array|Iterator $collection): array
```
Returns all values of collection

---

<a name="method-combine"></a>

### combine()
```php
arr::combine(array|Iterator $keys, array|Iterator $values): array|null
```
Combines two collections to array.

---

<a name="method-map"></a>

### map()
```php
arr::map(array|Iterator $collection, callable $callback): void
```

---

<a name="method-flatten"></a>

### flatten()
```php
arr::flatten(array|Iterator $collection, int $maxLevel): array
```
Returns a new array that is a one-dimensional flattening of this collection (recursively).
That is, for every element that is an collection, extract its elements into the new array.
If the optional $maxLevel argument > -1 the level of recursion to flatten.

---

<a name="method-sort"></a>

### sort()
```php
arr::sort(array|Iterator $collection, callable $comparator, bool $saveKeys): array
```
Sorts the specified list into ascending order

---

<a name="method-sortbykeys"></a>

### sortByKeys()
```php
arr::sortByKeys(array|Iterator $collection, callable $comparator, bool $saveKeys): array
```
Sorts the specified list into ascending order by keys

---

<a name="method-peak"></a>

### peak()
```php
arr::peak(mixed $array): mixed
```
Returns the last element of array.

---

<a name="method-push"></a>

### push()
```php
arr::push(array|ArrayAccess $array, mixed $values): void
```

---

<a name="method-pop"></a>

### pop()
```php
arr::pop(array $array): mixed
```

---

<a name="method-shift"></a>

### shift()
```php
arr::shift(array $array): mixed
```

---

<a name="method-unshift"></a>

### unshift()
```php
arr::unshift(array $array, mixed $values): void
```

---

<a name="method-first"></a>

### first()
```php
arr::first(Traversable|array $collection): mixed
```

---

<a name="method-firstkey"></a>

### firstKey()
```php
arr::firstKey(Traversable|array $collection): string|int|null
```

---

<a name="method-last"></a>

### last()
```php
arr::last(array $collection): mixed
```
Alias to peek().

---

<a name="method-lastkey"></a>

### lastKey()
```php
arr::lastKey(array $collection): string|int|null
```

---

<a name="method-reverse"></a>

### reverse()
```php
arr::reverse(array $array): array
```

---

<a name="method-merge"></a>

### merge()
```php
arr::merge(array $array, array $others): array
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
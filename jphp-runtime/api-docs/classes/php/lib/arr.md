# arr

- **class** `arr` (`php\lib\arr`)
- **package** `std`
- **source** [`php/lib/arr.php`](./src/main/resources/JPHP-INF/sdk/php/lib/arr.php)

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

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

# Flow

- **class** `Flow` (`php\util\Flow`)
- **package** `std`
- **source** `php/util/Flow.php`

**Description**

A special class to work with arrays and iterators under flows.
Flows are used for the lazy array/iterator operations, to save the RAM memory.

Class Flow, Immutable

---

#### Static Methods

- `Flow ::`[`ofEmpty()`](#method-ofempty)
- `Flow ::`[`of()`](#method-of) - _Creates a new flow for an array of Iterator_
- `Flow ::`[`ofRange()`](#method-ofrange) - _Creates a new flow for a number range_
- `Flow ::`[`ofString()`](#method-ofstring) - _Creates a new flow for the string_
- `Flow ::`[`ofStream()`](#method-ofstream) - _Creates a new flow for the Stream object_

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Create a new flow, you can also use ``of()`` method_
- `->`[`withKeys()`](#method-withkeys) - _Enables to save keys for the next operation_
- `->`[`onlyKeys()`](#method-onlykeys)
- `->`[`append()`](#method-append) - _Appends a new collection to the current flow,_
- `->`[`anyMatch()`](#method-anymatch) - _Returns whether any elements of this stream match the provided_
- `->`[`allMatch()`](#method-allmatch) - _Returns whether all elements of this stream match the provided predicate._
- `->`[`noneMatch()`](#method-nonematch) - _Returns whether no elements of this stream match the provided predicate._
- `->`[`find()`](#method-find) - _Finds elements by using the $filter callback,_
- `->`[`findOne()`](#method-findone) - _Finds the first element by using the $filter callback,_
- `->`[`findValue()`](#method-findvalue)
- `->`[`group()`](#method-group)
- `->`[`each()`](#method-each) - _Iterates elements._
- `->`[`eachSlice()`](#method-eachslice) - _Iterates elements as slices (that are passing as arrays to $callback)._
- `->`[`map()`](#method-map) - _Iterates elements and returns a new flow of the result_
- `->`[`keys()`](#method-keys) - _Create a new flow by using the keys of the current flow_
- `->`[`skip()`](#method-skip) - _Skips $n elements in the current collection_
- `->`[`limit()`](#method-limit) - _Limits collection with $count_
- `->`[`reduce()`](#method-reduce) - _Iterates elements and gets a result of this operation_
- `->`[`max()`](#method-max) - _Get max of elements._
- `->`[`min()`](#method-min) - _Get min of elements._
- `->`[`avg()`](#method-avg) - _Get avg number of elements._
- `->`[`median()`](#method-median) - _Get median of elements._
- `->`[`numMedian()`](#method-nummedian)
- `->`[`sum()`](#method-sum) - _Get sum of elements._
- `->`[`concat()`](#method-concat) - _Get concatenation of all elements._
- `->`[`sort()`](#method-sort) - _Sort the last result of the flow, also see: ``php\\lib\\items::sort()``_
- `->`[`sortByKeys()`](#method-sortbykeys) - _The same method as ``sort()`` only based on keys insteadof values_
- `->`[`toArray()`](#method-toarray) - _Convert elements to an array_
- `->`[`toMap()`](#method-tomap) - _Convert element to an array with keys._
- `->`[`toString()`](#method-tostring) - _Join elements to a string similar to ``implode()`` in PHP_
- `->`[`count()`](#method-count)
- `->`[`current()`](#method-current)
- `->`[`next()`](#method-next)
- `->`[`key()`](#method-key)
- `->`[`valid()`](#method-valid)
- `->`[`rewind()`](#method-rewind)
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Static Methods

<a name="method-ofempty"></a>

### ofEmpty()
```php
Flow::ofEmpty(): php\util\Flow
```

---

<a name="method-of"></a>

### of()
```php
Flow::of(iterable $collection): php\util\Flow
```
Creates a new flow for an array of Iterator

---

<a name="method-ofrange"></a>

### ofRange()
```php
Flow::ofRange(int $from, int $to, int $step): php\util\Flow
```
Creates a new flow for a number range

---

<a name="method-ofstring"></a>

### ofString()
```php
Flow::ofString(string $string, int $chunkSize): php\util\Flow
```
Creates a new flow for the string

---

<a name="method-ofstream"></a>

### ofStream()
```php
Flow::ofStream(php\io\Stream $stream, int $chunkSize): php\util\Flow
```
Creates a new flow for the Stream object

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(iterable $collection): void
```
Create a new flow, you can also use ``of()`` method

---

<a name="method-withkeys"></a>

### withKeys()
```php
withKeys(): php\util\Flow
```
Enables to save keys for the next operation

---

<a name="method-onlykeys"></a>

### onlyKeys()
```php
onlyKeys(iterable $keys, bool $ignoreCase): php\util\Flow
```

---

<a name="method-append"></a>

### append()
```php
append(iterable $collection): php\util\Flow
```
Appends a new collection to the current flow,
do not remember that you can pass a flow to this method

---

<a name="method-anymatch"></a>

### anyMatch()
```php
anyMatch(callable $predicate): bool
```
Returns whether any elements of this stream match the provided
predicate.  May not evaluate the predicate on all elements if not
necessary for determining the result. If the flow is empty then
`false` is returned and the predicate is not evaluated.

---

<a name="method-allmatch"></a>

### allMatch()
```php
allMatch(callable $predicate): bool
```
Returns whether all elements of this stream match the provided predicate.
May not evaluate the predicate on all elements if not necessary for
determining the result. If the flow is empty then `true` is
returned and the predicate is not evaluated.

---

<a name="method-nonematch"></a>

### noneMatch()
```php
noneMatch(callable $predicate): bool
```
Returns whether no elements of this stream match the provided predicate.
May not evaluate the predicate on all elements if not necessary for
determining the result. If the flow is empty then `true` is
returned and the predicate is not evaluated.

---

<a name="method-find"></a>

### find()
```php
find(callable $filter): php\util\Flow
```
Finds elements by using the $filter callback,
elements - for each iteration that returns `true`

---

<a name="method-findone"></a>

### findOne()
```php
findOne(callable $filter): mixed
```
Finds the first element by using the $filter callback,
when $filter will return the first `true`

---

<a name="method-findvalue"></a>

### findValue()
```php
findValue(mixed $value, bool $strict): int|null|string
```

---

<a name="method-group"></a>

### group()
```php
group(callable $callback): php\util\Flow
```

---

<a name="method-each"></a>

### each()
```php
each(callable $callback): int
```
Iterates elements.
It will break if $callback returns ``false`` strongly

---

<a name="method-eachslice"></a>

### eachSlice()
```php
eachSlice(int $sliceSize, callable $callback, bool $withKeys): int
```
Iterates elements as slices (that are passing as arrays to $callback).
It will break if $callback returns ``false`` strongly

---

<a name="method-map"></a>

### map()
```php
map(callable $callback): php\util\Flow
```
Iterates elements and returns a new flow of the result
Example::

$newFlow = Flow::of([1,2,3])->map(function($el){  return $el * 10 });
// the new flow will contain 10, 20 and 30

---

<a name="method-keys"></a>

### keys()
```php
keys(): php\util\Flow
```
Create a new flow by using the keys of the current flow

---

<a name="method-skip"></a>

### skip()
```php
skip(int $n): php\util\Flow
```
Skips $n elements in the current collection

---

<a name="method-limit"></a>

### limit()
```php
limit(int $count): php\util\Flow
```
Limits collection with $count

---

<a name="method-reduce"></a>

### reduce()
```php
reduce(callable $callback): int
```
Iterates elements and gets a result of this operation
It can be used for calculate some results, for example::

// calculates a sum of elements
$sum = .. ->reduce(function($result, $el){  $result = $result + $el });

---

<a name="method-max"></a>

### max()
```php
max(callable|null $comparator): mixed
```
Get max of elements.

---

<a name="method-min"></a>

### min()
```php
min(callable|null $comparator): mixed
```
Get min of elements.

---

<a name="method-avg"></a>

### avg()
```php
avg(): int|float
```
Get avg number of elements.

---

<a name="method-median"></a>

### median()
```php
median(callable|null $comparator): mixed
```
Get median of elements.

---

<a name="method-nummedian"></a>

### numMedian()
```php
numMedian(callable|null $comparator): int|float
```

---

<a name="method-sum"></a>

### sum()
```php
sum(): int|float
```
Get sum of elements.

---

<a name="method-concat"></a>

### concat()
```php
concat(): string
```
Get concatenation of all elements.

---

<a name="method-sort"></a>

### sort()
```php
sort(callable $comparator): array
```
Sort the last result of the flow, also see: ``php\\lib\\items::sort()``

.. note:: use the ``withKeys()`` method to save keys

---

<a name="method-sortbykeys"></a>

### sortByKeys()
```php
sortByKeys(callable $comparator): array
```
The same method as ``sort()`` only based on keys insteadof values

.. note:: use the ``withKeys()`` method to save keys

---

<a name="method-toarray"></a>

### toArray()
```php
toArray([ bool|null $withKeys): array
```
Convert elements to an array

.. note:: use the ``withKeys()`` method to save keys

---

<a name="method-tomap"></a>

### toMap()
```php
toMap(): array
```
Convert element to an array with keys.

---

<a name="method-tostring"></a>

### toString()
```php
toString(string $separator): string
```
Join elements to a string similar to ``implode()`` in PHP

---

<a name="method-count"></a>

### count()
```php
count(): int
```

---

<a name="method-current"></a>

### current()
```php
current(): mixed
```

---

<a name="method-next"></a>

### next()
```php
next(): void
```

---

<a name="method-key"></a>

### key()
```php
key(): mixed
```

---

<a name="method-valid"></a>

### valid()
```php
valid(): bool
```

---

<a name="method-rewind"></a>

### rewind()
```php
rewind(): void
```

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
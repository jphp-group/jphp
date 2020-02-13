# arr

- **класс** `arr` (`php\lib\arr`)
- **пакет** `std`
- **исходники** `php/lib/arr.php`

**Классы наследники**

> [items](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/items.ru.md)

**Описание**

Библиотека для работы с коллекциями - массивы, итераторы и т.д.

---

#### Статичные Методы

- `arr ::`[`count()`](#method-count) - _Возвращает количество элементов коллекции_
- `arr ::`[`has()`](#method-has)
- `arr ::`[`toArray()`](#method-toarray) - _Конвертирует коллекцию в массив_
- `arr ::`[`of()`](#method-of) - _Alias of toArray()_
- `arr ::`[`toList()`](#method-tolist) - _Example: items::toList(['x' => 10, 20], 30, ['x' => 50, 60]) -> [10, 20, 30, 50, 60]_
- `arr ::`[`keys()`](#method-keys) - _Возвращает все ключи коллекции_
- `arr ::`[`values()`](#method-values) - _Возвращает все значения коллекции_
- `arr ::`[`combine()`](#method-combine) - _Combines two collections to array._
- `arr ::`[`map()`](#method-map)
- `arr ::`[`flatten()`](#method-flatten) - _Возвращает новый массив полученный исходя из всех элементов коллекции рекурсивно._
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

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-count"></a>

### count()
```php
arr::count(array|Countable|Iterator $collection): int
```
Возвращает количество элементов коллекции

.. warning:: для итераторов для подсчета количества требуется итерация по всем элементам

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
Конвертирует коллекцию в массив

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
Возвращает все ключи коллекции

---

<a name="method-values"></a>

### values()
```php
arr::values(array|Iterator $collection): array
```
Возвращает все значения коллекции

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
Возвращает новый массив полученный исходя из всех элементов коллекции рекурсивно.

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
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
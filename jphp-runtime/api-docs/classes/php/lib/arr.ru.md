# arr

- **класс** `arr` (`php\lib\arr`)
- **пакет** `std`
- **исходники** [`php/lib/arr.php`](./src/main/resources/JPHP-INF/sdk/php/lib/arr.php)

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

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

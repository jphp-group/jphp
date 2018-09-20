# MongoIterable

- **класс** `MongoIterable` (`mongo\MongoIterable`)
- **исходники** `mongo/MongoIterable.php`

**Описание**

Class MongoIterable

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`first()`](#method-first)
- `->`[`batchSize()`](#method-batchsize)
- `->`[`skip()`](#method-skip)
- `->`[`limit()`](#method-limit)
- `->`[`projection()`](#method-projection)
- `->`[`hint()`](#method-hint)
- `->`[`max()`](#method-max)
- `->`[`min()`](#method-min)
- `->`[`filter()`](#method-filter)
- `->`[`sort()`](#method-sort)
- `->`[`comment()`](#method-comment)
- `->`[`maxAwaitTime()`](#method-maxawaittime)
- `->`[`maxTime()`](#method-maxtime)
- `->`[`maxScan()`](#method-maxscan)
- `->`[`returnKey()`](#method-returnkey)
- `->`[`partial()`](#method-partial)
- `->`[`snapshot()`](#method-snapshot)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-first"></a>

### first()
```php
first(): array
```

---

<a name="method-batchsize"></a>

### batchSize()
```php
batchSize(int $size): mongo\MongoIterable
```

---

<a name="method-skip"></a>

### skip()
```php
skip(int $n): mongo\MongoIterable
```

---

<a name="method-limit"></a>

### limit()
```php
limit(int $n): mongo\MongoIterable
```

---

<a name="method-projection"></a>

### projection()
```php
projection(array $value): mongo\MongoIterable
```

---

<a name="method-hint"></a>

### hint()
```php
hint(array $value): mongo\MongoIterable
```

---

<a name="method-max"></a>

### max()
```php
max(array $value): mongo\MongoIterable
```

---

<a name="method-min"></a>

### min()
```php
min(array $value): mongo\MongoIterable
```

---

<a name="method-filter"></a>

### filter()
```php
filter(array $value): mongo\MongoIterable
```

---

<a name="method-sort"></a>

### sort()
```php
sort(array $value): mongo\MongoIterable
```

---

<a name="method-comment"></a>

### comment()
```php
comment(string $value): mongo\MongoIterable
```

---

<a name="method-maxawaittime"></a>

### maxAwaitTime()
```php
maxAwaitTime(int $millis): mongo\MongoIterable
```

---

<a name="method-maxtime"></a>

### maxTime()
```php
maxTime(int $millis): mongo\MongoIterable
```

---

<a name="method-maxscan"></a>

### maxScan()
```php
maxScan(int $c): mongo\MongoIterable
```

---

<a name="method-returnkey"></a>

### returnKey()
```php
returnKey(boolean $value): mongo\MongoIterable
```

---

<a name="method-partial"></a>

### partial()
```php
partial(boolean $value): mongo\MongoIterable
```

---

<a name="method-snapshot"></a>

### snapshot()
```php
snapshot(boolean $value): mongo\MongoIterable
```
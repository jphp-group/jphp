# Scanner

- **класс** `Scanner` (`php\util\Scanner`)
- **пакет** `std`
- **исходники** `php/util/Scanner.php`

**Описание**

A simple text scanner which can parse primitive types and strings using
regular expressions.

Class Scanner

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`hasNext()`](#method-hasnext)
- `->`[`next()`](#method-next)
- `->`[`nextLine()`](#method-nextline)
- `->`[`hasNextLine()`](#method-hasnextline)
- `->`[`nextInt()`](#method-nextint)
- `->`[`hasNextInt()`](#method-hasnextint)
- `->`[`nextDouble()`](#method-nextdouble)
- `->`[`hasNextDouble()`](#method-hasnextdouble)
- `->`[`skip()`](#method-skip)
- `->`[`useDelimiter()`](#method-usedelimiter)
- `->`[`useLocale()`](#method-uselocale)
- `->`[`useRadix()`](#method-useradix)
- `->`[`getIOException()`](#method-getioexception) - _Get the last io exception (if does not occur then returns ``null``)_
- `->`[`reset()`](#method-reset) - _Resets this scanner._
- `->`[`current()`](#method-current) - _Uses the result of the last called ``next()`` method_
- `->`[`key()`](#method-key)
- `->`[`valid()`](#method-valid)
- `->`[`rewind()`](#method-rewind) - _Iterator of the scanner can be used only one time_
- `->`[`__clone()`](#method-__clone) - _The scanner cannot be cloned_

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|Stream $source, string|null $charset): void
```

---

<a name="method-hasnext"></a>

### hasNext()
```php
hasNext(php\util\Regex $pattern): bool
```

---

<a name="method-next"></a>

### next()
```php
next(php\util\Regex $pattern): string|null
```

---

<a name="method-nextline"></a>

### nextLine()
```php
nextLine(): string|null
```

---

<a name="method-hasnextline"></a>

### hasNextLine()
```php
hasNextLine(): bool
```

---

<a name="method-nextint"></a>

### nextInt()
```php
nextInt(null|int $radix): int|null
```

---

<a name="method-hasnextint"></a>

### hasNextInt()
```php
hasNextInt(null|int $radix): bool
```

---

<a name="method-nextdouble"></a>

### nextDouble()
```php
nextDouble(): float|null
```

---

<a name="method-hasnextdouble"></a>

### hasNextDouble()
```php
hasNextDouble(): bool
```

---

<a name="method-skip"></a>

### skip()
```php
skip(php\util\Regex $pattern): bool
```

---

<a name="method-usedelimiter"></a>

### useDelimiter()
```php
useDelimiter(php\util\Regex $delimiter): Scanner
```

---

<a name="method-uselocale"></a>

### useLocale()
```php
useLocale(php\util\Locale $locale): Scanner
```

---

<a name="method-useradix"></a>

### useRadix()
```php
useRadix(int $value): Scanner
```

---

<a name="method-getioexception"></a>

### getIOException()
```php
getIOException(): IOException|null
```
Get the last io exception (if does not occur then returns ``null``)

---

<a name="method-reset"></a>

### reset()
```php
reset(): void
```
Resets this scanner.

Resetting a scanner discards all of its explicit state
information which may have been changed by invocations of
``useDelimiter()``, ``useLocale()``, or ``useRadix()``.

---

<a name="method-current"></a>

### current()
```php
current(): string
```
Uses the result of the last called ``next()`` method

---

<a name="method-key"></a>

### key()
```php
key(): int
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
Iterator of the scanner can be used only one time

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
The scanner cannot be cloned
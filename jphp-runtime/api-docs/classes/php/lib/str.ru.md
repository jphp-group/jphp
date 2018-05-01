# str

- **класс** `str` (`php\lib\str`)
- **пакет** `std`
- **исходники** `php/lib/Str.php`

**Описание**

Класс для работы со строками.

---

#### Статичные Методы

- `str ::`[`pos()`](#method-pos) - _Возвращает первую позицию найденной строки в подстроке, начиная_
- `str ::`[`posIgnoreCase()`](#method-posignorecase) - _Метод такой же как pos() только с игнорированием регистра символов._
- `str ::`[`lastPos()`](#method-lastpos) - _Возвращает позицию первой найденной подстроки в строке начиная с конца._
- `str ::`[`lastPosIgnoreCase()`](#method-lastposignorecase) - _Метод такой же как lastPos() только с игнорированием регистра символов._
- `str ::`[`sub()`](#method-sub) - _Возвращает подстроку из другой строки, которая укзывается через_
- `str ::`[`compare()`](#method-compare) - _Compares two strings lexicographically._
- `str ::`[`compareIgnoreCase()`](#method-compareignorecase) - _The same method as ``compare()`` only with ignoring case characters_
- `str ::`[`equalsIgnoreCase()`](#method-equalsignorecase) - _Checks that the strings are equal with ignoring case characters_
- `str ::`[`startsWith()`](#method-startswith) - _Tests if the substring of this string beginning at the_
- `str ::`[`endsWith()`](#method-endswith) - _Tests if this string ends with the specified suffix._
- `str ::`[`lower()`](#method-lower) - _Конвертирует все символы строки в нижний регистр и возвращает её._
- `str ::`[`upper()`](#method-upper) - _Конвертирует все символы строки в верхний регистр и возвращает её._
- `str ::`[`length()`](#method-length) - _Возвращает длину строки._
- `str ::`[`replace()`](#method-replace) - _Заменяет каждую подстроку на другую._
- `str ::`[`repeat()`](#method-repeat) - _Возвращает новую строку состояющую из повторов указанной строки._
- `str ::`[`trim()`](#method-trim) - _Возвращает копию строки без пробельных символов слева и справа._
- `str ::`[`trimRight()`](#method-trimright)
- `str ::`[`trimLeft()`](#method-trimleft)
- `str ::`[`reverse()`](#method-reverse) - _Возвращает перевернутую строку._
- `str ::`[`shuffle()`](#method-shuffle) - _Returns a randomized string based on chars in $string_
- `str ::`[`random()`](#method-random) - _Возвращает новую случайную строку основанную на наборе символов из $set._
- `str ::`[`split()`](#method-split) - _Разбивает строку на массив используя строку-разделитель $separator._
- `str ::`[`join()`](#method-join) - _Собирает массив в строку с разделителем $separator._
- `str ::`[`encode()`](#method-encode) - _Кодирует юникодную строку в любую другую кодировку и возвращает бинарную строку._
- `str ::`[`decode()`](#method-decode) - _Декодирует строку из указанной кодировке в родную юникодную строку._
- `str ::`[`isNumber()`](#method-isnumber) - _Returns true if $string is integer number (e.g: '12893', '3784', '0047')_
- `str ::`[`isLower()`](#method-islower)
- `str ::`[`isUpper()`](#method-isupper)
- `str ::`[`lowerFirst()`](#method-lowerfirst)
- `str ::`[`upperFirst()`](#method-upperfirst)
- `str ::`[`parseAs()`](#method-parseas) - _Parse text as json, ini, yaml, etc._
- `str ::`[`formatAs()`](#method-formatas) - _Format value as ini, json, yml, etc._
- `str ::`[`format()`](#method-format)
- `str ::`[`contains()`](#method-contains) - _Возвращает true если строка содержит укзанную подстроку._
- `str ::`[`count()`](#method-count) - _Возвращает количество найденный строк в другой строке._
- `str ::`[`uuid()`](#method-uuid)
- `str ::`[`hash()`](#method-hash) - _Возвращает хеш строки._
- `str ::`[`lines()`](#method-lines) - _Переводит многострочный текст в массив, если $removeEmpty true, то удаляет пустые строки из результата._

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-pos"></a>

### pos()
```php
str::pos(string $string, string $search, int $fromIndex): int
```
Возвращает первую позицию найденной строки в подстроке, начиная
с указанной позиции ($fromIndex). Если ничего не найдено, возвращает -1.

---

<a name="method-posignorecase"></a>

### posIgnoreCase()
```php
str::posIgnoreCase(string $string, string $search, int $fromIndex): int
```
Метод такой же как pos() только с игнорированием регистра символов.

---

<a name="method-lastpos"></a>

### lastPos()
```php
str::lastPos(string $string, string $search, null|int $fromIndex): int
```
Возвращает позицию первой найденной подстроки в строке начиная с конца.
Если ничего не найдено, возвращает -1.

---

<a name="method-lastposignorecase"></a>

### lastPosIgnoreCase()
```php
str::lastPosIgnoreCase(string $string, string $search, null|int $fromIndex): int
```
Метод такой же как lastPos() только с игнорированием регистра символов.

---

<a name="method-sub"></a>

### sub()
```php
str::sub(string $string, int $beginIndex, null|int $endIndex): string
```
Возвращает подстроку из другой строки, которая укзывается через
начальную позицию (включительно) $beginIndex и до конечной позиции (не включительно) $endIndex.
В итоге длина подстроки будет равна $endIndex - $beginIndex.

---

<a name="method-compare"></a>

### compare()
```php
str::compare(string $string1, string $string2): int
```
Compares two strings lexicographically.
The comparison is based on the Unicode value of each character in
the strings.

The character sequence represented by ``$string1``
``String`` is compared lexicographically to the
character sequence represented by ``$string2``. The result is
a negative integer if ``$string1``
lexicographically precedes ``$string2``. The result is a
positive integer if ``$string1`` lexicographically
follows ``$string2``. The result is zero if the strings
are equal; ``compare`` returns **0** exactly when
the strings are equal

---

<a name="method-compareignorecase"></a>

### compareIgnoreCase()
```php
str::compareIgnoreCase(string $string1, string $string2): int
```
The same method as ``compare()`` only with ignoring case characters

---

<a name="method-equalsignorecase"></a>

### equalsIgnoreCase()
```php
str::equalsIgnoreCase(string $string1, string $string2): bool
```
Checks that the strings are equal with ignoring case characters

---

<a name="method-startswith"></a>

### startsWith()
```php
str::startsWith(string $string, string $prefix, int $offset): bool
```
Tests if the substring of this string beginning at the
specified index starts with the specified prefix.

Returns true if the character sequence represented by the
argument is a prefix of the substring of this object starting
at index `offset`; `false` otherwise.
The result is `false` if `toffset` is
negative or greater than the length of this
`$string`; otherwise the result is the same
as the result of the expression

.. code-block:: php

startsWith(sub($offset), $prefix)

---

<a name="method-endswith"></a>

### endsWith()
```php
str::endsWith(string $string, string $suffix): bool
```
Tests if this string ends with the specified suffix.

---

<a name="method-lower"></a>

### lower()
```php
str::lower(string $string): string
```
Конвертирует все символы строки в нижний регистр и возвращает её.

---

<a name="method-upper"></a>

### upper()
```php
str::upper(string $string): string
```
Конвертирует все символы строки в верхний регистр и возвращает её.

---

<a name="method-length"></a>

### length()
```php
str::length(string $string): int
```
Возвращает длину строки.
Длина строки равна количеству юникодных единиц в строке.

---

<a name="method-replace"></a>

### replace()
```php
str::replace(string $string, string $target, string $replacement): string
```
Заменяет каждую подстроку на другую.

---

<a name="method-repeat"></a>

### repeat()
```php
str::repeat(string $string, int $amount): string
```
Возвращает новую строку состояющую из повторов указанной строки.

---

<a name="method-trim"></a>

### trim()
```php
str::trim(string $string, string $charList): string
```
Возвращает копию строки без пробельных символов слева и справа.

---

<a name="method-trimright"></a>

### trimRight()
```php
str::trimRight(string $string, string $charList): string
```

---

<a name="method-trimleft"></a>

### trimLeft()
```php
str::trimLeft(string $string, string $charList): string
```

---

<a name="method-reverse"></a>

### reverse()
```php
str::reverse(string $string): string
```
Возвращает перевернутую строку.

---

<a name="method-shuffle"></a>

### shuffle()
```php
str::shuffle(string $string): string
```
Returns a randomized string based on chars in $string

---

<a name="method-random"></a>

### random()
```php
str::random(int $length, string $set): string
```
Возвращает новую случайную строку основанную на наборе символов из $set.

---

<a name="method-split"></a>

### split()
```php
str::split(string $string, string $separator, int $limit): array
```
Разбивает строку на массив используя строку-разделитель $separator.
Аналог функции explode() из zend php.

---

<a name="method-join"></a>

### join()
```php
str::join(array|\Iterator $iterable, string $separator, int $limit): string
```
Собирает массив в строку с разделителем $separator.
Аналог функции implode() из zend php.

---

<a name="method-encode"></a>

### encode()
```php
str::encode(string $string, string $charset): string
```
Кодирует юникодную строку в любую другую кодировку и возвращает бинарную строку.

---

<a name="method-decode"></a>

### decode()
```php
str::decode(string $string, string $charset): string
```
Декодирует строку из указанной кодировке в родную юникодную строку.

---

<a name="method-isnumber"></a>

### isNumber()
```php
str::isNumber(string $string, bool $bigNumbers): bool
```
Returns true if $string is integer number (e.g: '12893', '3784', '0047')

- for ``123`` - true
- for ``00304`` - true
- for ``3389e4`` - false
- for ``3.49`` - false
- for ``23  `` - false

---

<a name="method-islower"></a>

### isLower()
```php
str::isLower(string $string): bool
```

---

<a name="method-isupper"></a>

### isUpper()
```php
str::isUpper(mixed $string): bool
```

---

<a name="method-lowerfirst"></a>

### lowerFirst()
```php
str::lowerFirst(string $string): string
```

---

<a name="method-upperfirst"></a>

### upperFirst()
```php
str::upperFirst(string $string): string
```

---

<a name="method-parseas"></a>

### parseAs()
```php
str::parseAs(string $string, string $format, int $flags): mixed
```
Parse text as json, ini, yaml, etc.

---

<a name="method-formatas"></a>

### formatAs()
```php
str::formatAs(mixed $input, string $format, int $flags): string
```
Format value as ini, json, yml, etc.

---

<a name="method-format"></a>

### format()
```php
str::format(string $string, mixed $args): string
```

---

<a name="method-contains"></a>

### contains()
```php
str::contains(string $string, string $search): bool
```
Возвращает true если строка содержит укзанную подстроку.

---

<a name="method-count"></a>

### count()
```php
str::count(string $string, string $subString, int $offset): int
```
Возвращает количество найденный строк в другой строке.

---

<a name="method-uuid"></a>

### uuid()
```php
str::uuid(null|string $value): string
```

---

<a name="method-hash"></a>

### hash()
```php
str::hash(string $string, string $algorithm): string
```
Возвращает хеш строки.

---

<a name="method-lines"></a>

### lines()
```php
str::lines(string $string, bool $removeEmpty): array
```
Переводит многострочный текст в массив, если $removeEmpty true, то удаляет пустые строки из результата.

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
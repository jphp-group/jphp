# str

- **class** `str` (`php\lib\str`)
- **package** `std`
- **source** `php/lib/Str.php`

**Description**

Class str

---

#### Static Methods

- `str ::`[`pos()`](#method-pos) - _Returns the index within this string of the first occurrence of the_
- `str ::`[`posIgnoreCase()`](#method-posignorecase) - _The same method as pos() only with ignoring case characters_
- `str ::`[`lastPos()`](#method-lastpos) - _Returns the index within this string of the last occurrence of the_
- `str ::`[`lastPosIgnoreCase()`](#method-lastposignorecase) - _The same method as ``lastPos()`` only with ignoring case characters_
- `str ::`[`sub()`](#method-sub) - _Returns a new string that is a substring of this string. The_
- `str ::`[`compare()`](#method-compare) - _Compares two strings lexicographically._
- `str ::`[`compareIgnoreCase()`](#method-compareignorecase) - _The same method as ``compare()`` only with ignoring case characters_
- `str ::`[`equalsIgnoreCase()`](#method-equalsignorecase) - _Checks that the strings are equal with ignoring case characters_
- `str ::`[`startsWith()`](#method-startswith) - _Tests if the substring of this string beginning at the_
- `str ::`[`endsWith()`](#method-endswith) - _Tests if this string ends with the specified suffix._
- `str ::`[`lower()`](#method-lower) - _Converts all of the characters in `$string` to lower_
- `str ::`[`upper()`](#method-upper) - _Converts all of the characters in ``$string`` to upper_
- `str ::`[`length()`](#method-length) - _Returns the length of ``$string``._
- `str ::`[`replace()`](#method-replace) - _Replaces each substring of this string that matches the literal target_
- `str ::`[`repeat()`](#method-repeat) - _Return s a new string consisting of the original ``$string`` repeated_
- `str ::`[`trim()`](#method-trim) - _Returns a copy of the string, with leading and trailing whitespace_
- `str ::`[`trimRight()`](#method-trimright)
- `str ::`[`trimLeft()`](#method-trimleft)
- `str ::`[`reverse()`](#method-reverse) - _Reverse the string._
- `str ::`[`shuffle()`](#method-shuffle) - _Returns a randomized string based on chars in $string_
- `str ::`[`random()`](#method-random) - _Returns a new random string based on set._
- `str ::`[`split()`](#method-split) - _The method like explode() in Zend PHP._
- `str ::`[`join()`](#method-join) - _The method like ``implode()`` in Zend PHP._
- `str ::`[`encode()`](#method-encode) - _Converts $string by using $charset and returns a binary string._
- `str ::`[`decode()`](#method-decode) - _Decodes $string by using $charset to UNICODE, returns a unicode string._
- `str ::`[`isNumber()`](#method-isnumber) - _Returns true if $string is integer number (e.g: '12893', '3784', '0047')_
- `str ::`[`isLower()`](#method-islower)
- `str ::`[`isUpper()`](#method-isupper)
- `str ::`[`lowerFirst()`](#method-lowerfirst)
- `str ::`[`upperFirst()`](#method-upperfirst)
- `str ::`[`parseAs()`](#method-parseas) - _Parse text as json, ini, yaml, etc._
- `str ::`[`formatAs()`](#method-formatas) - _Format value as ini, json, yml, etc._
- `str ::`[`format()`](#method-format)
- `str ::`[`contains()`](#method-contains) - _Returns true if string contains a substring._
- `str ::`[`count()`](#method-count) - _Возвращает количество найденный строк в другой строке._
- `str ::`[`uuid()`](#method-uuid)
- `str ::`[`hash()`](#method-hash) - _Возвращает хеш строки._
- `str ::`[`lines()`](#method-lines) - _Переводит многострочный текст в массив, если $removeEmpty true, то удаляет пустые строки из результата._

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-pos"></a>

### pos()
```php
str::pos(string $string, string $search, int $fromIndex): int
```
Returns the index within this string of the first occurrence of the
specified substring, starting at the specified index.

---

<a name="method-posignorecase"></a>

### posIgnoreCase()
```php
str::posIgnoreCase(string $string, string $search, int $fromIndex): int
```
The same method as pos() only with ignoring case characters

---

<a name="method-lastpos"></a>

### lastPos()
```php
str::lastPos(string $string, string $search, null|int $fromIndex): int
```
Returns the index within this string of the last occurrence of the
specified substring. The last occurrence of the empty string ""
is considered to occur at the index value $string.length.

---

<a name="method-lastposignorecase"></a>

### lastPosIgnoreCase()
```php
str::lastPosIgnoreCase(string $string, string $search, null|int $fromIndex): int
```
The same method as ``lastPos()`` only with ignoring case characters

---

<a name="method-sub"></a>

### sub()
```php
str::sub(string $string, int $beginIndex, null|int $endIndex): string
```
Returns a new string that is a substring of this string. The
substring begins at the specified ``$beginIndex`` and
extends to the character at index ``$endIndex`` - 1.
Thus the length of the substring is ``endIndex - beginIndex``.

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
Converts all of the characters in `$string` to lower
case using the rules of the default locale.

---

<a name="method-upper"></a>

### upper()
```php
str::upper(string $string): string
```
Converts all of the characters in ``$string`` to upper
case using the rules of the default locale.

---

<a name="method-length"></a>

### length()
```php
str::length(string $string): int
```
Returns the length of ``$string``.
The length is equal to the number of `Unicode code units` in the string.

---

<a name="method-replace"></a>

### replace()
```php
str::replace(string $string, string $target, string $replacement): string
```
Replaces each substring of this string that matches the literal target
sequence with the specified literal replacement sequence. The
replacement proceeds from the beginning of the string to the end, for
example, replacing "aa" with "b" in the string "aaa" will result in
"ba" rather than "ab".

---

<a name="method-repeat"></a>

### repeat()
```php
str::repeat(string $string, int $amount): string
```
Return s a new string consisting of the original ``$string`` repeated

---

<a name="method-trim"></a>

### trim()
```php
str::trim(string $string, string $charList): string
```
Returns a copy of the string, with leading and trailing whitespace
omitted.

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
Reverse the string.

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
Returns a new random string based on set.

---

<a name="method-split"></a>

### split()
```php
str::split(string $string, string $separator, int $limit): array
```
The method like explode() in Zend PHP.

---

<a name="method-join"></a>

### join()
```php
str::join(array|\Iterator $iterable, string $separator, int $limit): string
```
The method like ``implode()`` in Zend PHP.

---

<a name="method-encode"></a>

### encode()
```php
str::encode(string $string, string $charset): string
```
Converts $string by using $charset and returns a binary string.

---

<a name="method-decode"></a>

### decode()
```php
str::decode(string $string, string $charset): string
```
Decodes $string by using $charset to UNICODE, returns a unicode string.

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
Returns true if string contains a substring.

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
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
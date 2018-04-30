# str

- **class** `str` (`php\lib\str`)
- **package** `std`
- **source** [`php/lib/Str.php`](./src/main/resources/JPHP-INF/sdk/php/lib/Str.php)

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
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

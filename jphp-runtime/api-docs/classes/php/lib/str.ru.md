# str

- **класс** `str` (`php\lib\str`)
- **пакет** `std`
- **исходники** [`php/lib/Str.php`](./src/main/resources/JPHP-INF/sdk/php/lib/Str.php)

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
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

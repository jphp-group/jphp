# num

- **класс** `num` (`php\lib\num`)
- **пакет** `std`
- **исходники** `php/lib/num.php`

**Классы наследники**

> [number](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/number.ru.md)

**Описание**

Utils for numbers

Class num

---

#### Статичные Методы

- `num ::`[`compare()`](#method-compare) - _Compare two numbers_
- `num ::`[`toBin()`](#method-tobin) - _Returns a string representation of the $number_
- `num ::`[`toOctal()`](#method-tooctal) - _Returns a string representation of the $number_
- `num ::`[`toHex()`](#method-tohex) - _Returns a string representation of the $number_
- `num ::`[`toString()`](#method-tostring) - _Returns a string representation of the first argument in the_
- `num ::`[`reverse()`](#method-reverse) - _Returns the value obtained by reversing the order of the bits in the_
- `num ::`[`decode()`](#method-decode) - _Decodes a string into a integer._
- `num ::`[`format()`](#method-format)

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-compare"></a>

### compare()
```php
num::compare(int|double $num1, int|double $num2): int
```
Compare two numbers

.. note:: it can be used as comparator for number sorting

---

<a name="method-tobin"></a>

### toBin()
```php
num::toBin(int $number): string
```
Returns a string representation of the $number
argument as an unsigned integer in base 2.

---

<a name="method-tooctal"></a>

### toOctal()
```php
num::toOctal(int $number): string
```
Returns a string representation of the $number
argument as an unsigned integer in base 8.

---

<a name="method-tohex"></a>

### toHex()
```php
num::toHex(int $number): string
```
Returns a string representation of the $number
argument as an unsigned integer in base 16.

---

<a name="method-tostring"></a>

### toString()
```php
num::toString(int $number, int $radix): string
```
Returns a string representation of the first argument in the
radix specified by the second argument.

---

<a name="method-reverse"></a>

### reverse()
```php
num::reverse(int $number): int
```
Returns the value obtained by reversing the order of the bits in the
two's complement binary representation of the specified {@code long}
value.

---

<a name="method-decode"></a>

### decode()
```php
num::decode(string $string): string
```
Decodes a string into a integer.
Accepts decimal, hexadecimal, and octal numbers

---

<a name="method-format"></a>

### format()
```php
num::format(int|double $number, string $pattern, string $decSep, string $groupSep): string
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
# char

- **class** `char` (`php\lib\char`)
- **package** `std`
- **source** `php/lib/char.php`

**Description**

Char Utils for working with unicode chars
(using string[0] char)

Class char

---

#### Static Methods

- `char ::`[`of()`](#method-of)
- `char ::`[`ord()`](#method-ord)
- `char ::`[`count()`](#method-count) - _Determines the number of {@code char} values needed to_
- `char ::`[`compare()`](#method-compare)
- `char ::`[`lower()`](#method-lower)
- `char ::`[`upper()`](#method-upper)
- `char ::`[`title()`](#method-title)
- `char ::`[`isSpace()`](#method-isspace)
- `char ::`[`isDigit()`](#method-isdigit)
- `char ::`[`isLetter()`](#method-isletter)
- `char ::`[`isLetterOrDigit()`](#method-isletterordigit)
- `char ::`[`isLower()`](#method-islower)
- `char ::`[`isUpper()`](#method-isupper)
- `char ::`[`isTitle()`](#method-istitle)
- `char ::`[`isWhitespace()`](#method-iswhitespace)
- `char ::`[`isISOControl()`](#method-isisocontrol)
- `char ::`[`isDefined()`](#method-isdefined) - _Determines if a character is defined in Unicode._
- `char ::`[`isMirrored()`](#method-ismirrored) - _Determines whether the specified character (Unicode code point)_
- `char ::`[`isLowSurrogate()`](#method-islowsurrogate) - _Determines if the given $char value is a_
- `char ::`[`isHighSurrogate()`](#method-ishighsurrogate) - _Determines if the given $char value is a_
- `char ::`[`isPrintable()`](#method-isprintable)
- `char ::`[`number()`](#method-number) - _Returns the {@code int} value that the specified Unicode_

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-of"></a>

### of()
```php
char::of(int $code): string
```

---

<a name="method-ord"></a>

### ord()
```php
char::ord(string $char): int
```

---

<a name="method-count"></a>

### count()
```php
char::count(int $code): int
```
Determines the number of {@code char} values needed to
represent the specified character (Unicode code point). If the
specified character is equal to or greater than 0x10000, then
the method returns 2. Otherwise, the method returns 1.

---

<a name="method-compare"></a>

### compare()
```php
char::compare(string $char1, string $char2): int
```

---

<a name="method-lower"></a>

### lower()
```php
char::lower(string $char): string
```

---

<a name="method-upper"></a>

### upper()
```php
char::upper(string $char): string
```

---

<a name="method-title"></a>

### title()
```php
char::title(string $char): string
```

---

<a name="method-isspace"></a>

### isSpace()
```php
char::isSpace(string $char): bool
```

---

<a name="method-isdigit"></a>

### isDigit()
```php
char::isDigit(string $char): bool
```

---

<a name="method-isletter"></a>

### isLetter()
```php
char::isLetter(string $char): bool
```

---

<a name="method-isletterordigit"></a>

### isLetterOrDigit()
```php
char::isLetterOrDigit(string $char): bool
```

---

<a name="method-islower"></a>

### isLower()
```php
char::isLower(string $char): bool
```

---

<a name="method-isupper"></a>

### isUpper()
```php
char::isUpper(string $char): bool
```

---

<a name="method-istitle"></a>

### isTitle()
```php
char::isTitle(string $char): bool
```

---

<a name="method-iswhitespace"></a>

### isWhitespace()
```php
char::isWhitespace(string $char): bool
```

---

<a name="method-isisocontrol"></a>

### isISOControl()
```php
char::isISOControl(string $char): bool
```

---

<a name="method-isdefined"></a>

### isDefined()
```php
char::isDefined(string $char): bool
```
Determines if a character is defined in Unicode.

---

<a name="method-ismirrored"></a>

### isMirrored()
```php
char::isMirrored(string $char): bool
```
Determines whether the specified character (Unicode code point)
is mirrored according to the Unicode specification.  Mirrored
characters should have their glyphs horizontally mirrored when
displayed in text that is right-to-left.

---

<a name="method-islowsurrogate"></a>

### isLowSurrogate()
```php
char::isLowSurrogate(string $char): bool
```
Determines if the given $char value is a
<a href="http://www.unicode.org/glossary/#low_surrogate_code_unit">
Unicode low-surrogate code unit</a>
(also known as <i>trailing-surrogate code unit</i>).

---

<a name="method-ishighsurrogate"></a>

### isHighSurrogate()
```php
char::isHighSurrogate(string $char): bool
```
Determines if the given $char value is a
<a href="http://www.unicode.org/glossary/#high_surrogate_code_unit">
Unicode high-surrogate code unit</a>
(also known as <i>leading-surrogate code unit</i>).

---

<a name="method-isprintable"></a>

### isPrintable()
```php
char::isPrintable(string $char): bool
```

---

<a name="method-number"></a>

### number()
```php
char::number(string $char): int
```
Returns the {@code int} value that the specified Unicode
character represents.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
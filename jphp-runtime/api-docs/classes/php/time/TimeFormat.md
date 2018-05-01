# TimeFormat

- **class** `TimeFormat` (`php\time\TimeFormat`)
- **package** `std`
- **source** `php/time/TimeFormat.php`

**Description**

Class TimeFormat, Immutable

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`format()`](#method-format)
- `->`[`parse()`](#method-parse)
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $format, php\util\Locale $locale, array $formatSymbols): void
```

---

<a name="method-format"></a>

### format()
```php
format(php\time\Time $time): string
```

---

<a name="method-parse"></a>

### parse()
```php
parse(string $string, php\time\TimeZone $timeZone): Time|null
```

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
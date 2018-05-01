# Locale

- **класс** `Locale` (`php\util\Locale`)
- **пакет** `std`
- **исходники** `php/util/Locale.php`

**Описание**

Class Locale, Immutable

---

#### Статичные Методы

- `Locale ::`[`ENGLISH()`](#method-english)
- `Locale ::`[`US()`](#method-us)
- `Locale ::`[`UK()`](#method-uk)
- `Locale ::`[`CANADA()`](#method-canada)
- `Locale ::`[`CANADA_FRENCH()`](#method-canada_french)
- `Locale ::`[`FRENCH()`](#method-french)
- `Locale ::`[`FRANCE()`](#method-france)
- `Locale ::`[`ITALIAN()`](#method-italian)
- `Locale ::`[`ITALY()`](#method-italy)
- `Locale ::`[`GERMAN()`](#method-german)
- `Locale ::`[`GERMANY()`](#method-germany)
- `Locale ::`[`JAPAN()`](#method-japan)
- `Locale ::`[`JAPANESE()`](#method-japanese)
- `Locale ::`[`KOREA()`](#method-korea)
- `Locale ::`[`KOREAN()`](#method-korean)
- `Locale ::`[`CHINA()`](#method-china)
- `Locale ::`[`CHINESE()`](#method-chinese)
- `Locale ::`[`TAIWAN()`](#method-taiwan)
- `Locale ::`[`RUSSIAN()`](#method-russian)
- `Locale ::`[`RUSSIA()`](#method-russia)
- `Locale ::`[`ROOT()`](#method-root)
- `Locale ::`[`getDefault()`](#method-getdefault) - _Get default locale (if globally = false - only for the current environment)_
- `Locale ::`[`setDefault()`](#method-setdefault) - _Set default locale_
- `Locale ::`[`getAvailableLocales()`](#method-getavailablelocales) - _Returns an array of all installed locales._

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`getLanguage()`](#method-getlanguage)
- `->`[`getDisplayLanguage()`](#method-getdisplaylanguage)
- `->`[`getCountry()`](#method-getcountry)
- `->`[`getDisplayCountry()`](#method-getdisplaycountry)
- `->`[`getVariant()`](#method-getvariant)
- `->`[`getDisplayVariant()`](#method-getdisplayvariant)
- `->`[`getISO3Country()`](#method-getiso3country)
- `->`[`getISO3Language()`](#method-getiso3language)
- `->`[`__toString()`](#method-__tostring) - _Returns a string representation of this Locale_
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Статичные Методы

<a name="method-english"></a>

### ENGLISH()
```php
Locale::ENGLISH(): Locale
```

---

<a name="method-us"></a>

### US()
```php
Locale::US(): Locale
```

---

<a name="method-uk"></a>

### UK()
```php
Locale::UK(): Locale
```

---

<a name="method-canada"></a>

### CANADA()
```php
Locale::CANADA(): Locale
```

---

<a name="method-canada_french"></a>

### CANADA_FRENCH()
```php
Locale::CANADA_FRENCH(): Locale
```

---

<a name="method-french"></a>

### FRENCH()
```php
Locale::FRENCH(): Locale
```

---

<a name="method-france"></a>

### FRANCE()
```php
Locale::FRANCE(): Locale
```

---

<a name="method-italian"></a>

### ITALIAN()
```php
Locale::ITALIAN(): Locale
```

---

<a name="method-italy"></a>

### ITALY()
```php
Locale::ITALY(): Locale
```

---

<a name="method-german"></a>

### GERMAN()
```php
Locale::GERMAN(): Locale
```

---

<a name="method-germany"></a>

### GERMANY()
```php
Locale::GERMANY(): Locale
```

---

<a name="method-japan"></a>

### JAPAN()
```php
Locale::JAPAN(): Locale
```

---

<a name="method-japanese"></a>

### JAPANESE()
```php
Locale::JAPANESE(): Locale
```

---

<a name="method-korea"></a>

### KOREA()
```php
Locale::KOREA(): Locale
```

---

<a name="method-korean"></a>

### KOREAN()
```php
Locale::KOREAN(): Locale
```

---

<a name="method-china"></a>

### CHINA()
```php
Locale::CHINA(): Locale
```

---

<a name="method-chinese"></a>

### CHINESE()
```php
Locale::CHINESE(): Locale
```

---

<a name="method-taiwan"></a>

### TAIWAN()
```php
Locale::TAIWAN(): Locale
```

---

<a name="method-russian"></a>

### RUSSIAN()
```php
Locale::RUSSIAN(): Locale
```

---

<a name="method-russia"></a>

### RUSSIA()
```php
Locale::RUSSIA(): Locale
```

---

<a name="method-root"></a>

### ROOT()
```php
Locale::ROOT(): Locale
```

---

<a name="method-getdefault"></a>

### getDefault()
```php
Locale::getDefault(bool $globally): Locale
```
Get default locale (if globally = false - only for the current environment)

---

<a name="method-setdefault"></a>

### setDefault()
```php
Locale::setDefault(php\util\Locale $locale, bool $globally): void
```
Set default locale

---

<a name="method-getavailablelocales"></a>

### getAvailableLocales()
```php
Locale::getAvailableLocales(): Locale[]
```
Returns an array of all installed locales.
The returned array represents the union of locales supported
by the Java runtime environment

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $lang, string $country, string $variant): void
```

---

<a name="method-getlanguage"></a>

### getLanguage()
```php
getLanguage(): string
```

---

<a name="method-getdisplaylanguage"></a>

### getDisplayLanguage()
```php
getDisplayLanguage(php\util\Locale $locale): string
```

---

<a name="method-getcountry"></a>

### getCountry()
```php
getCountry(): string
```

---

<a name="method-getdisplaycountry"></a>

### getDisplayCountry()
```php
getDisplayCountry(php\util\Locale $locale): string
```

---

<a name="method-getvariant"></a>

### getVariant()
```php
getVariant(): string
```

---

<a name="method-getdisplayvariant"></a>

### getDisplayVariant()
```php
getDisplayVariant(php\util\Locale $locale): string
```

---

<a name="method-getiso3country"></a>

### getISO3Country()
```php
getISO3Country(): string
```

---

<a name="method-getiso3language"></a>

### getISO3Language()
```php
getISO3Language(): string
```

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```
Returns a string representation of this Locale
object, consisting of language, country, variant, script,
and extensions as below::

language + "_" + country + "_" + (variant + "_#" | "#") + script + "-" + extensions

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
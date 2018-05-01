# TimeZone

- **class** `TimeZone` (`php\time\TimeZone`)
- **package** `std`
- **source** `php/time/TimeZone.php`

**Description**

Class TimeZone, Immutable

---

#### Static Methods

- `TimeZone ::`[`UTC()`](#method-utc) - _Returns UTC Time zone_
- `TimeZone ::`[`of()`](#method-of)
- `TimeZone ::`[`setDefault()`](#method-setdefault) - _Set default time zone for Time objects, by default - the default timezone is UTC_
- `TimeZone ::`[`getDefault()`](#method-getdefault) - _Get default timezone_
- `TimeZone ::`[`getAvailableIDs()`](#method-getavailableids) - _Returns all available ids of timezones_

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`getId()`](#method-getid) - _Get id of the timezone_
- `->`[`getRawOffset()`](#method-getrawoffset) - _Get raw offset of the timezone_
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Static Methods

<a name="method-utc"></a>

### UTC()
```php
TimeZone::UTC(): TimeZone
```
Returns UTC Time zone

---

<a name="method-of"></a>

### of()
```php
TimeZone::of(string $ID): TimeZone
```

---

<a name="method-setdefault"></a>

### setDefault()
```php
TimeZone::setDefault(php\time\TimeZone $zone, bool $globally): void
```
Set default time zone for Time objects, by default - the default timezone is UTC

---

<a name="method-getdefault"></a>

### getDefault()
```php
TimeZone::getDefault(bool $globally): TimeZone
```
Get default timezone

---

<a name="method-getavailableids"></a>

### getAvailableIDs()
```php
TimeZone::getAvailableIDs(int|null $rawOffset): string[]
```
Returns all available ids of timezones

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $rawOffset, string $ID, array $options): TimeZone
```

---

<a name="method-getid"></a>

### getId()
```php
getId(): string
```
Get id of the timezone

---

<a name="method-getrawoffset"></a>

### getRawOffset()
```php
getRawOffset(): string
```
Get raw offset of the timezone

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
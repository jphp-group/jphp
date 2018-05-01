# Time

- **class** `Time` (`php\time\Time`)
- **package** `std`
- **source** `php/time/Time.php`

**Description**

Class Time, Immutable

---

#### Static Methods

- `Time ::`[`now()`](#method-now) - _Returns now time object (date + time)_
- `Time ::`[`today()`](#method-today) - _Returns today date (without time)_
- `Time ::`[`of()`](#method-of) - _Create a new time by using the $args arrays that can contain the ``sec``, ``min``, ``hour`` and other keys::_
- `Time ::`[`seconds()`](#method-seconds) - _Returns the current time in seconds (like the ``millis()`` method only in seconds)_
- `Time ::`[`millis()`](#method-millis) - _Returns the current time in milliseconds.  Note that_
- `Time ::`[`nanos()`](#method-nanos) - _Returns the current value of the running Java Virtual Machine's_

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Create a new time with unix timestamp_
- `->`[`getTime()`](#method-gettime) - _Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT_
- `->`[`getTimeZone()`](#method-gettimezone) - _Get timezone of the time object_
- `->`[`year()`](#method-year) - _Get the current year_
- `->`[`month()`](#method-month) - _Get the current month of the year, 1 - Jan, 12 - Dec_
- `->`[`week()`](#method-week) - _Get week of year_
- `->`[`weekOfMonth()`](#method-weekofmonth) - _Get week of month_
- `->`[`day()`](#method-day) - _Get day of year_
- `->`[`dayOfMonth()`](#method-dayofmonth) - _Get day of month_
- `->`[`dayOfWeek()`](#method-dayofweek) - _Get day of week_
- `->`[`dayOfWeekInMonth()`](#method-dayofweekinmonth)
- `->`[`hour()`](#method-hour) - _Get hour, indicating the hour of the morning or afternoon._
- `->`[`hourOfDay()`](#method-hourofday) - _Get hour of the day_
- `->`[`minute()`](#method-minute) - _Get minute of the hour_
- `->`[`second()`](#method-second) - _Get second of the minute_
- `->`[`millisecond()`](#method-millisecond) - _Get millisecond of the second_
- `->`[`compare()`](#method-compare) - _Compares the time values_
- `->`[`withTimeZone()`](#method-withtimezone)
- `->`[`withLocale()`](#method-withlocale)
- `->`[`add()`](#method-add) - _Get a new time + $args_
- `->`[`replace()`](#method-replace) - _Clones the current datetime and replaces some fields to new values $args_
- `->`[`toString()`](#method-tostring) - _Format the current datetime to string with $format_
- `->`[`__toString()`](#method-__tostring) - _Format the time to yyyy-MM-dd'T'HH:mm:ss_
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Static Methods

<a name="method-now"></a>

### now()
```php
Time::now(php\time\TimeZone $timeZone, php\util\Locale $locale): Time
```
Returns now time object (date + time)

---

<a name="method-today"></a>

### today()
```php
Time::today(php\time\TimeZone $timeZone, php\util\Locale $locale): Time
```
Returns today date (without time)

---

<a name="method-of"></a>

### of()
```php
Time::of(array $args, php\time\TimeZone $timeZone, php\util\Locale $locale): Time
```
Create a new time by using the $args arrays that can contain the ``sec``, ``min``, ``hour`` and other keys::

$time = Time::of(['year' => 2013, 'month' => 1, 'day' => 1]) // 01 Jan 2013

---

<a name="method-seconds"></a>

### seconds()
```php
Time::seconds(): int
```
Returns the current time in seconds (like the ``millis()`` method only in seconds)

---

<a name="method-millis"></a>

### millis()
```php
Time::millis(): int
```
Returns the current time in milliseconds.  Note that
while the unit of time of the return value is a millisecond,
the granularity of the value depends on the underlying
operating system and may be larger.  For example, many
operating systems measure time in units of tens of
milliseconds.

---

<a name="method-nanos"></a>

### nanos()
```php
Time::nanos(): int
```
Returns the current value of the running Java Virtual Machine's
high-resolution time source, in nanoseconds.

This method can only be used to measure elapsed time and is
not related to any other notion of system or wall-clock time.
The value returned represents nanoseconds since some fixed but
arbitrary *origin* time (perhaps in the future, so values
may be negative).  The same origin is used by all invocations of
this method in an instance of a Java virtual machine; other
virtual machine instances are likely to use a different origin

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $date, php\time\TimeZone $timezone, php\util\Locale $locale): void
```
Create a new time with unix timestamp

---

<a name="method-gettime"></a>

### getTime()
```php
getTime(): int
```
Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
represented by this Time object.

_Examples:_

- **Unix Timestamp**

```php
echo Time::now()->getTime() / 1000;
```


---

<a name="method-gettimezone"></a>

### getTimeZone()
```php
getTimeZone(): TimeZone
```
Get timezone of the time object

---

<a name="method-year"></a>

### year()
```php
year(): int
```
Get the current year

_Examples:_

- **Get current year**

```php
$now = Time::now();
echo $now->year();
```


---

<a name="method-month"></a>

### month()
```php
month(): int
```
Get the current month of the year, 1 - Jan, 12 - Dec

---

<a name="method-week"></a>

### week()
```php
week(): int
```
Get week of year

---

<a name="method-weekofmonth"></a>

### weekOfMonth()
```php
weekOfMonth(): int
```
Get week of month

---

<a name="method-day"></a>

### day()
```php
day(): int
```
Get day of year

---

<a name="method-dayofmonth"></a>

### dayOfMonth()
```php
dayOfMonth(): int
```
Get day of month

---

<a name="method-dayofweek"></a>

### dayOfWeek()
```php
dayOfWeek(): int
```
Get day of week

---

<a name="method-dayofweekinmonth"></a>

### dayOfWeekInMonth()
```php
dayOfWeekInMonth(): int
```

---

<a name="method-hour"></a>

### hour()
```php
hour(): int
```
Get hour, indicating the hour of the morning or afternoon.
hour() is used for the 12-hour clock (0 - 11). Noon and midnight are represented by 0, not by 12.

---

<a name="method-hourofday"></a>

### hourOfDay()
```php
hourOfDay(): int
```
Get hour of the day

---

<a name="method-minute"></a>

### minute()
```php
minute(): int
```
Get minute of the hour

---

<a name="method-second"></a>

### second()
```php
second(): int
```
Get second of the minute

---

<a name="method-millisecond"></a>

### millisecond()
```php
millisecond(): int
```
Get millisecond of the second

---

<a name="method-compare"></a>

### compare()
```php
compare(php\time\Time $time): int
```
Compares the time values

Returns the value ``0`` if the time represented by the argument
is equal to the time represented by this ``Time``; a value
less than ``0`` if the time of this ``Time`` is
before the time represented by the argument; and a value greater than
``0`` if the time of this ``Time`` is after the
time represented by the argument.

---

<a name="method-withtimezone"></a>

### withTimeZone()
```php
withTimeZone(php\time\TimeZone $timeZone): Time
```

---

<a name="method-withlocale"></a>

### withLocale()
```php
withLocale(php\util\Locale $locale): Time
```

---

<a name="method-add"></a>

### add()
```php
add(array $args): Time
```
Get a new time + $args

.. note::

use negative values to minus

_Examples:_

- **Add one year to date**

```php
$date = Time::of(['year' => 2018, 'month' => 5, 'day' => 1]);
echo $date->year();

$dayPlusYear = $date->add(['year' => 1]);
echo $dayPlusYear->year();
```

- **Decrease date by one day**

```php
$date = Time::of(['year' => 2018, 'month' => 5, 'day' => 1]);
$newDate = $date->add(['day' => -1]);

echo $newDate->toString('yyyy/MM/dd'); // 2018.04.30
```

---

<a name="method-replace"></a>

### replace()
```php
replace(array $args): Time
```
Clones the current datetime and replaces some fields to new values $args

---

<a name="method-tostring"></a>

### toString()
```php
toString(string $format, php\util\Locale $locale): string
```
Format the current datetime to string with $format

- G    Era designator    Text    AD
- y    Year    Year    1996; 96
- M    Month in year    Month    July; Jul; 07
- w    Week in year    Number    27
- W    Week in month    Number    2
- D    Day in year    Number    189
- d    Day in month    Number    10
- F    Day of week in month    Number    2
- E    Day in week    Text    Tuesday; Tue
- a    Am/pm marker    Text    PM
- H    Hour in day (0-23)    Number    0
- k    Hour in day (1-24)    Number    24
- K    Hour in am/pm (0-11)    Number    0
- h    Hour in am/pm (1-12)    Number    12
- m    Minute in hour    Number    30
- s    Second in minute    Number    55
- S    Millisecond    Number    978
- z    Time zone    General time zone    Pacific Standard Time; PST; GMT-08:00
- Z    Time zone    RFC 822 time zone    -0800

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```
Format the time to yyyy-MM-dd'T'HH:mm:ss

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
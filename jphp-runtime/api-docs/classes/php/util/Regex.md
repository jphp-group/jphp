# Regex

- **class** `Regex` (`php\util\Regex`)
- **package** `std`
- **source** `php/util/Regex.php`

**Description**

http://www.regular-expressions.info/java.html

Class Regex, Immutable

---

#### Static Methods

- `Regex ::`[`of()`](#method-of) - _Creates a new Regex of regex with $string and $flag_
- `Regex ::`[`match()`](#method-match) - _Tells whether or not this string matches the given regular expression._
- `Regex ::`[`split()`](#method-split) - _Splits this string around matches of the given regular expression._
- `Regex ::`[`quote()`](#method-quote) - _Returns a literal pattern ``String`` for the specified_
- `Regex ::`[`quoteReplacement()`](#method-quotereplacement) - _Returns a literal replacement ``String`` for the specified_

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Regex of $pattern and $flag_
- `->`[`getPattern()`](#method-getpattern) - _Get the current pattern._
- `->`[`getInput()`](#method-getinput) - _Get the input string._
- `->`[`getFlags()`](#method-getflags) - _Get the current flags_
- `->`[`test()`](#method-test) - _Executes a search for a match between a regular expression and a specified string. Returns true or false._
- `->`[`matches()`](#method-matches) - _Attempts to match the entire region against the pattern._
- `->`[`all()`](#method-all) - _Returns array of all found groups._
- `->`[`one()`](#method-one) - _Calls find() + groups() methods and returns the result of the groups() method._
- `->`[`first()`](#method-first) - _Alias of one() method._
- `->`[`last()`](#method-last) - _Finds the last match and returns last groups._
- `->`[`find()`](#method-find) - _Resets this matcher and then attempts to find the next subsequence of_
- `->`[`replace()`](#method-replace) - _Replaces every subsequence of the input sequence that matches the_
- `->`[`replaceFirst()`](#method-replacefirst) - _Replaces the first subsequence of the input sequence that matches the_
- `->`[`replaceGroup()`](#method-replacegroup)
- `->`[`replaceWithCallback()`](#method-replacewithcallback)
- `->`[`with()`](#method-with) - _Duplicates this pattern with a new $string_
- `->`[`withFlags()`](#method-withflags) - _Clone this object with the new $flags_
- `->`[`groups()`](#method-groups) - _Returns an array of all group._
- `->`[`group()`](#method-group) - _Returns the input subsequence captured by the given group during the_
- `->`[`groupNames()`](#method-groupnames) - _Returns group names._
- `->`[`getGroupCount()`](#method-getgroupcount) - _Returns the number of capturing groups in this matcher's pattern._
- `->`[`start()`](#method-start) - _Returns the start index of the previous match._
- `->`[`end()`](#method-end) - _Returns the offset after the last character matched._
- `->`[`hitEnd()`](#method-hitend) - _Returns true if the end of input was hit by the search engine in_
- `->`[`requireEnd()`](#method-requireend) - _Returns true if more input could change a positive match into a_
- `->`[`lookingAt()`](#method-lookingat) - _Attempts to match the input sequence, starting at the beginning of the_
- `->`[`region()`](#method-region) - _Sets the limits of this matcher's region. The region is the part of the_
- `->`[`regionStart()`](#method-regionstart) - _Reports the start index of this matcher's region. The_
- `->`[`regionEnd()`](#method-regionend) - _Reports the end index (exclusive) of this matcher's region._
- `->`[`reset()`](#method-reset) - _Resets this matcher._
- `->`[`current()`](#method-current)
- `->`[`next()`](#method-next)
- `->`[`key()`](#method-key)
- `->`[`valid()`](#method-valid)
- `->`[`rewind()`](#method-rewind)
- `->`[`__clone()`](#method-__clone) - _Class is immutable, the disallowed clone method_

---
# Static Methods

<a name="method-of"></a>

### of()
```php
Regex::of(string $pattern, int|string $flag, string $string): Regex
```
Creates a new Regex of regex with $string and $flag

---

<a name="method-match"></a>

### match()
```php
Regex::match(string $pattern, string $string, int|string $flags): bool
```
Tells whether or not this string matches the given regular expression.
See also java.lang.String.matches()

---

<a name="method-split"></a>

### split()
```php
Regex::split(string $pattern, string $string, int $limit): array
```
Splits this string around matches of the given regular expression.
See also java.lang.String.split()

---

<a name="method-quote"></a>

### quote()
```php
Regex::quote(string $string): string
```
Returns a literal pattern ``String`` for the specified
``String``.


This method produces a ``String`` that can be used to
create a ``Regex`` that would match the string
``$string`` as if it were a literal pattern. Metacharacters
or escape sequences in the input sequence will be given no special
meaning.

---

<a name="method-quotereplacement"></a>

### quoteReplacement()
```php
Regex::quoteReplacement(string $string): string
```
Returns a literal replacement ``String`` for the specified
``String``.

This method produces a ``String`` that will work
as a literal replacement $string in the
replaceWithCallback() method of the ``php\util\Regex`` class.
The ``String`` produced will match the sequence of characters
in $string treated as a literal sequence. Slashes ('\') and
dollar signs ('$') will be given no special meaning.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $pattern, int|string $flag, string $string): void
```
Regex of $pattern and $flag

---

<a name="method-getpattern"></a>

### getPattern()
```php
getPattern(): string
```
Get the current pattern.

---

<a name="method-getinput"></a>

### getInput()
```php
getInput(): string
```
Get the input string.

---

<a name="method-getflags"></a>

### getFlags()
```php
getFlags(): int
```
Get the current flags

---

<a name="method-test"></a>

### test()
```php
test(string $string): bool
```
Executes a search for a match between a regular expression and a specified string. Returns true or false.

---

<a name="method-matches"></a>

### matches()
```php
matches(): bool
```
Attempts to match the entire region against the pattern.

---

<a name="method-all"></a>

### all()
```php
all(int $start): array
```
Returns array of all found groups.

---

<a name="method-one"></a>

### one()
```php
one(int $start): array|null
```
Calls find() + groups() methods and returns the result of the groups() method.
Returns the first found groups, if founds nothing returns null.

---

<a name="method-first"></a>

### first()
```php
first(int $start): array
```
Alias of one() method.

---

<a name="method-last"></a>

### last()
```php
last(int $start): array
```
Finds the last match and returns last groups.

---

<a name="method-find"></a>

### find()
```php
find(int|null $start): bool
```
Resets this matcher and then attempts to find the next subsequence of
the input sequence that matches the pattern, starting at the specified
index.

---

<a name="method-replace"></a>

### replace()
```php
replace(string $replacement): string
```
Replaces every subsequence of the input sequence that matches the
pattern with the given replacement string.

This method first resets this matcher.  It then scans the input
sequence looking for matches of the pattern.  Characters that are not
part of any match are appended directly to the result string; each match
is replaced in the result by the replacement string.

---

<a name="method-replacefirst"></a>

### replaceFirst()
```php
replaceFirst(string $replacement): string
```
Replaces the first subsequence of the input sequence that matches the
pattern with the given replacement string.

---

<a name="method-replacegroup"></a>

### replaceGroup()
```php
replaceGroup(int $group, string $replacement): string
```

---

<a name="method-replacewithcallback"></a>

### replaceWithCallback()
```php
replaceWithCallback(callable $callback): string
```

---

<a name="method-with"></a>

### with()
```php
with(string $string): Regex
```
Duplicates this pattern with a new $string

---

<a name="method-withflags"></a>

### withFlags()
```php
withFlags(int|string $flags): Regex
```
Clone this object with the new $flags

---

<a name="method-groups"></a>

### groups()
```php
groups(): array
```
Returns an array of all group.

---

<a name="method-group"></a>

### group()
```php
group(null|int $group): string
```
Returns the input subsequence captured by the given group during the
previous match operation.

---

<a name="method-groupnames"></a>

### groupNames()
```php
groupNames(): array
```
Returns group names.

---

<a name="method-getgroupcount"></a>

### getGroupCount()
```php
getGroupCount(): int
```
Returns the number of capturing groups in this matcher's pattern.

---

<a name="method-start"></a>

### start()
```php
start(null|int $group): int
```
Returns the start index of the previous match.

---

<a name="method-end"></a>

### end()
```php
end(null|int $group): int
```
Returns the offset after the last character matched.

---

<a name="method-hitend"></a>

### hitEnd()
```php
hitEnd(): bool
```
Returns true if the end of input was hit by the search engine in
the last match operation performed by this matcher.

---

<a name="method-requireend"></a>

### requireEnd()
```php
requireEnd(): bool
```
Returns true if more input could change a positive match into a
negative one.

If this method returns true, and a match was found, then more
input could cause the match to be lost. If this method returns false
and a match was found, then more input might change the match but the
match won't be lost. If a match was not found, then requireEnd has no
meaning.

---

<a name="method-lookingat"></a>

### lookingAt()
```php
lookingAt(): bool
```
Attempts to match the input sequence, starting at the beginning of the
region, against the pattern.

---

<a name="method-region"></a>

### region()
```php
region(int $start, int $end): Regex
```
Sets the limits of this matcher's region. The region is the part of the
input sequence that will be searched to find a match. Invoking this
method resets the matcher, and then sets the region to start at the
index specified by the $start parameter and end at the
index specified by the $end parameter.

---

<a name="method-regionstart"></a>

### regionStart()
```php
regionStart(): int
```
Reports the start index of this matcher's region. The
searches this matcher conducts are limited to finding matches
within ``regionStart()`` (inclusive) and
``regionEnd()`` (exclusive).

---

<a name="method-regionend"></a>

### regionEnd()
```php
regionEnd(): int
```
Reports the end index (exclusive) of this matcher's region.
The searches this matcher conducts are limited to finding matches
within ``regionStart()`` (inclusive) and
``regionEnd()`` (exclusive).

---

<a name="method-reset"></a>

### reset()
```php
reset(null|string $string): $this
```
Resets this matcher.

Resetting a matcher discards all of its explicit state information
and sets its append position to zero. The matcher's region is set to the
default region, which is its entire character sequence. The anchoring
and transparency of this matcher's region boundaries are unaffected.

---

<a name="method-current"></a>

### current()
```php
current(): null|string
```

---

<a name="method-next"></a>

### next()
```php
next(): void
```

---

<a name="method-key"></a>

### key()
```php
key(): int
```

---

<a name="method-valid"></a>

### valid()
```php
valid(): bool
```

---

<a name="method-rewind"></a>

### rewind()
```php
rewind(): void
```

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Class is immutable, the disallowed clone method
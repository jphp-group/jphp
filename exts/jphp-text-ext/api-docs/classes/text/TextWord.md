# TextWord

- **class** `TextWord` (`text\TextWord`)
- **source** `text/TextWord.php`

**Description**

Class TextWord

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TextWord constructor._
- `->`[`__debugInfo()`](#method-__debuginfo)
- `->`[`length()`](#method-length) - _Get length of text._
- `->`[`capitalize()`](#method-capitalize) - _Capitalizes all the delimiter separated words in a String._
- `->`[`capitalizeFully()`](#method-capitalizefully) - _Converts all the delimiter separated words in a String into capitalized words,_
- `->`[`uncapitalize()`](#method-uncapitalize) - _Uncapitalizes all the whitespace separated words in a String._
- `->`[`wrap()`](#method-wrap) - _Wraps a single line of text, identifying words by ' '._
- `->`[`swapCase()`](#method-swapcase) - _Swaps the case of a String using a word based algorithm._
- `->`[`initials()`](#method-initials) - _Extracts the initial characters from each word in the String._
- `->`[`levenshteinDistance()`](#method-levenshteindistance) - _Find the Levenshtein distance between two Strings._
- `->`[`jaroWinklerDistance()`](#method-jarowinklerdistance) - _Find the Jaro Winkler Distance which indicates the similarity score_
- `->`[`jaccardDistance()`](#method-jaccarddistance) - _Calculates Jaccard distance of two set character sequence passed as_
- `->`[`hammingDistance()`](#method-hammingdistance) - _Find the Hamming Distance between two strings with the same_
- `->`[`cosineDistance()`](#method-cosinedistance) - _Measures the cosine distance between two character sequences._
- `->`[`fuzzyScore()`](#method-fuzzyscore) - _Find the Fuzzy Score which indicates the similarity score between two_
- `->`[`__toString()`](#method-__tostring) - _Return text of this._
- `->`[`__clone()`](#method-__clone)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $text): void
```
TextWord constructor.

---

<a name="method-__debuginfo"></a>

### __debugInfo()
```php
__debugInfo(): array
```

---

<a name="method-length"></a>

### length()
```php
length(): int
```
Get length of text.

---

<a name="method-capitalize"></a>

### capitalize()
```php
capitalize(string $delimiters): text\TextWord
```
Capitalizes all the delimiter separated words in a String.
Only the first character of each word is changed.

---

<a name="method-capitalizefully"></a>

### capitalizeFully()
```php
capitalizeFully(string|null $delimiters): text\TextWord
```
Converts all the delimiter separated words in a String into capitalized words,
that is each word is made up of a titlecase character and then a series of
lowercase characters.

---

<a name="method-uncapitalize"></a>

### uncapitalize()
```php
uncapitalize(string|null $delimiters): text\TextWord
```
Uncapitalizes all the whitespace separated words in a String.
Only the first character of each word is changed.

---

<a name="method-wrap"></a>

### wrap()
```php
wrap(int $lineLength, [ null|string $newLineString, bool $wrapLongWords): text\TextWord
```
Wraps a single line of text, identifying words by ' '.

Leading spaces on a new line are stripped.
Trailing spaces are not stripped.

---

<a name="method-swapcase"></a>

### swapCase()
```php
swapCase(): text\TextWord
```
Swaps the case of a String using a word based algorithm.

---

<a name="method-initials"></a>

### initials()
```php
initials(string|null $delimiters): text\TextWord
```
Extracts the initial characters from each word in the String.

Ben John Lee => BJL
Ben J.Lee => BJ
Ben J.Lee => with delimiters '. ' => BJL

---

<a name="method-levenshteindistance"></a>

### levenshteinDistance()
```php
levenshteinDistance(string $other, [ int|null $threshold): int|null
```
Find the Levenshtein distance between two Strings.
A higher score indicates a greater distance.

---

<a name="method-jarowinklerdistance"></a>

### jaroWinklerDistance()
```php
jaroWinklerDistance(string $other): float|null
```
Find the Jaro Winkler Distance which indicates the similarity score
between two CharSequences.

---

<a name="method-jaccarddistance"></a>

### jaccardDistance()
```php
jaccardDistance(string $other): float|null
```
Calculates Jaccard distance of two set character sequence passed as
input. Calculates Jaccard similarity and returns the complement of it.

---

<a name="method-hammingdistance"></a>

### hammingDistance()
```php
hammingDistance(string $other): int|null
```
Find the Hamming Distance between two strings with the same
length.

The distance starts with zero, and for each occurrence of a
different character in either String, it increments the distance
by 1, and finally return its value.

Since the Hamming Distance can only be calculated between strings of equal length, input of different lengths
will throw IllegalArgumentException

---

<a name="method-cosinedistance"></a>

### cosineDistance()
```php
cosineDistance(string $other): float|null
```
Measures the cosine distance between two character sequences.

For further explanation about Cosine Similarity and Cosine Distance, refer to
http://en.wikipedia.org/wiki/Cosine_similarity.

---

<a name="method-fuzzyscore"></a>

### fuzzyScore()
```php
fuzzyScore(string $score, [ php\util\Locale $locale): int
```
Find the Fuzzy Score which indicates the similarity score between two
Strings.

A matching algorithm that is similar to the searching algorithms implemented in editors such
as Sublime Text, TextMate, Atom and others.

One point is given for every matched character. Subsequent matches yield two bonus points. A higher score
indicates a higher similarity.

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```
Return text of this.

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
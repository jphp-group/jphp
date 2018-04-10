## jphp-text-ext

> Library work modify text, finding similar text and diff.

### How to use?

```php
use text\TextWord;

$text = new TextWord("your text");
echo $text->capitalize(); // output "Your Text";
```

### Hot to wrap text?

```
$text = new TextWord("the long text with attributes and chars etc");
echo $text->wrap(10, "\r\n"); // output multiline text each line with 10+ chars.
```

### Avaliable methods

1. `$text->capitalize()`.
2. `$text->capitalizeFully()`.
3. `$text->uncapitalize()`.
4. `$text->wrap()`.
5. `$text->initials()`.
6. .. etc, see in sdk directory.

### How to calculate levenshtein distance?

```php
use text\TextWord;

$text = new TextWord("your text");
$distance = $text->levenshteinDistance("your textt"); 
```

### How to calculate fuzzy score?

> Can be use for sorting based on query search string.

```php
use text\TextWord;

$text = new TextWord("your text");
$score = $text->fuzzyScore("yt"); // will 2.
```

# MarkdownOptions

- **класс** `MarkdownOptions` (`markdown\MarkdownOptions`)
- **исходники** `markdown/MarkdownOptions.php`

---

#### Методы

- `->`[`setRenderSoftBreak()`](#method-setrendersoftbreak)
- `->`[`setRenderHardBreak()`](#method-setrenderhardbreak)
- `->`[`setRenderEscapeHtml()`](#method-setrenderescapehtml)
- `->`[`setRenderSuppressHtml()`](#method-setrendersuppresshtml)
- `->`[`setRenderIndentSize()`](#method-setrenderindentsize)
- `->`[`addTableExtension()`](#method-addtableextension)
- `->`[`addAnchorLinkExtension()`](#method-addanchorlinkextension)
- `->`[`addWikiLinkExtension()`](#method-addwikilinkextension) - _Options are:_
- `->`[`addSubscriptExtension()`](#method-addsubscriptextension) - _Options are:_
- `->`[`addSuperscriptExtension()`](#method-addsuperscriptextension) - _Options are:_
- `->`[`addEmojiExtension()`](#method-addemojiextension) - _Options are:_

---
# Методы

<a name="method-setrendersoftbreak"></a>

### setRenderSoftBreak()
```php
setRenderSoftBreak(string $break): markdown\MarkdownOptions
```

---

<a name="method-setrenderhardbreak"></a>

### setRenderHardBreak()
```php
setRenderHardBreak(string $break): markdown\MarkdownOptions
```

---

<a name="method-setrenderescapehtml"></a>

### setRenderEscapeHtml()
```php
setRenderEscapeHtml(bool $value): markdown\MarkdownOptions
```

---

<a name="method-setrendersuppresshtml"></a>

### setRenderSuppressHtml()
```php
setRenderSuppressHtml(bool $value): markdown\MarkdownOptions
```

---

<a name="method-setrenderindentsize"></a>

### setRenderIndentSize()
```php
setRenderIndentSize(int $indent): markdown\MarkdownOptions
```

---

<a name="method-addtableextension"></a>

### addTableExtension()
```php
addTableExtension(): markdown\MarkdownOptions
```

---

<a name="method-addanchorlinkextension"></a>

### addAnchorLinkExtension()
```php
addAnchorLinkExtension(): markdown\MarkdownOptions
```

---

<a name="method-addwikilinkextension"></a>

### addWikiLinkExtension()
```php
addWikiLinkExtension(array $options): markdown\MarkdownOptions
```
Options are:
linkPrefix => string
linkPrefixAbsolute => string
imagePrefix => string
imagePrefixAbsolute => string
imageFileExtension => string
imageLinks => bool (def false)
disableRendering => bool (def false)
allowAnchors => bool (def false)
allowInlines => bool (def false)

---

<a name="method-addsubscriptextension"></a>

### addSubscriptExtension()
```php
addSubscriptExtension(array $options): markdown\MarkdownOptions
```
Options are:
htmlOpen => any string
htmlClose => any string.

---

<a name="method-addsuperscriptextension"></a>

### addSuperscriptExtension()
```php
addSuperscriptExtension(array $options): markdown\MarkdownOptions
```
Options are:
htmlOpen => any string
htmlClose => any string.

---

<a name="method-addemojiextension"></a>

### addEmojiExtension()
```php
addEmojiExtension(array $options): markdown\MarkdownOptions
```
Options are:
imageType => IMAGE_ONLY|UNICODE_FALLBACK_TO_IMAGE|UNICODE_ONLY
shortcutType => EMOJI_CHEAT_SHEET|GITHUB|ANY_EMOJI_CHEAT_SHEET_PREFERRED|ANY_GITHUB_PREFERRED
imagePath => any string (default /img/).
imageClass => any string (style css class).
imageSize => integer (default 20)
useImageUrls => bool (default false)
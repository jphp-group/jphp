# UXFont

- **class** `UXFont` (`php\gui\text\UXFont`)
- **package** `gui`
- **source** [`php/gui/text/UXFont.php`](./src/main/resources/JPHP-INF/sdk/php/gui/text/UXFont.php)

**Description**

Class UXFont

---

#### Properties

- `->`[`name`](#prop-name) : `string`
- `->`[`family`](#prop-family) : `string`
- `->`[`size`](#prop-size) : `int`
- `->`[`style`](#prop-style) : `string`
- `->`[`bold`](#prop-bold) : `bool`
- `->`[`italic`](#prop-italic) : `bool`
- `->`[`lineHeight`](#prop-lineheight) : `float`

---

#### Static Methods

- `UXFont ::`[`of()`](#method-of)
- `UXFont ::`[`load()`](#method-load)
- `UXFont ::`[`getDefault()`](#method-getdefault)
- `UXFont ::`[`getFontNames()`](#method-getfontnames)
- `UXFont ::`[`getFamilies()`](#method-getfamilies)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`withName()`](#method-withname)
- `->`[`withSize()`](#method-withsize)
- `->`[`withNameAndSize()`](#method-withnameandsize)
- `->`[`withBold()`](#method-withbold)
- `->`[`withThin()`](#method-withthin)
- `->`[`withItalic()`](#method-withitalic)
- `->`[`withoutItalic()`](#method-withoutitalic)
- `->`[`withRegular()`](#method-withregular)
- `->`[`calculateTextWidth()`](#method-calculatetextwidth)
- `->`[`generateStyle()`](#method-generatestyle) - _Generate CSS style of font._

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(double $size, string $family): void
```

---

<a name="method-withname"></a>

### withName()
```php
withName( $name): UXFont
```

---

<a name="method-withsize"></a>

### withSize()
```php
withSize( $size): UXFont
```

---

<a name="method-withnameandsize"></a>

### withNameAndSize()
```php
withNameAndSize( $name,  $size): void
```

---

<a name="method-withbold"></a>

### withBold()
```php
withBold(): UXFont
```

---

<a name="method-withthin"></a>

### withThin()
```php
withThin(): UXFont
```

---

<a name="method-withitalic"></a>

### withItalic()
```php
withItalic(): UXFont
```

---

<a name="method-withoutitalic"></a>

### withoutItalic()
```php
withoutItalic(): UXFont
```

---

<a name="method-withregular"></a>

### withRegular()
```php
withRegular(): UXFont
```

---

<a name="method-calculatetextwidth"></a>

### calculateTextWidth()
```php
calculateTextWidth(string $text): float
```

---

<a name="method-generatestyle"></a>

### generateStyle()
```php
generateStyle(): string
```
Generate CSS style of font.

---

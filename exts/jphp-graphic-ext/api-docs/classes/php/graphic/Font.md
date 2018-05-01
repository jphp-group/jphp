# Font

- **class** `Font` (`php\graphic\Font`)
- **package** `graphic`
- **source** `php/graphic/Font.php`

**Description**

Class Font

---

#### Properties

- `->`[`name`](#prop-name) : `string`
- `->`[`fontName`](#prop-fontname) : `string`
- `->`[`family`](#prop-family) : `string`
- `->`[`size`](#prop-size) : `int`
- `->`[`bold`](#prop-bold) : `bool`
- `->`[`italic`](#prop-italic) : `bool`
- `->`[`plain`](#prop-plain) : `bool`

---

#### Static Methods

- `Font ::`[`allFonts()`](#method-allfonts)
- `Font ::`[`register()`](#method-register) - _Registers a created Font in this GraphicsEnvironment._

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Font constructor._
- `->`[`canDisplay()`](#method-candisplay)
- `->`[`asItalic()`](#method-asitalic)
- `->`[`asBold()`](#method-asbold)
- `->`[`asWithSize()`](#method-aswithsize)

---
# Static Methods

<a name="method-allfonts"></a>

### allFonts()
```php
Font::allFonts(): Font[]
```

---

<a name="method-register"></a>

### register()
```php
Font::register(php\graphic\Font $font): bool
```
Registers a created Font in this GraphicsEnvironment.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $name, int $size, bool $bold, bool $italic): void
```
Font constructor.

---

<a name="method-candisplay"></a>

### canDisplay()
```php
canDisplay(string $char): bool
```

---

<a name="method-asitalic"></a>

### asItalic()
```php
asItalic(): php\graphic\Font
```

---

<a name="method-asbold"></a>

### asBold()
```php
asBold(): php\graphic\Font
```

---

<a name="method-aswithsize"></a>

### asWithSize()
```php
asWithSize(float $size): php\graphic\Font
```
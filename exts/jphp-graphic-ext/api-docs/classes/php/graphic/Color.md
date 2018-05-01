# Color

- **class** `Color` (`php\graphic\Color`)
- **package** `graphic`
- **source** `php/graphic/Color.php`

**Description**

Class Color

---

#### Properties

- `->`[`red`](#prop-red) : `int`
- `->`[`green`](#prop-green) : `int`
- `->`[`blue`](#prop-blue) : `int`
- `->`[`alpha`](#prop-alpha) : `int`
- `->`[`rgb`](#prop-rgb) : `int`

---

#### Static Methods

- `Color ::`[`ofRGB()`](#method-ofrgb)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Color constructor._
- `->`[`brighter()`](#method-brighter)
- `->`[`darker()`](#method-darker)
- `->`[`toHexString()`](#method-tohexstring)

---
# Static Methods

<a name="method-ofrgb"></a>

### ofRGB()
```php
Color::ofRGB(int $r, int $g, int $b, float $alpha): php\graphic\Color
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|int $value): void
```
Color constructor.

---

<a name="method-brighter"></a>

### brighter()
```php
brighter(): php\graphic\Color
```

---

<a name="method-darker"></a>

### darker()
```php
darker(): php\graphic\Color
```

---

<a name="method-tohexstring"></a>

### toHexString()
```php
toHexString(): string
```
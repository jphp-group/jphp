# ImageDraw

- **class** `ImageDraw` (`php\graphic\ImageDraw`)
- **package** `graphic`
- **source** `php/graphic/ImageDraw.php`

**Description**

Class ImageDraw

---

#### Properties

- `->`[`fill`](#prop-fill) : `Color|string`
- `->`[`font`](#prop-font) : [`Font`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-graphic-ext/api-docs/classes/php/graphic/Font.md)
- `->`[`antialiasing`](#prop-antialiasing) : `bool`
- `->`[`textAntialiasing`](#prop-textantialiasing) : `bool|string` - _GASP, LCD_*, true, false_

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _ImageDraw constructor._
- `->`[`clipRect()`](#method-cliprect)
- `->`[`clipEllipse()`](#method-clipellipse)
- `->`[`image()`](#method-image)
- `->`[`textSize()`](#method-textsize)
- `->`[`text()`](#method-text)
- `->`[`arc()`](#method-arc)
- `->`[`point()`](#method-point)
- `->`[`rect()`](#method-rect)
- `->`[`roundRect()`](#method-roundrect)
- `->`[`ellipse()`](#method-ellipse)
- `->`[`circle()`](#method-circle)
- `->`[`dispose()`](#method-dispose) - _Disposes of this graphics context and releases_
- `->`[`__destruct()`](#method-__destruct)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\graphic\Image $image): void
```
ImageDraw constructor.

---

<a name="method-cliprect"></a>

### clipRect()
```php
clipRect(float $x, float $y, float $width, float $height): void
```

---

<a name="method-clipellipse"></a>

### clipEllipse()
```php
clipEllipse(float $x, float $y, float $width, float $height): void
```

---

<a name="method-image"></a>

### image()
```php
image(int $x, int $y, php\graphic\Image $image): void
```

---

<a name="method-textsize"></a>

### textSize()
```php
textSize(string $text, array $options): float[]
```

---

<a name="method-text"></a>

### text()
```php
text(float $x, float $y, string $text, array $options): void
```

---

<a name="method-arc"></a>

### arc()
```php
arc(int $x, int $y, int $width, int $height, int $startAngle, int $arcAngle, array $options): void
```

---

<a name="method-point"></a>

### point()
```php
point(int $x, int $y, array $options): void
```

---

<a name="method-rect"></a>

### rect()
```php
rect(int $x, int $y, int $width, int $height, array $options): void
```

---

<a name="method-roundrect"></a>

### roundRect()
```php
roundRect(int $x, int $y, int $width, int $height, int $arcWidth, int $arcHeight, array $options): void
```

---

<a name="method-ellipse"></a>

### ellipse()
```php
ellipse(int $x, int $y, int $width, int $height, array $options): void
```

---

<a name="method-circle"></a>

### circle()
```php
circle(int $x, int $y, int $radius, array $options): void
```

---

<a name="method-dispose"></a>

### dispose()
```php
dispose(): void
```
Disposes of this graphics context and releases
any system resources that it is using.

---

<a name="method-__destruct"></a>

### __destruct()
```php
__destruct(): void
```
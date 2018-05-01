# Image

- **класс** `Image` (`php\graphic\Image`)
- **пакет** `graphic`
- **исходники** `php/graphic/Image.php`

**Описание**

Class Image

---

#### Свойства

- `->`[`width`](#prop-width) : `int`
- `->`[`height`](#prop-height) : `int`
- `->`[`size`](#prop-size) : `array`

---

#### Статичные Методы

- `Image ::`[`open()`](#method-open)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _Image constructor._
- `->`[`getPixel()`](#method-getpixel)
- `->`[`setPixel()`](#method-setpixel)
- `->`[`rotate()`](#method-rotate)
- `->`[`resize()`](#method-resize)
- `->`[`save()`](#method-save)
- `->`[`__clone()`](#method-__clone) - _Clonable._

---
# Статичные Методы

<a name="method-open"></a>

### open()
```php
Image::open(string|Stream $source): php\graphic\Image
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $width, int $height): void
```
Image constructor.

---

<a name="method-getpixel"></a>

### getPixel()
```php
getPixel(int $x, int $y): php\graphic\Color
```

---

<a name="method-setpixel"></a>

### setPixel()
```php
setPixel(int $x, int $y, php\graphic\Color $color): void
```

---

<a name="method-rotate"></a>

### rotate()
```php
rotate(float $angle): php\graphic\Image
```

---

<a name="method-resize"></a>

### resize()
```php
resize(int $width, int $height): php\graphic\Image
```

---

<a name="method-save"></a>

### save()
```php
save(mixed $dest, string $format): bool
```

---

<a name="method-__clone"></a>

### __clone()
```php
__clone(): void
```
Clonable.
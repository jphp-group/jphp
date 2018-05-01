# UXImage

- **class** `UXImage` (`php\gui\UXImage`)
- **package** `gui`
- **source** `php/gui/UXImage.php`

**Description**

Class UXImage

---

#### Properties

- `->`[`width`](#prop-width) : `double` - _Ширина картинки._
- `->`[`height`](#prop-height) : `double` - _Высота картинки._
- `->`[`progress`](#prop-progress) : `double` - _Прогресс загрузки._

---

#### Static Methods

- `UXImage ::`[`ofNative()`](#method-ofnative) - _Create from native image._
- `UXImage ::`[`ofUrl()`](#method-ofurl) - _Создает новую картинку из URL._

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`getPixelColor()`](#method-getpixelcolor) - _Возвращает цвет пикселя картинки._
- `->`[`getPixelARGB()`](#method-getpixelargb) - _Возвращает цвет пикселя картинки в формате argb._
- `->`[`cancel()`](#method-cancel) - _Отменяет загрузку картинки._
- `->`[`isError()`](#method-iserror)
- `->`[`isBackgroundLoading()`](#method-isbackgroundloading)
- `->`[`save()`](#method-save) - _Save image to file or stream in passed format, by default png._
- `->`[`toNative()`](#method-tonative) - _Convert to native image._

---
# Static Methods

<a name="method-ofnative"></a>

### ofNative()
```php
UXImage::ofNative(php\graphic\Image $image): php\gui\UXImage
```
Create from native image.

---

<a name="method-ofurl"></a>

### ofUrl()
```php
UXImage::ofUrl(string $url, bool $background): UXImage
```
Создает новую картинку из URL.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(Stream|string|Image $stream, mixed $requiredWidth, mixed $requiredHeight, bool $proportional): void
```

---

<a name="method-getpixelcolor"></a>

### getPixelColor()
```php
getPixelColor(int $x, int $y): UXColor
```
Возвращает цвет пикселя картинки.

---

<a name="method-getpixelargb"></a>

### getPixelARGB()
```php
getPixelARGB(int $x, int $y): int
```
Возвращает цвет пикселя картинки в формате argb.

---

<a name="method-cancel"></a>

### cancel()
```php
cancel(): void
```
Отменяет загрузку картинки.

---

<a name="method-iserror"></a>

### isError()
```php
isError(): bool
```

---

<a name="method-isbackgroundloading"></a>

### isBackgroundLoading()
```php
isBackgroundLoading(): bool
```

---

<a name="method-save"></a>

### save()
```php
save(string|Stream|File $to, string $format): void
```
Save image to file or stream in passed format, by default png.

---

<a name="method-tonative"></a>

### toNative()
```php
toNative(): php\graphic\Image
```
Convert to native image.
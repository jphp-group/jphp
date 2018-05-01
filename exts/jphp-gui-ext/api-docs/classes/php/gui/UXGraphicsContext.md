# UXGraphicsContext

- **class** `UXGraphicsContext` (`php\gui\UXGraphicsContext`)
- **package** `gui`
- **source** `php/gui/UXGraphicsContext.php`

**Description**

Class UXGraphicsContext

---

#### Properties

- `->`[`canvas`](#prop-canvas) : [`UXCanvas`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md)
- `->`[`font`](#prop-font) : [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.md)
- `->`[`globalAlpha`](#prop-globalalpha) : `float`
- `->`[`globalBlendMode`](#prop-globalblendmode) : `string` - _SRC_OVER, SRC_ATOP, ADD, MULTIPLY, SCREEN, OVERLAY, DARKEN, LIGHTEN, COLOR_DODGE, COLOR_BURN_
- `->`[`lineWidth`](#prop-linewidth) : `double`
- `->`[`lineCap`](#prop-linecap) : `string` - _SQUARE, BUTT, ROUND_
- `->`[`lineJoin`](#prop-linejoin) : `string` - _MITER, BEVEL, BEVEL_
- `->`[`miterLimit`](#prop-miterlimit) : `double`
- `->`[`fillColor`](#prop-fillcolor) : [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.md)
- `->`[`fillRule`](#prop-fillrule) : `string` - _EVEN_ODD, NON_ZERO_
- `->`[`strokeColor`](#prop-strokecolor) : [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.md)

---

#### Methods

- `->`[`beginPath()`](#method-beginpath) - _Resets the current path to empty._
- `->`[`moveTo()`](#method-moveto) - _Issues a move command for the current path to the given x,y coordinate._
- `->`[`lineTo()`](#method-lineto) - _Adds segments to the current path to make a line to the given x,y_
- `->`[`quadraticCurveTo()`](#method-quadraticcurveto) - _Adds segments to the current path to make a quadratic Bezier curve._
- `->`[`bezierCurveTo()`](#method-beziercurveto) - _Adds segments to the current path to make a cubic Bezier curve._
- `->`[`arcTo()`](#method-arcto) - _Adds segments to the current path to make an arc._
- `->`[`arc()`](#method-arc) - _Adds path elements to the current path to make an arc that uses Euclidean_
- `->`[`rect()`](#method-rect) - _Adds path elements to the current path to make a rectangle._
- `->`[`appendSVGPath()`](#method-appendsvgpath) - _Appends an SVG Path string to the current path. If there is no current_
- `->`[`closePath()`](#method-closepath) - _Closes the path._
- `->`[`fill()`](#method-fill) - _Fills the path with the current fill paint._
- `->`[`stroke()`](#method-stroke) - _Strokes the path with the current stroke paint._
- `->`[`clip()`](#method-clip) - _Intersects the current clip with the current path and applies it to_
- `->`[`isPointInPath()`](#method-ispointinpath) - _Returns true if the the given x,y point is inside the path._
- `->`[`clearRect()`](#method-clearrect) - _Clears a portion of the canvas with a transparent color value._
- `->`[`fillRect()`](#method-fillrect)
- `->`[`fillText()`](#method-filltext) - _Fills the given string of text at position x, y_
- `->`[`strokeText()`](#method-stroketext) - _Draws the given string of text at position x, y_
- `->`[`drawImage()`](#method-drawimage)
- `->`[`setFillColor()`](#method-setfillcolor)

---
# Methods

<a name="method-beginpath"></a>

### beginPath()
```php
beginPath(): void
```
Resets the current path to empty.
The default path is empty.

---

<a name="method-moveto"></a>

### moveTo()
```php
moveTo(double $x0, double $y0): void
```
Issues a move command for the current path to the given x,y coordinate.
The coordinates are transformed by the current transform as they are
added to the path and unaffected by subsequent changes to the transform.

---

<a name="method-lineto"></a>

### lineTo()
```php
lineTo(double $x1, double $y1): void
```
Adds segments to the current path to make a line to the given x,y
coordinate.

---

<a name="method-quadraticcurveto"></a>

### quadraticCurveTo()
```php
quadraticCurveTo(double $xc, double $yc, double $x1, double $y1): void
```
Adds segments to the current path to make a quadratic Bezier curve.

---

<a name="method-beziercurveto"></a>

### bezierCurveTo()
```php
bezierCurveTo(double $xc1, double $yc1, double $xc2, double $yc2, double $x1, double $y1): void
```
Adds segments to the current path to make a cubic Bezier curve.

---

<a name="method-arcto"></a>

### arcTo()
```php
arcTo(double $x1, double $y1, double $x2, double $y2, double $radius): void
```
Adds segments to the current path to make an arc.

---

<a name="method-arc"></a>

### arc()
```php
arc(double $centerX, double $centerY, double $radiusX, double $radiusY, double $startAngle, double $length): void
```
Adds path elements to the current path to make an arc that uses Euclidean
degrees. This Euclidean orientation sweeps from East to North, then West,
then South, then back to East.

---

<a name="method-rect"></a>

### rect()
```php
rect(double $x, double $y, double $w, double $h): void
```
Adds path elements to the current path to make a rectangle.

---

<a name="method-appendsvgpath"></a>

### appendSVGPath()
```php
appendSVGPath(string $svgpath): void
```
Appends an SVG Path string to the current path. If there is no current
path the string must then start with either type of move command.

---

<a name="method-closepath"></a>

### closePath()
```php
closePath(): void
```
Closes the path.

---

<a name="method-fill"></a>

### fill()
```php
fill(): void
```
Fills the path with the current fill paint.

---

<a name="method-stroke"></a>

### stroke()
```php
stroke(): void
```
Strokes the path with the current stroke paint.

---

<a name="method-clip"></a>

### clip()
```php
clip(): void
```
Intersects the current clip with the current path and applies it to
subsequent rendering operation as an anti-aliased mask.

---

<a name="method-ispointinpath"></a>

### isPointInPath()
```php
isPointInPath(double $x, double $y): bool
```
Returns true if the the given x,y point is inside the path.

---

<a name="method-clearrect"></a>

### clearRect()
```php
clearRect(double $x, double $y, double $w, double $h): void
```
Clears a portion of the canvas with a transparent color value.

---

<a name="method-fillrect"></a>

### fillRect()
```php
fillRect(double $x, double $y, double $w, double $h): void
```

---

<a name="method-filltext"></a>

### fillText()
```php
fillText(string $text, double $x, double $y, float|int $maxWidth): void
```
Fills the given string of text at position x, y
with the current fill paint attribute.

---

<a name="method-stroketext"></a>

### strokeText()
```php
strokeText(string $text, double $x, double $y, float|int $maxWidth): void
```
Draws the given string of text at position x, y
with the current fill paint attribute.

---

<a name="method-drawimage"></a>

### drawImage()
```php
drawImage(php\gui\UXImage $image, mixed $x, mixed $y, mixed $w, mixed $h, mixed $dx, mixed $dy, mixed $dw, mixed $dh): void
```

---

<a name="method-setfillcolor"></a>

### setFillColor()
```php
setFillColor(php\gui\paint\UXColor $color): void
```
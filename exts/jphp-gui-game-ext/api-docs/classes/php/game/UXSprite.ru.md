# UXSprite

- **класс** `UXSprite` (`php\game\UXSprite`)
- **пакет** `game`
- **исходники** `php/game/UXSprite.php`

**Описание**

Class UXSprite

---

#### Свойства

- `->`[`image`](#prop-image) : `UXImage`
- `->`[`frameSize`](#prop-framesize) : `double[] width + height`
- `->`[`frameWidth`](#prop-framewidth) : `double`
- `->`[`frameHeight`](#prop-frameheight) : `double`
- `->`[`frameCount`](#prop-framecount) : `int`
- `->`[`speed`](#prop-speed) : `int`
- `->`[`cycledAnimation`](#prop-cycledanimation) : `bool`
- `->`[`currentAnimation`](#prop-currentanimation) : `null|string`

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`setAnimation()`](#method-setanimation)
- `->`[`play()`](#method-play)
- `->`[`draw()`](#method-draw)
- `->`[`drawNext()`](#method-drawnext)
- `->`[`drawByTime()`](#method-drawbytime)
- `->`[`getFrameImage()`](#method-getframeimage)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\game\UXSprite $origin): void
```

---

<a name="method-setanimation"></a>

### setAnimation()
```php
setAnimation(string $name, int[] $indexes): void
```

---

<a name="method-play"></a>

### play()
```php
play(string $animation, int $speed): void
```

---

<a name="method-draw"></a>

### draw()
```php
draw(php\gui\UXCanvas $canvas, mixed $index): void
```

---

<a name="method-drawnext"></a>

### drawNext()
```php
drawNext(php\gui\UXCanvas $canvas): void
```

---

<a name="method-drawbytime"></a>

### drawByTime()
```php
drawByTime(php\gui\UXCanvas $canvas, int $now): void
```

---

<a name="method-getframeimage"></a>

### getFrameImage()
```php
getFrameImage(int $index): UXImage
```
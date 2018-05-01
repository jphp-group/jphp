# UXSpriteView

- **class** `UXSpriteView` (`php\game\UXSpriteView`) **extends** `UXCanvas` (`php\gui\UXCanvas`)
- **package** `game`
- **source** `php/game/UXSpriteView.php`

**Description**

Class UXSpriteView

---

#### Properties

- `->`[`sprite`](#prop-sprite) : [`UXSprite`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-game-ext/api-docs/classes/php/game/UXSprite.md) - _Спрайт._
- `->`[`animated`](#prop-animated) : `bool` - _Анимирован._
- `->`[`animationName`](#prop-animationname) : `null|string` - _Анимация._
- `->`[`animationSpeed`](#prop-animationspeed) : `int` - _Скорость анимации._
- `->`[`frame`](#prop-frame) : `int` - _Кадр спрайта._
- `->`[`frameCount`](#prop-framecount) : `int` - _Количество кадров._
- `->`[`flipX`](#prop-flipx) : `bool` - _Отразить спрайт по X._
- `->`[`flipY`](#prop-flipy) : `bool` - _Отразить спрайт по Y._

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXSpriteView constructor._
- `->`[`play()`](#method-play) - _Проигрывать анимацию циклично._
- `->`[`playOnce()`](#method-playonce) - _Проиграть анимацию единожды._
- `->`[`pause()`](#method-pause)
- `->`[`resume()`](#method-resume)
- `->`[`isPaused()`](#method-ispaused)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\game\UXSprite $sprite): void
```
UXSpriteView constructor.

---

<a name="method-play"></a>

### play()
```php
play(string $animation, int $speed): void
```
Проигрывать анимацию циклично.

---

<a name="method-playonce"></a>

### playOnce()
```php
playOnce(string $animation, int $speed): void
```
Проиграть анимацию единожды.

---

<a name="method-pause"></a>

### pause()
```php
pause(): void
```

---

<a name="method-resume"></a>

### resume()
```php
resume(): void
```

---

<a name="method-ispaused"></a>

### isPaused()
```php
isPaused(): bool
```
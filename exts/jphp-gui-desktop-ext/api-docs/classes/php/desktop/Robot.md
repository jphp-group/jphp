# Robot

- **class** `Robot` (`php\desktop\Robot`)
- **source** `php/desktop/Robot.php`

**Description**

Class Robot

---

#### Properties

- `->`[`x`](#prop-x) : `int` - _Mouse X_
- `->`[`y`](#prop-y) : `int` - _Mouse Y_
- `->`[`position`](#prop-position) : `int[]` - _Mouse X, Y_

---

#### Methods

- `->`[`mouseClick()`](#method-mouseclick)
- `->`[`mouseDown()`](#method-mousedown)
- `->`[`mouseUp()`](#method-mouseup)
- `->`[`mouseScroll()`](#method-mousescroll)
- `->`[`type()`](#method-type)
- `->`[`keyDown()`](#method-keydown)
- `->`[`keyUp()`](#method-keyup)
- `->`[`keyPress()`](#method-keypress)
- `->`[`screenshot()`](#method-screenshot) - _Make screen shot of screen (primary if null passed)._

---
# Methods

<a name="method-mouseclick"></a>

### mouseClick()
```php
mouseClick(string $button): void
```

---

<a name="method-mousedown"></a>

### mouseDown()
```php
mouseDown(string $button): void
```

---

<a name="method-mouseup"></a>

### mouseUp()
```php
mouseUp(string $button): void
```

---

<a name="method-mousescroll"></a>

### mouseScroll()
```php
mouseScroll(int $wheelAmt): void
```

---

<a name="method-type"></a>

### type()
```php
type(string $chars): void
```

---

<a name="method-keydown"></a>

### keyDown()
```php
keyDown(string $keyCombination): void
```

---

<a name="method-keyup"></a>

### keyUp()
```php
keyUp(string $keyCombination): void
```

---

<a name="method-keypress"></a>

### keyPress()
```php
keyPress(string $keyCombination): void
```

---

<a name="method-screenshot"></a>

### screenshot()
```php
screenshot(array $bounds, php\gui\UXScreen $screen): UXImage
```
Make screen shot of screen (primary if null passed).
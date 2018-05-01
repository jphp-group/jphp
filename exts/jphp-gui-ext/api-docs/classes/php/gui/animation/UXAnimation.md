# UXAnimation

- **class** `UXAnimation` (`php\gui\animation\UXAnimation`)
- **package** `gui`
- **source** `php/gui/animation/UXAnimation.php`

**Child Classes**

> [UXFadeAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXFadeAnimation.md), [UXPathAnimation](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXPathAnimation.md), [UXTimeline](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXTimeline.md)

**Description**

Abstract animation class.

---

#### Properties

- `->`[`status`](#prop-status) : `string PAUSED, RUNNING, STOPPED`
- `->`[`rate`](#prop-rate) : `double`
- `->`[`currentRate`](#prop-currentrate) : `double`
- `->`[`cycleCount`](#prop-cyclecount) : `int`
- `->`[`targetFramerate`](#prop-targetframerate) : `double`
- `->`[`autoReverse`](#prop-autoreverse) : `bool`
- `->`[`currentTime`](#prop-currenttime) : `int millis`
- `->`[`cycleDuration`](#prop-cycleduration) : `int millis`
- `->`[`totalDuration`](#prop-totalduration) : `int millis`
- `->`[`delay`](#prop-delay) : `int millis`

---

#### Methods

- `->`[`play()`](#method-play)
- `->`[`playFromStart()`](#method-playfromstart)
- `->`[`playFrom()`](#method-playfrom)
- `->`[`jumpTo()`](#method-jumpto)
- `->`[`stop()`](#method-stop)
- `->`[`pause()`](#method-pause)
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)

---
# Methods

<a name="method-play"></a>

### play()
```php
play(): void
```

---

<a name="method-playfromstart"></a>

### playFromStart()
```php
playFromStart(): void
```

---

<a name="method-playfrom"></a>

### playFrom()
```php
playFrom(int $millis): void
```

---

<a name="method-jumpto"></a>

### jumpTo()
```php
jumpTo(int $millis): void
```

---

<a name="method-stop"></a>

### stop()
```php
stop(): void
```

---

<a name="method-pause"></a>

### pause()
```php
pause(): void
```

---

<a name="method-on"></a>

### on()
```php
on(string $event, callable $handler, string $group): void
```

---

<a name="method-off"></a>

### off()
```php
off(string $event, string $group): void
```

---

<a name="method-trigger"></a>

### trigger()
```php
trigger(string $event, php\gui\animation\UXEvent $e): void
```
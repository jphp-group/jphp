# UXMediaPlayer

- **class** `UXMediaPlayer` (`php\gui\UXMediaPlayer`)
- **package** `gui`
- **source** `php/gui/UXMediaPlayer.php`

**Description**

Class UXMediaPlayer

---

#### Properties

- `->`[`balance`](#prop-balance) : `double form -1.0 to 1.0`
- `->`[`rate`](#prop-rate) : `double from 0.0 to 8.0`
- `->`[`volume`](#prop-volume) : `double`
- `->`[`mute`](#prop-mute) : `bool`
- `->`[`cycleCount`](#prop-cyclecount) : `int -1 is`
- `->`[`status`](#prop-status) : `string` - _UNKNOWN, READY, PAUSED, PLAYING, STOPPED, STALLED, HALTED, DISPOSED_
- `->`[`currentRate`](#prop-currentrate) : `double`
- `->`[`currentTime`](#prop-currenttime) : `int in millis`
- `->`[`currentTimeAsPercent`](#prop-currenttimeaspercent) : `int in percent from 0 to 100`
- `->`[`currentCount`](#prop-currentcount) : `int`
- `->`[`media`](#prop-media) : [`UXMedia`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMedia.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`play()`](#method-play)
- `->`[`pause()`](#method-pause)
- `->`[`stop()`](#method-stop)
- `->`[`seek()`](#method-seek)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXMedia $media): void
```

---

<a name="method-play"></a>

### play()
```php
play(): void
```

---

<a name="method-pause"></a>

### pause()
```php
pause(): void
```

---

<a name="method-stop"></a>

### stop()
```php
stop(): void
```

---

<a name="method-seek"></a>

### seek()
```php
seek(mixed $time): void
```
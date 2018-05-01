# TrayIcon

- **class** `TrayIcon` (`php\desktop\TrayIcon`)
- **source** `php/desktop/TrayIcon.php`

**Description**

Class TrayIcon

---

#### Properties

- `->`[`image`](#prop-image) : `UXImage`
- `->`[`imageAutoSize`](#prop-imageautosize) : `bool`
- `->`[`tooltip`](#prop-tooltip) : `string`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _TrayIcon constructor._
- `->`[`displayMessage()`](#method-displaymessage) - _Displays a popup message near the tray icon.  The message will_
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXImage $image): void
```
TrayIcon constructor.

---

<a name="method-displaymessage"></a>

### displayMessage()
```php
displayMessage(string $title, string $text, string $type): void
```
Displays a popup message near the tray icon.  The message will
disappear after a time or if the user clicks on it.  Clicking
on the message may trigger an ActionEvent.

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
trigger(string $event, php\gui\event\UXEvent $e): void
```
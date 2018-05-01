# UXTrayNotification

- **класс** `UXTrayNotification` (`php\gui\UXTrayNotification`)
- **пакет** `gui`
- **исходники** `php/gui/UXTrayNotification.php`

**Описание**

Class UXTrayNotification

---

#### Свойства

- `->`[`title`](#prop-title) : `string`
- `->`[`message`](#prop-message) : `string`
- `->`[`notificationType`](#prop-notificationtype) : `string INFORMATION, NOTICE, WARNING, SUCCESS, ERROR, CUSTOM`
- `->`[`animationType`](#prop-animationtype) : `string FADE, SLIDE, POPUP`
- `->`[`location`](#prop-location) : `string BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, TOP_RIGHT`
- `->`[`image`](#prop-image) : [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.ru.md)
- `->`[`trayIcon`](#prop-trayicon) : [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.ru.md)
- `->`[`horGap`](#prop-horgap) : `int`
- `->`[`verGap`](#prop-vergap) : `int`

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`show()`](#method-show)
- `->`[`showAndWait()`](#method-showandwait) - _..._
- `->`[`hide()`](#method-hide)
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $title, string $message, string $type): void
```

---

<a name="method-show"></a>

### show()
```php
show(int $delay): void
```

---

<a name="method-showandwait"></a>

### showAndWait()
```php
showAndWait(): void
```
...

---

<a name="method-hide"></a>

### hide()
```php
hide(): void
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
trigger(string $event, php\gui\event\UXEvent $e): void
```
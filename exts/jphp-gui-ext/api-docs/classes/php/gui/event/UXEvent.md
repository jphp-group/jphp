# UXEvent

- **class** `UXEvent` (`php\gui\event\UXEvent`)
- **package** `gui`
- **source** `php/gui/event/UXEvent.php`

**Child Classes**

> [UXContextMenuEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXContextMenuEvent.md), [UXDragEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXDragEvent.md), [UXKeyEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyEvent.md), [UXMouseEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXMouseEvent.md), [UXScrollEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXScrollEvent.md), [UXWebErrorEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebErrorEvent.md), [UXWebEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebEvent.md), [UXWindowEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWindowEvent.md)

**Description**

Class Event

---

#### Properties

- `->`[`sender`](#prop-sender) : `UXNode|UXWindow`
- `->`[`target`](#prop-target) : `object|UXNode`

---

#### Static Methods

- `UXEvent ::`[`makeMock()`](#method-makemock)

---

#### Methods

- `->`[`copyFor()`](#method-copyfor)
- `->`[`isConsumed()`](#method-isconsumed)
- `->`[`consume()`](#method-consume) - _..._

---
# Static Methods

<a name="method-makemock"></a>

### makeMock()
```php
UXEvent::makeMock(mixed $sender): UXEvent
```

---
# Methods

<a name="method-copyfor"></a>

### copyFor()
```php
copyFor(object $newSender): UXEvent
```

---

<a name="method-isconsumed"></a>

### isConsumed()
```php
isConsumed(): bool
```

---

<a name="method-consume"></a>

### consume()
```php
consume(): void
```
...
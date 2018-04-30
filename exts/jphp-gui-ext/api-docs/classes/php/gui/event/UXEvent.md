# UXEvent

- **class** `UXEvent` (`php\gui\event\UXEvent`)
- **package** `gui`
- **source** [`php/gui/event/UXEvent.php`](./src/main/resources/JPHP-INF/sdk/php/gui/event/UXEvent.php)

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
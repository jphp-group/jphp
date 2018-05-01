# UXCollisionEvent

- **class** `UXCollisionEvent` (`php\game\event\UXCollisionEvent`) **extends** `UXEvent` (`php\gui\event\UXEvent`)
- **package** `game`
- **source** `php/game/event/UXCollisionEvent.php`

**Description**

->sender first object
->other second object

Class UXCollisionEvent

---

#### Properties

- `->`[`normal`](#prop-normal) : `float[]`
- `->`[`tangent`](#prop-tangent) : `float[]`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXCollisionEvent constructor._

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\game\event\UXCollisionEvent $parent, mixed $sender, mixed $target): void
```
UXCollisionEvent constructor.
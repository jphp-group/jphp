# UXCollisionEvent

- **класс** `UXCollisionEvent` (`php\game\event\UXCollisionEvent`) **унаследован от** `UXEvent` (`php\gui\event\UXEvent`)
- **пакет** `game`
- **исходники** `php/game/event/UXCollisionEvent.php`

**Описание**

->sender first object
->other second object

Class UXCollisionEvent

---

#### Свойства

- `->`[`normal`](#prop-normal) : `float[]`
- `->`[`tangent`](#prop-tangent) : `float[]`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXCollisionEvent constructor._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\game\event\UXCollisionEvent $parent, mixed $sender, mixed $target): void
```
UXCollisionEvent constructor.
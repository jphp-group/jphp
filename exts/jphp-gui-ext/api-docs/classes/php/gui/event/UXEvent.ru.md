# UXEvent

- **класс** `UXEvent` (`php\gui\event\UXEvent`)
- **пакет** `gui`
- **исходники** `php/gui/event/UXEvent.php`

**Классы наследники**

> [UXContextMenuEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXContextMenuEvent.ru.md), [UXDragEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXDragEvent.ru.md)

**Описание**

Class Event

---

#### Свойства

- `->`[`sender`](#prop-sender) : `UXNode|UXWindow`
- `->`[`target`](#prop-target) : `object|UXNode`

---

#### Статичные Методы

- `UXEvent ::`[`makeMock()`](#method-makemock)

---

#### Методы

- `->`[`copyFor()`](#method-copyfor)
- `->`[`isConsumed()`](#method-isconsumed)
- `->`[`consume()`](#method-consume) - _..._

---
# Статичные Методы

<a name="method-makemock"></a>

### makeMock()
```php
UXEvent::makeMock(mixed $sender): UXEvent
```

---
# Методы

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
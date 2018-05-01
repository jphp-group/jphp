# UXTab

- **класс** `UXTab` (`php\gui\UXTab`)
- **пакет** `gui`
- **исходники** `php/gui/UXTab.php`

**Классы наследники**

> [UXDraggableTab](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDraggableTab.ru.md)

**Описание**

Class UXTab

---

#### Свойства

- `->`[`closable`](#prop-closable) : `bool`
- `->`[`disabled`](#prop-disabled) : `bool`
- `->`[`disable`](#prop-disable) : `bool`
- `->`[`selected`](#prop-selected) : `bool`
- `->`[`id`](#prop-id) : `string`
- `->`[`content`](#prop-content) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`text`](#prop-text) : `string`
- `->`[`style`](#prop-style) : `string`
- `->`[`tooltip`](#prop-tooltip) : [`UXTooltip`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTooltip.ru.md)
- `->`[`userData`](#prop-userdata) : `mixed`
- `->`[`draggable`](#prop-draggable) : `bool`

---

#### Методы

- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)
- `->`[`data()`](#method-data) - _Getter and Setter for object data_

---
# Методы

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
trigger(string $event, php\gui\UXEvent $e): void
```

---

<a name="method-data"></a>

### data()
```php
data(string $name, mixed $value): mixed
```
Getter and Setter for object data
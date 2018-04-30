# UXTab

- **class** `UXTab` (`php\gui\UXTab`)
- **package** `gui`
- **source** [`php/gui/UXTab.php`](./src/main/resources/JPHP-INF/sdk/php/gui/UXTab.php)

**Description**

Class UXTab

---

#### Properties

- `->`[`closable`](#prop-closable) : `bool`
- `->`[`disabled`](#prop-disabled) : `bool`
- `->`[`disable`](#prop-disable) : `bool`
- `->`[`selected`](#prop-selected) : `bool`
- `->`[`id`](#prop-id) : `string`
- `->`[`content`](#prop-content) : `UXNode`
- `->`[`graphic`](#prop-graphic) : `UXNode`
- `->`[`text`](#prop-text) : `string`
- `->`[`style`](#prop-style) : `string`
- `->`[`tooltip`](#prop-tooltip) : `UXTooltip`
- `->`[`userData`](#prop-userdata) : `mixed`
- `->`[`draggable`](#prop-draggable) : `bool`

---

#### Methods

- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)
- `->`[`data()`](#method-data) - _Getter and Setter for object data_

---
# Methods

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
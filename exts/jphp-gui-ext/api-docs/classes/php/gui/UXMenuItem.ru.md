# UXMenuItem

- **класс** `UXMenuItem` (`php\gui\UXMenuItem`)
- **пакет** `gui`
- **исходники** `php/gui/UXMenuItem.php`

**Классы наследники**

> [UXMenu](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenu.ru.md)

**Описание**

Class UXMenuItem

---

#### Свойства

- `->`[`id`](#prop-id) : `string`
- `->`[`text`](#prop-text) : `string`
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`style`](#prop-style) : `string`
- `->`[`classes`](#prop-classes) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md)
- `->`[`accelerator`](#prop-accelerator) : `string`
- `->`[`parentPopup`](#prop-parentpopup) : [`UXContextMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXContextMenu.ru.md)
- `->`[`visible`](#prop-visible) : `bool`
- `->`[`disable`](#prop-disable) : `bool`
- `->`[`enabled`](#prop-enabled) : `bool`
- `->`[`userData`](#prop-userdata) : `mixed`

---

#### Статичные Методы

- `UXMenuItem ::`[`createSeparator()`](#method-createseparator)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)
- `->`[`isSeparator()`](#method-isseparator)

---
# Статичные Методы

<a name="method-createseparator"></a>

### createSeparator()
```php
UXMenuItem::createSeparator(): UXMenuItem
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $text, php\gui\UXNode $graphic): void
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

---

<a name="method-isseparator"></a>

### isSeparator()
```php
isSeparator(): bool
```
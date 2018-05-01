# UXMouseEvent

- **класс** `UXMouseEvent` (`php\gui\event\UXMouseEvent`) **унаследован от** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/event/UXMouseEvent.php`

**Описание**

Class UXMouseEvent

---

#### Свойства

- `->`[`x`](#prop-x) : `double`
- `->`[`y`](#prop-y) : `double`
- `->`[`position`](#prop-position) : `array` - _[x, y] position_
- `->`[`screenX`](#prop-screenx) : `double`
- `->`[`screenY`](#prop-screeny) : `double`
- `->`[`button`](#prop-button) : `string`
- `->`[`clickCount`](#prop-clickcount) : `int`
- `->`[`altDown`](#prop-altdown) : `bool`
- `->`[`controlDown`](#prop-controldown) : `bool`
- `->`[`shiftDown`](#prop-shiftdown) : `bool`
- `->`[`metaDown`](#prop-metadown) : `bool`
- `->`[`shortcutDown`](#prop-shortcutdown) : `bool`
- *См. также в родительском классе* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md).

---

#### Методы

- `->`[`isDoubleClick()`](#method-isdoubleclick) - _Returns true if clickCount >= 2._
- См. также в родительском классе [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)

---
# Методы

<a name="method-isdoubleclick"></a>

### isDoubleClick()
```php
isDoubleClick(): bool
```
Returns true if clickCount >= 2.
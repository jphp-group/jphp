# UXMouseEvent

- **class** `UXMouseEvent` (`php\gui\event\UXMouseEvent`) **extends** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)
- **package** `gui`
- **source** `php/gui/event/UXMouseEvent.php`

**Description**

Class UXMouseEvent

---

#### Properties

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
- *See also in the parent class* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md).

---

#### Methods

- `->`[`isDoubleClick()`](#method-isdoubleclick) - _Returns true if clickCount >= 2._
- See also in the parent class [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)

---
# Methods

<a name="method-isdoubleclick"></a>

### isDoubleClick()
```php
isDoubleClick(): bool
```
Returns true if clickCount >= 2.
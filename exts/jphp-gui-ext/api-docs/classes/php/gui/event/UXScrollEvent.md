# UXScrollEvent

- **class** `UXScrollEvent` (`php\gui\event\UXScrollEvent`) **extends** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)
- **package** `gui`
- **source** `php/gui/event/UXScrollEvent.php`

**Description**

Class UXScrollEvent

---

#### Properties

- `->`[`deltaX`](#prop-deltax) : `double`
- `->`[`deltaY`](#prop-deltay) : `double`
- `->`[`textDeltaX`](#prop-textdeltax) : `double`
- `->`[`textDeltaY`](#prop-textdeltay) : `double`
- `->`[`totalDeltaX`](#prop-totaldeltax) : `double`
- `->`[`totalDeltaY`](#prop-totaldeltay) : `double`
- `->`[`multiplierX`](#prop-multiplierx) : `double`
- `->`[`multiplierY`](#prop-multipliery) : `double`
- `->`[`touchCount`](#prop-touchcount) : `int`
- `->`[`eventType`](#prop-eventtype) : `string SCROLL, SCROLL_STARTED, SCROLL_FINISHED`
- `->`[`pickResult`](#prop-pickresult) : `array` - _Returns information about the pick._
- `->`[`altDown`](#prop-altdown) : `bool` - _Indicates whether or not the Alt modifier is down on this event._
- `->`[`shiftDown`](#prop-shiftdown) : `bool` - _Indicates whether or not the Shift modifier is down on this event._
- `->`[`controlDown`](#prop-controldown) : `bool` - _Indicates whether or not the Control modifier is down on this event._
- `->`[`metaDown`](#prop-metadown) : `bool` - _Indicates whether or not the Meta modifier is down on this event._
- `->`[`shortcutDown`](#prop-shortcutdown) : `bool` - _Indicates whether or not the Shortcut modifier is down on this event._
- *See also in the parent class* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md).

---

#### Methods

- `->`[`isDirect()`](#method-isdirect) - _Indicates whether this gesture is caused by a direct or indirect input_
- `->`[`isInertia()`](#method-isinertia) - _Indicates if this event represents an inertia of an already finished_
- See also in the parent class [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)

---
# Methods

<a name="method-isdirect"></a>

### isDirect()
```php
isDirect(): bool
```
Indicates whether this gesture is caused by a direct or indirect input
device. With direct input device the gestures are performed directly at
the concrete coordinates, a typical example would be a touch screen.
With indirect device the gestures are performed indirectly and usually
mouse cursor position is used as the gesture coordinates, a typical
example would be a track pad.

---

<a name="method-isinertia"></a>

### isInertia()
```php
isInertia(): bool
```
Indicates if this event represents an inertia of an already finished
gesture.
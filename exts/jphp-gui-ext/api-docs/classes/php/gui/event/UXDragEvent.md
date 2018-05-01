# UXDragEvent

- **class** `UXDragEvent` (`php\gui\event\UXDragEvent`) **extends** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)
- **package** `gui`
- **source** `php/gui/event/UXDragEvent.php`

**Description**

Class UXDragEvent

---

#### Properties

- `->`[`sceneX`](#prop-scenex) : `double`
- `->`[`sceneY`](#prop-sceney) : `double`
- `->`[`screenX`](#prop-screenx) : `double`
- `->`[`screenY`](#prop-screeny) : `double`
- `->`[`x`](#prop-x) : `double`
- `->`[`y`](#prop-y) : `double`
- `->`[`accepted`](#prop-accepted) : `bool`
- `->`[`acceptedTransferMode`](#prop-acceptedtransfermode) : `string`
- `->`[`transferMode`](#prop-transfermode) : `string`
- `->`[`dropCompleted`](#prop-dropcompleted) : `bool`
- `->`[`gestureSource`](#prop-gesturesource) : `mixed|UXNode`
- `->`[`gestureTarget`](#prop-gesturetarget) : `mixed|UXNode`
- `->`[`dragboard`](#prop-dragboard) : [`UXDragboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDragboard.md)
- *See also in the parent class* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md).

---

#### Methods

- `->`[`acceptTransferModes()`](#method-accepttransfermodes)
- See also in the parent class [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)

---
# Methods

<a name="method-accepttransfermodes"></a>

### acceptTransferModes()
```php
acceptTransferModes(array $modes): void
```
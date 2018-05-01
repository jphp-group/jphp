# UXDragEvent

- **class** `UXDragEvent` (`php\gui\event\UXDragEvent`) **extends** `UXEvent` (`php\gui\event\UXEvent`)
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
- `->`[`dragboard`](#prop-dragboard) : `UXDragboard`

---

#### Methods

- `->`[`acceptTransferModes()`](#method-accepttransfermodes)

---
# Methods

<a name="method-accepttransfermodes"></a>

### acceptTransferModes()
```php
acceptTransferModes(array $modes): void
```
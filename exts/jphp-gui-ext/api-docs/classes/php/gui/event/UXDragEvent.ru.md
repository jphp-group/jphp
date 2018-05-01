# UXDragEvent

- **класс** `UXDragEvent` (`php\gui\event\UXDragEvent`) **унаследован от** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/event/UXDragEvent.php`

**Описание**

Class UXDragEvent

---

#### Свойства

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
- `->`[`dragboard`](#prop-dragboard) : [`UXDragboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDragboard.ru.md)
- *См. также в родительском классе* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md).

---

#### Методы

- `->`[`acceptTransferModes()`](#method-accepttransfermodes)
- См. также в родительском классе [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)

---
# Методы

<a name="method-accepttransfermodes"></a>

### acceptTransferModes()
```php
acceptTransferModes(array $modes): void
```
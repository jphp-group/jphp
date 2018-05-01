# UXParent

- **class** `UXParent` (`php\gui\UXParent`) **extends** [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- **package** `gui`
- **source** `php/gui/UXParent.php`

**Child Classes**

> [UXRegion](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXRegion.md), [UXGroup](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGroup.md), [UXWebView](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebView.md)

**Description**

Class UXParent

---

#### Properties

- `->`[`childrenUnmodifiable`](#prop-childrenunmodifiable) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.md)
- `->`[`stylesheets`](#prop-stylesheets) : `UXList of string (paths for css style sheets)` - _Список css таблиц стилей (пути к файлам и ресурсам)._
- *See also in the parent class* [UXNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md).

---

#### Methods

- `->`[`layout()`](#method-layout) - _Executes a top-down layout pass on the scene graph under this parent._
- `->`[`requestLayout()`](#method-requestlayout) - _Requests a layout pass to be performed before the next scene is rendered._
- `->`[`findNode()`](#method-findnode)
- `->`[`findFocusedNode()`](#method-findfocusednode)
- See also in the parent class [UXNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)

---
# Methods

<a name="method-layout"></a>

### layout()
```php
layout(): void
```
Executes a top-down layout pass on the scene graph under this parent.

---

<a name="method-requestlayout"></a>

### requestLayout()
```php
requestLayout(): void
```
Requests a layout pass to be performed before the next scene is rendered.

---

<a name="method-findnode"></a>

### findNode()
```php
findNode(callable $filter): void
```

---

<a name="method-findfocusednode"></a>

### findFocusedNode()
```php
findFocusedNode(): UXNode|null
```
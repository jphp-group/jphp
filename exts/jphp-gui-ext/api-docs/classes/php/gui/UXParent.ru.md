# UXParent

- **класс** `UXParent` (`php\gui\UXParent`) **унаследован от** [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXParent.php`

**Классы наследники**

> [UXRegion](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXRegion.ru.md), [UXGroup](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGroup.ru.md), [UXWebView](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebView.ru.md)

**Описание**

Class UXParent

---

#### Свойства

- `->`[`childrenUnmodifiable`](#prop-childrenunmodifiable) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md)
- `->`[`stylesheets`](#prop-stylesheets) : `UXList of string (paths for css style sheets)` - _Список css таблиц стилей (пути к файлам и ресурсам)._
- *См. также в родительском классе* [UXNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md).

---

#### Методы

- `->`[`layout()`](#method-layout) - _Executes a top-down layout pass on the scene graph under this parent._
- `->`[`requestLayout()`](#method-requestlayout) - _Requests a layout pass to be performed before the next scene is rendered._
- `->`[`findNode()`](#method-findnode)
- `->`[`findFocusedNode()`](#method-findfocusednode)
- См. также в родительском классе [UXNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)

---
# Методы

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
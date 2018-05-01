# UXMenuButton

- **class** `UXMenuButton` (`php\gui\UXMenuButton`) **extends** [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.md)
- **package** `gui`
- **source** `php/gui/UXMenuButton.php`

**Child Classes**

> [UXSplitMenuButton](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitMenuButton.md)

**Description**

Class UXMenuButton

---

#### Properties

- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.md) - _List of UXMenuItem_
- `->`[`popupSide`](#prop-popupside) : `string` - _BOTTOM, TOP, LEFT or RIGHT._
- *See also in the parent class* [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _UXMenuButton constructor._
- `->`[`showMenu()`](#method-showmenu) - _Show popup menu._
- `->`[`hideMenu()`](#method-hidemenu) - _Hide popup menu._
- See also in the parent class [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $text, php\gui\UXNode $graphic): void
```
UXMenuButton constructor.

---

<a name="method-showmenu"></a>

### showMenu()
```php
showMenu(): void
```
Show popup menu.

---

<a name="method-hidemenu"></a>

### hideMenu()
```php
hideMenu(): void
```
Hide popup menu.
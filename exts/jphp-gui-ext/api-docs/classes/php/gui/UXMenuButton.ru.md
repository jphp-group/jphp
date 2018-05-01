# UXMenuButton

- **класс** `UXMenuButton` (`php\gui\UXMenuButton`) **унаследован от** [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXMenuButton.php`

**Классы наследники**

> [UXSplitMenuButton](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitMenuButton.ru.md)

**Описание**

Class UXMenuButton

---

#### Свойства

- `->`[`items`](#prop-items) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md) - _List of UXMenuItem_
- `->`[`popupSide`](#prop-popupside) : `string` - _BOTTOM, TOP, LEFT or RIGHT._
- *См. также в родительском классе* [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXMenuButton constructor._
- `->`[`showMenu()`](#method-showmenu) - _Show popup menu._
- `->`[`hideMenu()`](#method-hidemenu) - _Hide popup menu._
- См. также в родительском классе [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)

---
# Методы

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
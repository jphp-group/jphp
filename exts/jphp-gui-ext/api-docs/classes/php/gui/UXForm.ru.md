# UXForm

- **класс** `UXForm` (`php\gui\UXForm`) **унаследован от** [`UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXForm.php`

---

#### Свойства

- `->`[`title`](#prop-title) : `string` - _Заголовок окна._
- `->`[`modality`](#prop-modality) : `string` - _NONE, WINDOW_MODAL, APPLICATION_MODAL_
- `->`[`alwaysOnTop`](#prop-alwaysontop) : `bool` - _Всегда поверх всех окон._
- `->`[`maximized`](#prop-maximized) : `bool` - _Раскрытость на весь экран._
- `->`[`owner`](#prop-owner) : [`UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md)
- `->`[`style`](#prop-style) : `string` - _Стиль окна.

DECORATED, UNDECORATED, TRANSPARENT, UTILITY, UNIFIED_
- `->`[`icons`](#prop-icons) : `UXList of UXImage` - _Иконки окна._
- `->`[`transparent`](#prop-transparent) : `bool` - _Прозрачность._
- `->`[`resizable`](#prop-resizable) : `bool`
- `->`[`iconified`](#prop-iconified) : `bool`
- `->`[`fullScreen`](#prop-fullscreen) : `bool` - _Фулскрин._
- *См. также в родительском классе* [UXWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`showAndWait()`](#method-showandwait) - _Показать окно и ожидать его закрытия._
- `->`[`toBack()`](#method-toback) - _Переместить назад (под все окна)._
- `->`[`toFront()`](#method-tofront) - _Переместить вперед (над всеми окнами)._
- `->`[`maximize()`](#method-maximize)
- См. также в родительском классе [UXWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXForm $form): void
```

---

<a name="method-showandwait"></a>

### showAndWait()
```php
showAndWait(): void
```
Показать окно и ожидать его закрытия.
! Только для модальных окон.

---

<a name="method-toback"></a>

### toBack()
```php
toBack(): void
```
Переместить назад (под все окна).

---

<a name="method-tofront"></a>

### toFront()
```php
toFront(): void
```
Переместить вперед (над всеми окнами).

---

<a name="method-maximize"></a>

### maximize()
```php
maximize(): void
```
# UXWindow

- **класс** `UXWindow` (`php\gui\UXWindow`)
- **пакет** `gui`
- **исходники** `php/gui/UXWindow.php`

**Классы наследники**

> [UXForm](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXForm.ru.md), [UXPopupWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md)

**Описание**

Class UXWindow

---

#### Свойства

- `->`[`scene`](#prop-scene) : [`UXScene`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScene.ru.md) - _Сцена._
- `->`[`x`](#prop-x) : `double`
- `->`[`y`](#prop-y) : `double`
- `->`[`width`](#prop-width) : `double`
- `->`[`height`](#prop-height) : `double`
- `->`[`opacity`](#prop-opacity) : `double`
- `->`[`size`](#prop-size) : `double[]` - _Размеры [width, height]_
- `->`[`layout`](#prop-layout) : [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)
- `->`[`children`](#prop-children) : [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md) - _Компоненты._
- `->`[`visible`](#prop-visible) : `bool` - _Видимость._
- `->`[`cursor`](#prop-cursor) : `string` - _Тип курсора._
- `->`[`userData`](#prop-userdata) : `mixed` - _Любые данные._

---

#### Методы

- `->`[`requestFocus()`](#method-requestfocus) - _Перевести фокус на окно._
- `->`[`show()`](#method-show) - _Показать окно._
- `->`[`hide()`](#method-hide) - _Скрыть окно._
- `->`[`centerOnScreen()`](#method-centeronscreen) - _Отцентрировать окно относительно разрешения._
- `->`[`sizeToScene()`](#method-sizetoscene)
- `->`[`data()`](#method-data) - _Пополнительные данные окна (геттер и сеттер)._
- `->`[`on()`](#method-on) - _Навесить событие._
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)
- `->`[`addEventFilter()`](#method-addeventfilter)
- `->`[`watch()`](#method-watch) **common.deprecated** - _Use observer() .._
- `->`[`observer()`](#method-observer)
- `->`[`add()`](#method-add) - _Добавить компонент._
- `->`[`remove()`](#method-remove) - _Удалить компонент._
- `->`[`addStylesheet()`](#method-addstylesheet) - _Добавить файл css стилей окну._
- `->`[`removeStylesheet()`](#method-removestylesheet) - _Удалить файл css стилей у окна._
- `->`[`hasStylesheet()`](#method-hasstylesheet) - _Возвращает true если указанный файл css стилей уже добавлен окну._
- `->`[`clearStylesheets()`](#method-clearstylesheets) - _Очистить от всех внешних стилей._
- `->`[`makeVirtualLayout()`](#method-makevirtuallayout) - _Make layout virtual._
- `->`[`__get()`](#method-__get)
- `->`[`__isset()`](#method-__isset)

---
# Методы

<a name="method-requestfocus"></a>

### requestFocus()
```php
requestFocus(): void
```
Перевести фокус на окно.

---

<a name="method-show"></a>

### show()
```php
show(): void
```
Показать окно.

---

<a name="method-hide"></a>

### hide()
```php
hide(): void
```
Скрыть окно.

---

<a name="method-centeronscreen"></a>

### centerOnScreen()
```php
centerOnScreen(): void
```
Отцентрировать окно относительно разрешения.

---

<a name="method-sizetoscene"></a>

### sizeToScene()
```php
sizeToScene(): void
```

---

<a name="method-data"></a>

### data()
```php
data(string $name, mixed $value): mixed
```
Пополнительные данные окна (геттер и сеттер).

---

<a name="method-on"></a>

### on()
```php
on(string $event, callable $handler, string $group): void
```
Навесить событие.

---

<a name="method-off"></a>

### off()
```php
off(string $event, string $group): void
```

---

<a name="method-trigger"></a>

### trigger()
```php
trigger(string $event, php\gui\event\UXEvent $e): void
```

---

<a name="method-addeventfilter"></a>

### addEventFilter()
```php
addEventFilter(string $event, callable $filter): void
```

---

<a name="method-watch"></a>

### watch()
```php
watch(string $property, callable $listener): void
```
Use observer() ..

---

<a name="method-observer"></a>

### observer()
```php
observer(string $property): UXValue
```

---

<a name="method-add"></a>

### add()
```php
add(php\gui\UXNode $node): void
```
Добавить компонент.

---

<a name="method-remove"></a>

### remove()
```php
remove(php\gui\UXNode $node): bool
```
Удалить компонент.

---

<a name="method-addstylesheet"></a>

### addStylesheet()
```php
addStylesheet(string $path): void
```
Добавить файл css стилей окну.

---

<a name="method-removestylesheet"></a>

### removeStylesheet()
```php
removeStylesheet(string $path): void
```
Удалить файл css стилей у окна.

---

<a name="method-hasstylesheet"></a>

### hasStylesheet()
```php
hasStylesheet(string $path): bool
```
Возвращает true если указанный файл css стилей уже добавлен окну.

---

<a name="method-clearstylesheets"></a>

### clearStylesheets()
```php
clearStylesheets(): void
```
Очистить от всех внешних стилей.

---

<a name="method-makevirtuallayout"></a>

### makeVirtualLayout()
```php
makeVirtualLayout(): void
```
Make layout virtual.

---

<a name="method-__get"></a>

### __get()
```php
__get(string $id): UXNode|null
```

---

<a name="method-__isset"></a>

### __isset()
```php
__isset(string $id): bool
```
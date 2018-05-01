# UXHyperlink

- **класс** `UXHyperlink` (`php\gui\UXHyperlink`) **унаследован от** [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXHyperlink.php`

**Описание**

Class UXHyperlink

---

#### Свойства

- `->`[`visited`](#prop-visited) : `bool`
- *См. также в родительском классе* [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md).

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`fire()`](#method-fire) - _Implemented to invoke the action event if one is defined. This_
- См. также в родительском классе [UXButtonBase](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $text, php\gui\UXNode $graphic): void
```

---

<a name="method-fire"></a>

### fire()
```php
fire(): void
```
Implemented to invoke the action event if one is defined. This
function will also set visited to true.
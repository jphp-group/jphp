# UXHyperlink

- **class** `UXHyperlink` (`php\gui\UXHyperlink`) **extends** [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.md)
- **package** `gui`
- **source** [`php/gui/UXHyperlink.php`](./src/main/resources/JPHP-INF/sdk/php/gui/UXHyperlink.php)

**Description**

Class UXHyperlink

---

#### Properties

- `->`[`visited`](#prop-visited) : `bool`

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`fire()`](#method-fire) - _Implemented to invoke the action event if one is defined. This_

---
# Methods

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
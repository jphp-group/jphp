# UXCanvas

- **class** `UXCanvas` (`php\gui\UXCanvas`) **extends** [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- **package** `gui`
- **source** `php/gui/UXCanvas.php`

**Child Classes**

> [UXImageArea](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageArea.md)

**Description**

Class UXCanvas

---

#### Methods

- `->`[`getGraphicsContext()`](#method-getgraphicscontext)
- `->`[`gc()`](#method-gc) - _Alias of getGraphicsContext()._
- `->`[`save()`](#method-save) - _Save image of canvas to file or stream in passed format, by default png._
- `->`[`writeImageAsync()`](#method-writeimageasync) **common.deprecated**
- See also in the parent class [UXNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)

---
# Methods

<a name="method-getgraphicscontext"></a>

### getGraphicsContext()
```php
getGraphicsContext(): UXGraphicsContext
```

---

<a name="method-gc"></a>

### gc()
```php
gc(): UXGraphicsContext
```
Alias of getGraphicsContext().

---

<a name="method-save"></a>

### save()
```php
save(string|Stream|File $to, string $format): void
```
Save image of canvas to file or stream in passed format, by default png.

---

<a name="method-writeimageasync"></a>

### writeImageAsync()
```php
writeImageAsync(string $format, Stream|File|string $output, php\gui\paint\UXColor $transparentColor, callable $callback): void
```
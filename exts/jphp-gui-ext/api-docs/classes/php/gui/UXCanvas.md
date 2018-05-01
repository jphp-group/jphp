# UXCanvas

- **class** `UXCanvas` (`php\gui\UXCanvas`) **extends** `UXNode` (`php\gui\UXNode`)
- **package** `gui`
- **source** `php/gui/UXCanvas.php`

**Description**

Class UXCanvas

---

#### Methods

- `->`[`getGraphicsContext()`](#method-getgraphicscontext)
- `->`[`gc()`](#method-gc) - _Alias of getGraphicsContext()._
- `->`[`save()`](#method-save) - _Save image of canvas to file or stream in passed format, by default png._
- `->`[`writeImageAsync()`](#method-writeimageasync) **common.deprecated**

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
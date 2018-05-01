# UXCanvas

- **класс** `UXCanvas` (`php\gui\UXCanvas`) **унаследован от** `UXNode` (`php\gui\UXNode`)
- **пакет** `gui`
- **исходники** `php/gui/UXCanvas.php`

**Описание**

Class UXCanvas

---

#### Методы

- `->`[`getGraphicsContext()`](#method-getgraphicscontext)
- `->`[`gc()`](#method-gc) - _Alias of getGraphicsContext()._
- `->`[`save()`](#method-save) - _Сохранить изображение полотна в файл или поток в переданном формате, по-умолчанию png._
- `->`[`writeImageAsync()`](#method-writeimageasync) **common.deprecated**

---
# Методы

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
Сохранить изображение полотна в файл или поток в переданном формате, по-умолчанию png.

---

<a name="method-writeimageasync"></a>

### writeImageAsync()
```php
writeImageAsync(string $format, Stream|File|string $output, php\gui\paint\UXColor $transparentColor, callable $callback): void
```
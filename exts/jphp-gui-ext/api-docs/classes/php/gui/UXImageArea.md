# UXImageArea

- **class** `UXImageArea` (`php\gui\UXImageArea`) **extends** [`UXCanvas`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md)
- **package** `gui`
- **source** `php/gui/UXImageArea.php`

**Description**

Class UXImageArea

---

#### Properties

- `->`[`centered`](#prop-centered) : `bool` - _Центроровано._
- `->`[`stretch`](#prop-stretch) : `bool` - _Растягивать._
- `->`[`smartStretch`](#prop-smartstretch) : `bool` - _Растягивать по-умному, не растягивая маленькие картинки._
- `->`[`autoSize`](#prop-autosize) : `bool` - _Авторазмер._
- `->`[`proportional`](#prop-proportional) : `bool` - _Пропорциональность._
- `->`[`mosaic`](#prop-mosaic) : `bool` - _Мозаичное изображение._
- `->`[`mosaicGap`](#prop-mosaicgap) : `float` - _Отступы для мозайки._
- `->`[`text`](#prop-text) : `string` - _Текст._
- `->`[`font`](#prop-font) : [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.md) - _Шрифт текста._
- `->`[`textColor`](#prop-textcolor) : [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.md) - _Цвет текста._
- `->`[`backgroundColor`](#prop-backgroundcolor) : [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.md) - _Фоновый цвет._
- `->`[`image`](#prop-image) : [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.md) - _Изображение._
- `->`[`hoverImage`](#prop-hoverimage) : [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.md) - _Изображение при наведении._
- `->`[`clickImage`](#prop-clickimage) : [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.md) - _Изображение при клике._
- `->`[`flipX`](#prop-flipx) : `bool` - _Отразить изображение по оси X._
- `->`[`flipY`](#prop-flipy) : `bool` - _Отразить изображение по оси Y._
- *See also in the parent class* [UXCanvas](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- See also in the parent class [UXCanvas](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXImage $image): void
```
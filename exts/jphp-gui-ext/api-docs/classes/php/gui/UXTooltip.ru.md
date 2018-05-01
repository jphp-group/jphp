# UXTooltip

- **класс** `UXTooltip` (`php\gui\UXTooltip`) **унаследован от** [`UXPopupWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXTooltip.php`

**Описание**

Class UXTooltip

---

#### Свойства

- `->`[`text`](#prop-text) : `string`
- `->`[`textAlignment`](#prop-textalignment) : `string` - _LEFT, RIGHT, CENTER, JUSTIFY_
- `->`[`textOverrun`](#prop-textoverrun) : `string` - _CLIP, ELLIPSIS, WORD_ELLIPSIS, CENTER_ELLIPSIS, CENTER_WORD_ELLIPSIS, LEADING_ELLIPSIS, LEADING_WORD_ELLIPSIS_
- `->`[`font`](#prop-font) : [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.ru.md)
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)
- `->`[`graphicTextGap`](#prop-graphictextgap) : `double`
- `->`[`activated`](#prop-activated) : `bool`
- `->`[`wrapText`](#prop-wraptext) : `bool`
- *См. также в родительском классе* [UXPopupWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md).

---

#### Статичные Методы

- `UXTooltip ::`[`of()`](#method-of)
- `UXTooltip ::`[`install()`](#method-install)
- `UXTooltip ::`[`uninstall()`](#method-uninstall)
- См. также в родительском классе [UXPopupWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md)

---
# Статичные Методы

<a name="method-of"></a>

### of()
```php
UXTooltip::of(string $text, php\gui\UXNode $graphic): UXTooltip
```

---

<a name="method-install"></a>

### install()
```php
UXTooltip::install(php\gui\UXNode $node, php\gui\UXTooltip $tooltip): void
```

---

<a name="method-uninstall"></a>

### uninstall()
```php
UXTooltip::uninstall(php\gui\UXNode $node, php\gui\UXTooltip $tooltip): void
```
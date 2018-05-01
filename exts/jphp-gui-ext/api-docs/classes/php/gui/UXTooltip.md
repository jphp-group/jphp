# UXTooltip

- **class** `UXTooltip` (`php\gui\UXTooltip`) **extends** [`UXPopupWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.md)
- **package** `gui`
- **source** `php/gui/UXTooltip.php`

**Description**

Class UXTooltip

---

#### Properties

- `->`[`text`](#prop-text) : `string`
- `->`[`textAlignment`](#prop-textalignment) : `string` - _LEFT, RIGHT, CENTER, JUSTIFY_
- `->`[`textOverrun`](#prop-textoverrun) : `string` - _CLIP, ELLIPSIS, WORD_ELLIPSIS, CENTER_ELLIPSIS, CENTER_WORD_ELLIPSIS, LEADING_ELLIPSIS, LEADING_WORD_ELLIPSIS_
- `->`[`font`](#prop-font) : [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.md)
- `->`[`graphic`](#prop-graphic) : [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)
- `->`[`graphicTextGap`](#prop-graphictextgap) : `double`
- `->`[`activated`](#prop-activated) : `bool`
- `->`[`wrapText`](#prop-wraptext) : `bool`
- *See also in the parent class* [UXPopupWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.md).

---

#### Static Methods

- `UXTooltip ::`[`of()`](#method-of)
- `UXTooltip ::`[`install()`](#method-install)
- `UXTooltip ::`[`uninstall()`](#method-uninstall)
- See also in the parent class [UXPopupWindow](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.md)

---
# Static Methods

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
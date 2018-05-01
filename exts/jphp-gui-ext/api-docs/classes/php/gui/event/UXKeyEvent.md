# UXKeyEvent

- **class** `UXKeyEvent` (`php\gui\event\UXKeyEvent`) **extends** [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)
- **package** `gui`
- **source** `php/gui/event/UXKeyEvent.php`

**Description**

Class UXKeyEvent

---

#### Properties

- `->`[`character`](#prop-character) : `string`
- `->`[`text`](#prop-text) : `string`
- `->`[`codeName`](#prop-codename) : `string`
- `->`[`altDown`](#prop-altdown) : `bool`
- `->`[`controlDown`](#prop-controldown) : `bool`
- `->`[`shiftDown`](#prop-shiftdown) : `bool`
- `->`[`metaDown`](#prop-metadown) : `bool`
- `->`[`shortcutDown`](#prop-shortcutdown) : `bool`
- *See also in the parent class* [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md).

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`matches()`](#method-matches)
- `->`[`isArrowKey()`](#method-isarrowkey) - _Left, right, up, down keys (including the keypad arrows)_
- `->`[`isDigitKey()`](#method-isdigitkey) - _All Digit keys (including the keypad digits)_
- `->`[`isFunctionKey()`](#method-isfunctionkey) - _Function keys like F1, F2, etc..._
- `->`[`isNavigationKey()`](#method-isnavigationkey) - _Navigation keys are arrow keys and Page Down, Page Up, Home, End_
- `->`[`isModifierKey()`](#method-ismodifierkey) - _Keys that could act as a modifier._
- `->`[`isLetterKey()`](#method-isletterkey) - _All keys with letters._
- `->`[`isKeypadKey()`](#method-iskeypadkey) - _All keys on the keypad._
- `->`[`isWhitespaceKey()`](#method-iswhitespacekey) - _Space, tab and enter._
- `->`[`isMediaKey()`](#method-ismediakey) - _All multimedia keys (channel up/down, volume control, etc...)._
- `->`[`isUndefinedKey()`](#method-isundefinedkey) - _This value is used to indicate that the keyCode is unknown._
- See also in the parent class [UXEvent](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\event\UXKeyEvent $parent, mixed $sender): void
```

---

<a name="method-matches"></a>

### matches()
```php
matches(string $accelerator): bool
```

---

<a name="method-isarrowkey"></a>

### isArrowKey()
```php
isArrowKey(): bool
```
Left, right, up, down keys (including the keypad arrows)

---

<a name="method-isdigitkey"></a>

### isDigitKey()
```php
isDigitKey(): bool
```
All Digit keys (including the keypad digits)

---

<a name="method-isfunctionkey"></a>

### isFunctionKey()
```php
isFunctionKey(): bool
```
Function keys like F1, F2, etc...

---

<a name="method-isnavigationkey"></a>

### isNavigationKey()
```php
isNavigationKey(): bool
```
Navigation keys are arrow keys and Page Down, Page Up, Home, End
(including keypad keys).

---

<a name="method-ismodifierkey"></a>

### isModifierKey()
```php
isModifierKey(): bool
```
Keys that could act as a modifier.

---

<a name="method-isletterkey"></a>

### isLetterKey()
```php
isLetterKey(): bool
```
All keys with letters.

---

<a name="method-iskeypadkey"></a>

### isKeypadKey()
```php
isKeypadKey(): bool
```
All keys on the keypad.

---

<a name="method-iswhitespacekey"></a>

### isWhitespaceKey()
```php
isWhitespaceKey(): bool
```
Space, tab and enter.

---

<a name="method-ismediakey"></a>

### isMediaKey()
```php
isMediaKey(): bool
```
All multimedia keys (channel up/down, volume control, etc...).

---

<a name="method-isundefinedkey"></a>

### isUndefinedKey()
```php
isUndefinedKey(): bool
```
This value is used to indicate that the keyCode is unknown.
Key typed events do not have a keyCode value; this value
is used instead.
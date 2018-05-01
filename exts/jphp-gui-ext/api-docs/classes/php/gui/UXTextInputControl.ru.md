# UXTextInputControl

- **класс** `UXTextInputControl` (`php\gui\UXTextInputControl`) **унаследован от** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)
- **пакет** `gui`
- **исходники** `php/gui/UXTextInputControl.php`

**Классы наследники**

> [UXTextArea](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextArea.ru.md), [UXTextField](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.ru.md)

**Описание**

Class UXTextInputControl

---

#### Свойства

- `->`[`anchor`](#prop-anchor) : `int`
- `->`[`text`](#prop-text) : `string`
- `->`[`font`](#prop-font) : [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.ru.md)
- `->`[`selection`](#prop-selection) : `array` - _array(start, end, length)._
- `->`[`selectedText`](#prop-selectedtext) : `string`
- `->`[`promptText`](#prop-prompttext) : `string`
- `->`[`length`](#prop-length) : `int`
- `->`[`editable`](#prop-editable) : `bool`
- `->`[`caretPosition`](#prop-caretposition) : `int`
- *См. также в родительском классе* [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md).

---

#### Методы

- `->`[`copy()`](#method-copy)
- `->`[`cut()`](#method-cut)
- `->`[`paste()`](#method-paste)
- `->`[`clear()`](#method-clear)
- `->`[`end()`](#method-end)
- `->`[`home()`](#method-home)
- `->`[`forward()`](#method-forward)
- `->`[`backward()`](#method-backward)
- `->`[`nextWord()`](#method-nextword)
- `->`[`previousWord()`](#method-previousword)
- `->`[`selectAll()`](#method-selectall)
- `->`[`selectBackward()`](#method-selectbackward)
- `->`[`selectEnd()`](#method-selectend)
- `->`[`selectEndOfNextWord()`](#method-selectendofnextword)
- `->`[`selectForward()`](#method-selectforward)
- `->`[`selectHome()`](#method-selecthome)
- `->`[`selectNextWord()`](#method-selectnextword)
- `->`[`selectPreviousWord()`](#method-selectpreviousword)
- `->`[`selectPositionCaret()`](#method-selectpositioncaret)
- `->`[`selectRange()`](#method-selectrange)
- `->`[`extendSelection()`](#method-extendselection)
- `->`[`deselect()`](#method-deselect) - _Deselect all._
- `->`[`appendText()`](#method-appendtext)
- `->`[`insertText()`](#method-inserttext)
- `->`[`replaceText()`](#method-replacetext)
- `->`[`replaceSelection()`](#method-replaceselection)
- `->`[`positionCaret()`](#method-positioncaret)
- `->`[`undo()`](#method-undo) - _Undo changes._
- `->`[`redo()`](#method-redo) - _Redo changes._
- `->`[`commitValue()`](#method-commitvalue) - _Commit the current text and convert it to a value._
- `->`[`cancelEdit()`](#method-canceledit) - _If the field is currently being edited, this call will set text to the last commited value._
- См. также в родительском классе [UXControl](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)

---
# Методы

<a name="method-copy"></a>

### copy()
```php
copy(): void
```

---

<a name="method-cut"></a>

### cut()
```php
cut(): void
```

---

<a name="method-paste"></a>

### paste()
```php
paste(): void
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```

---

<a name="method-end"></a>

### end()
```php
end(): void
```

---

<a name="method-home"></a>

### home()
```php
home(): void
```

---

<a name="method-forward"></a>

### forward()
```php
forward(): void
```

---

<a name="method-backward"></a>

### backward()
```php
backward(): void
```

---

<a name="method-nextword"></a>

### nextWord()
```php
nextWord(): void
```

---

<a name="method-previousword"></a>

### previousWord()
```php
previousWord(): void
```

---

<a name="method-selectall"></a>

### selectAll()
```php
selectAll(): void
```

---

<a name="method-selectbackward"></a>

### selectBackward()
```php
selectBackward(): void
```

---

<a name="method-selectend"></a>

### selectEnd()
```php
selectEnd(): void
```

---

<a name="method-selectendofnextword"></a>

### selectEndOfNextWord()
```php
selectEndOfNextWord(): void
```

---

<a name="method-selectforward"></a>

### selectForward()
```php
selectForward(): void
```

---

<a name="method-selecthome"></a>

### selectHome()
```php
selectHome(): void
```

---

<a name="method-selectnextword"></a>

### selectNextWord()
```php
selectNextWord(): void
```

---

<a name="method-selectpreviousword"></a>

### selectPreviousWord()
```php
selectPreviousWord(): void
```

---

<a name="method-selectpositioncaret"></a>

### selectPositionCaret()
```php
selectPositionCaret(int $pos): void
```

---

<a name="method-selectrange"></a>

### selectRange()
```php
selectRange(int $anchor, int $caretPosition): void
```

---

<a name="method-extendselection"></a>

### extendSelection()
```php
extendSelection(int $pos): void
```

---

<a name="method-deselect"></a>

### deselect()
```php
deselect(): void
```
Deselect all.

---

<a name="method-appendtext"></a>

### appendText()
```php
appendText(string $text): void
```

---

<a name="method-inserttext"></a>

### insertText()
```php
insertText(int $index, string $text): void
```

---

<a name="method-replacetext"></a>

### replaceText()
```php
replaceText(int $start, int $end, string $text): void
```

---

<a name="method-replaceselection"></a>

### replaceSelection()
```php
replaceSelection(string $text): void
```

---

<a name="method-positioncaret"></a>

### positionCaret()
```php
positionCaret(int $pos): void
```

---

<a name="method-undo"></a>

### undo()
```php
undo(): void
```
Undo changes.

---

<a name="method-redo"></a>

### redo()
```php
redo(): void
```
Redo changes.

---

<a name="method-commitvalue"></a>

### commitValue()
```php
commitValue(): void
```
Commit the current text and convert it to a value.

---

<a name="method-canceledit"></a>

### cancelEdit()
```php
cancelEdit(): void
```
If the field is currently being edited, this call will set text to the last commited value.
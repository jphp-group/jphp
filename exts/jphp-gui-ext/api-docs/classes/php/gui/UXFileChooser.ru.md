# UXFileChooser

- **класс** `UXFileChooser` (`php\gui\UXFileChooser`)
- **пакет** `gui`
- **исходники** `php/gui/UXFileChooser.php`

**Описание**

Class UXFileChooser

---

#### Свойства

- `->`[`title`](#prop-title) : `string`
- `->`[`initialDirectory`](#prop-initialdirectory) : `File|string`
- `->`[`initialFileName`](#prop-initialfilename) : `string`
- `->`[`selectedExtensionFilter`](#prop-selectedextensionfilter) : `int`
- `->`[`extensionFilters`](#prop-extensionfilters) : `array [description, extensions: [...]]`

---

#### Методы

- `->`[`execute()`](#method-execute)
- `->`[`showOpenDialog()`](#method-showopendialog)
- `->`[`showSaveDialog()`](#method-showsavedialog)
- `->`[`showOpenMultipleDialog()`](#method-showopenmultipledialog)

---
# Методы

<a name="method-execute"></a>

### execute()
```php
execute(): File
```

---

<a name="method-showopendialog"></a>

### showOpenDialog()
```php
showOpenDialog(php\gui\UXWindow $window): File
```

---

<a name="method-showsavedialog"></a>

### showSaveDialog()
```php
showSaveDialog(php\gui\UXWindow $window): File
```

---

<a name="method-showopenmultipledialog"></a>

### showOpenMultipleDialog()
```php
showOpenMultipleDialog(php\gui\UXWindow $window): File[]
```
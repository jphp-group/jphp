# UXPrinter

- **класс** `UXPrinter` (`php\gui\printing\UXPrinter`)
- **пакет** `gui`
- **исходники** `php/gui/printing/UXPrinter.php`

---

#### Свойства

- `->`[`name`](#prop-name) : `string`
- `->`[`attributes`](#prop-attributes) : `array`

---

#### Статичные Методы

- `UXPrinter ::`[`getDefault()`](#method-getdefault)
- `UXPrinter ::`[`getAll()`](#method-getall)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`createPrintJob()`](#method-createprintjob)
- `->`[`print()`](#method-print)
- `->`[`printWithDialog()`](#method-printwithdialog)

---
# Статичные Методы

<a name="method-getdefault"></a>

### getDefault()
```php
UXPrinter::getDefault(): UXPrinter
```

---

<a name="method-getall"></a>

### getAll()
```php
UXPrinter::getAll(): UXPrinter[]
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-createprintjob"></a>

### createPrintJob()
```php
createPrintJob(): UXPrinterJob
```

---

<a name="method-print"></a>

### print()
```php
print(php\gui\UXNode $node): bool
```

---

<a name="method-printwithdialog"></a>

### printWithDialog()
```php
printWithDialog(php\gui\UXWindow $ownerWindow, php\gui\UXNode $node): bool
```
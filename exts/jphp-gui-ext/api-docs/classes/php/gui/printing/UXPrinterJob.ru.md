# UXPrinterJob

- **класс** `UXPrinterJob` (`php\gui\printing\UXPrinterJob`)
- **исходники** `php/gui/printing/UXPrinterJob.php`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXPrinterJob constructor._
- `->`[`getPrinter()`](#method-getprinter)
- `->`[`getJobStatus()`](#method-getjobstatus) - _NOT_STARTED, PRINTING, CANCELED, ERROR, DONE._
- `->`[`print()`](#method-print)
- `->`[`cancel()`](#method-cancel) - _Cancel print job._
- `->`[`end()`](#method-end) - _Finish print job._
- `->`[`showPrintDialog()`](#method-showprintdialog)
- `->`[`showPageSetupDialog()`](#method-showpagesetupdialog)
- `->`[`setSettings()`](#method-setsettings) - _Settings is an array like:_

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\printing\UXPrinter $printer): void
```
UXPrinterJob constructor.

---

<a name="method-getprinter"></a>

### getPrinter()
```php
getPrinter(): UXPrinter
```

---

<a name="method-getjobstatus"></a>

### getJobStatus()
```php
getJobStatus(): string
```
NOT_STARTED, PRINTING, CANCELED, ERROR, DONE.

---

<a name="method-print"></a>

### print()
```php
print(php\gui\UXNode $node): bool
```

---

<a name="method-cancel"></a>

### cancel()
```php
cancel(): void
```
Cancel print job.

---

<a name="method-end"></a>

### end()
```php
end(): bool
```
Finish print job.

---

<a name="method-showprintdialog"></a>

### showPrintDialog()
```php
showPrintDialog(php\gui\UXWindow $ownerWindow): bool
```

---

<a name="method-showpagesetupdialog"></a>

### showPageSetupDialog()
```php
showPageSetupDialog(php\gui\UXWindow $ownerWindow): bool
```

---

<a name="method-setsettings"></a>

### setSettings()
```php
setSettings(array $settings): void
```
Settings is an array like:

jobName => string,
copies => int,
collation => string (UNCOLLATED, COLLATED)
printSides => string (ONE_SIDED, DUPLEX, TUMBLE)
printColor => string (COLOR, MONOCHROME)
printQuality => string (DRAFT, LOW, NORMAL, HIGH)
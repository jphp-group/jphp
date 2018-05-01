# UXClipboard

- **класс** `UXClipboard` (`php\gui\UXClipboard`)
- **пакет** `gui`
- **исходники** `php/gui/UXClipboard.php`

**Описание**

Класс для работы с буфером обмена.

---

#### Статичные Методы

- `UXClipboard ::`[`clear()`](#method-clear) - _Очистить буфер._
- `UXClipboard ::`[`hasText()`](#method-hastext) - _Возвращает true если буфер содержит какой-то текст._
- `UXClipboard ::`[`hasUrl()`](#method-hasurl)
- `UXClipboard ::`[`hasHtml()`](#method-hashtml)
- `UXClipboard ::`[`hasImage()`](#method-hasimage)
- `UXClipboard ::`[`hasFiles()`](#method-hasfiles)
- `UXClipboard ::`[`getHtml()`](#method-gethtml)
- `UXClipboard ::`[`getUrl()`](#method-geturl)
- `UXClipboard ::`[`getText()`](#method-gettext)
- `UXClipboard ::`[`getImage()`](#method-getimage)
- `UXClipboard ::`[`getFiles()`](#method-getfiles)
- `UXClipboard ::`[`setText()`](#method-settext) - _Помещает в буфер указанный текст._
- `UXClipboard ::`[`setContent()`](#method-setcontent)

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-clear"></a>

### clear()
```php
UXClipboard::clear(): void
```
Очистить буфер.

---

<a name="method-hastext"></a>

### hasText()
```php
UXClipboard::hasText(): bool
```
Возвращает true если буфер содержит какой-то текст.

---

<a name="method-hasurl"></a>

### hasUrl()
```php
UXClipboard::hasUrl(): bool
```

---

<a name="method-hashtml"></a>

### hasHtml()
```php
UXClipboard::hasHtml(): bool
```

---

<a name="method-hasimage"></a>

### hasImage()
```php
UXClipboard::hasImage(): bool
```

---

<a name="method-hasfiles"></a>

### hasFiles()
```php
UXClipboard::hasFiles(): bool
```

---

<a name="method-gethtml"></a>

### getHtml()
```php
UXClipboard::getHtml(): string
```

---

<a name="method-geturl"></a>

### getUrl()
```php
UXClipboard::getUrl(): string
```

---

<a name="method-gettext"></a>

### getText()
```php
UXClipboard::getText(): string
```

---

<a name="method-getimage"></a>

### getImage()
```php
UXClipboard::getImage(): UXImage
```

---

<a name="method-getfiles"></a>

### getFiles()
```php
UXClipboard::getFiles(): File[]
```

---

<a name="method-settext"></a>

### setText()
```php
UXClipboard::setText(string $text): void
```
Помещает в буфер указанный текст.

---

<a name="method-setcontent"></a>

### setContent()
```php
UXClipboard::setContent(array $content): void
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
# Email

- **класс** `Email` (`php\mail\Email`)
- **исходники** `php/mail/Email.php`

**Описание**

Class Email

---

#### Методы

- `->`[`setFrom()`](#method-setfrom)
- `->`[`setCharset()`](#method-setcharset)
- `->`[`setSubject()`](#method-setsubject)
- `->`[`setTo()`](#method-setto)
- `->`[`setCc()`](#method-setcc)
- `->`[`setBcc()`](#method-setbcc)
- `->`[`setBounceAddress()`](#method-setbounceaddress)
- `->`[`setHeaders()`](#method-setheaders)
- `->`[`setMessage()`](#method-setmessage)
- `->`[`setHtmlMessage()`](#method-sethtmlmessage)
- `->`[`setTextMessage()`](#method-settextmessage)
- `->`[`attach()`](#method-attach)
- `->`[`send()`](#method-send) - _Sends the email. Internally we build a MimeMessage_

---
# Методы

<a name="method-setfrom"></a>

### setFrom()
```php
setFrom(string $email, string $name, string $charset): $this
```

---

<a name="method-setcharset"></a>

### setCharset()
```php
setCharset(string $charset): $this
```

---

<a name="method-setsubject"></a>

### setSubject()
```php
setSubject(string $subject): $this
```

---

<a name="method-setto"></a>

### setTo()
```php
setTo(array $addresses): $this
```

---

<a name="method-setcc"></a>

### setCc()
```php
setCc(array $addresses): $this
```

---

<a name="method-setbcc"></a>

### setBcc()
```php
setBcc(array $addresses): $this
```

---

<a name="method-setbounceaddress"></a>

### setBounceAddress()
```php
setBounceAddress(array $email): $this
```

---

<a name="method-setheaders"></a>

### setHeaders()
```php
setHeaders(array $headers): $this
```

---

<a name="method-setmessage"></a>

### setMessage()
```php
setMessage(string $message): $this
```

---

<a name="method-sethtmlmessage"></a>

### setHtmlMessage()
```php
setHtmlMessage(string $message): $this
```

---

<a name="method-settextmessage"></a>

### setTextMessage()
```php
setTextMessage(string $message): $this
```

---

<a name="method-attach"></a>

### attach()
```php
attach(string|File|Stream $content, string $contentType, string $name, string $description): $this
```

---

<a name="method-send"></a>

### send()
```php
send(php\mail\EmailBackend $backend): string
```
Sends the email. Internally we build a MimeMessage
which is afterwards sent to the SMTP server.
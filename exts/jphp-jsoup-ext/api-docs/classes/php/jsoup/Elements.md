# Elements

- **class** `Elements` (`php\jsoup\Elements`)
- **source** `php/jsoup/Elements.php`

**Description**

Class Elements

---

#### Methods

- `->`[`text()`](#method-text)
- `->`[`hasText()`](#method-hastext)
- `->`[`html()`](#method-html)
- `->`[`outerHtml()`](#method-outerhtml)
- `->`[`attr()`](#method-attr)
- `->`[`hasAttr()`](#method-hasattr)
- `->`[`removeAttr()`](#method-removeattr)
- `->`[`addClass()`](#method-addclass)
- `->`[`removeClass()`](#method-removeclass)
- `->`[`hasClass()`](#method-hasclass)
- `->`[`toggleClass()`](#method-toggleclass)
- `->`[`val()`](#method-val)
- `->`[`prepend()`](#method-prepend)
- `->`[`append()`](#method-append)
- `->`[`before()`](#method-before)
- `->`[`after()`](#method-after)
- `->`[`select()`](#method-select)
- `->`[`first()`](#method-first)
- `->`[`last()`](#method-last)
- `->`[`not()`](#method-not)
- `->`[`is()`](#method-is)
- `->`[`parents()`](#method-parents)

---
# Methods

<a name="method-text"></a>

### text()
```php
text(): string
```

---

<a name="method-hastext"></a>

### hasText()
```php
hasText(): bool
```

---

<a name="method-html"></a>

### html()
```php
html(string $html): string
```

---

<a name="method-outerhtml"></a>

### outerHtml()
```php
outerHtml(): string
```

---

<a name="method-attr"></a>

### attr()
```php
attr(string $attributeKey, mixed $value): string|$this
```

---

<a name="method-hasattr"></a>

### hasAttr()
```php
hasAttr(string $attributeKey): bool
```

---

<a name="method-removeattr"></a>

### removeAttr()
```php
removeAttr(string $attributeKey): Elements
```

---

<a name="method-addclass"></a>

### addClass()
```php
addClass(string $class): Elements
```

---

<a name="method-removeclass"></a>

### removeClass()
```php
removeClass(string $class): Elements
```

---

<a name="method-hasclass"></a>

### hasClass()
```php
hasClass(string $class): bool
```

---

<a name="method-toggleclass"></a>

### toggleClass()
```php
toggleClass(string $class): bool
```

---

<a name="method-val"></a>

### val()
```php
val(string $value): $this
```

---

<a name="method-prepend"></a>

### prepend()
```php
prepend(string $html): Elements
```

---

<a name="method-append"></a>

### append()
```php
append(string $html): Elements
```

---

<a name="method-before"></a>

### before()
```php
before(string $html): Elements
```

---

<a name="method-after"></a>

### after()
```php
after(string $html): Elements
```

---

<a name="method-select"></a>

### select()
```php
select(string $query): Elements
```

---

<a name="method-first"></a>

### first()
```php
first(): Element
```

---

<a name="method-last"></a>

### last()
```php
last(): Element
```

---

<a name="method-not"></a>

### not()
```php
not(string $query): Elements
```

---

<a name="method-is"></a>

### is()
```php
is(string $query): bool
```

---

<a name="method-parents"></a>

### parents()
```php
parents(): Elements
```
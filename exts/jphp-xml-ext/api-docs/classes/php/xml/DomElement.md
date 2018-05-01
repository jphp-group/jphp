# DomElement

- **class** `DomElement` (`php\xml\DomElement`) **extends** [`DomNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomNode.md)
- **source** `php/xml/DomElement.php`

**Child Classes**

> [DomDocument](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomDocument.md)

**Description**

Class DomElement

---

#### Methods

- `->`[`__get()`](#method-__get)
- `->`[`__set()`](#method-__set) - _Set attribute value_
- `->`[`__unset()`](#method-__unset) - _Remove attribute by name_
- `->`[`__isset()`](#method-__isset) - _Check attribute exists by name_
- `->`[`getTagName()`](#method-gettagname)
- `->`[`getAttribute()`](#method-getattribute)
- `->`[`hasAttribute()`](#method-hasattribute)
- `->`[`hasAttributeNS()`](#method-hasattributens)
- `->`[`setAttribute()`](#method-setattribute)
- `->`[`setAttributes()`](#method-setattributes)
- `->`[`getAttributes()`](#method-getattributes)
- `->`[`removeAttribute()`](#method-removeattribute)
- `->`[`getElementsByTagName()`](#method-getelementsbytagname)
- `->`[`getElementsByTagNameNS()`](#method-getelementsbytagnamens)
- `->`[`getAttributeNS()`](#method-getattributens)
- `->`[`setAttributeNS()`](#method-setattributens)
- `->`[`removeAttributeNS()`](#method-removeattributens)
- `->`[`setIdAttribute()`](#method-setidattribute)
- `->`[`setIdAttributeNS()`](#method-setidattributens)
- See also in the parent class [DomNode](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomNode.md)

---
# Methods

<a name="method-__get"></a>

### __get()
```php
__get(string $name): string
```

---

<a name="method-__set"></a>

### __set()
```php
__set(string $name, string $value): void
```
Set attribute value

---

<a name="method-__unset"></a>

### __unset()
```php
__unset(string $name): void
```
Remove attribute by name

---

<a name="method-__isset"></a>

### __isset()
```php
__isset(mixed $name): bool
```
Check attribute exists by name

---

<a name="method-gettagname"></a>

### getTagName()
```php
getTagName(): string
```

---

<a name="method-getattribute"></a>

### getAttribute()
```php
getAttribute(string $name): string
```

---

<a name="method-hasattribute"></a>

### hasAttribute()
```php
hasAttribute(string $name): bool
```

---

<a name="method-hasattributens"></a>

### hasAttributeNS()
```php
hasAttributeNS(string $namespaceURI, string $localName): bool
```

---

<a name="method-setattribute"></a>

### setAttribute()
```php
setAttribute(string $name, string $value): void
```

---

<a name="method-setattributes"></a>

### setAttributes()
```php
setAttributes(array|Traversable $attributes): void
```

---

<a name="method-getattributes"></a>

### getAttributes()
```php
getAttributes(): array
```

---

<a name="method-removeattribute"></a>

### removeAttribute()
```php
removeAttribute(string $name): void
```

---

<a name="method-getelementsbytagname"></a>

### getElementsByTagName()
```php
getElementsByTagName(string $name): DomNodeList
```

---

<a name="method-getelementsbytagnamens"></a>

### getElementsByTagNameNS()
```php
getElementsByTagNameNS(string $namespaceURI, string $localName): DomNodeList
```

---

<a name="method-getattributens"></a>

### getAttributeNS()
```php
getAttributeNS(string $namespaceURI, string $localName): string
```

---

<a name="method-setattributens"></a>

### setAttributeNS()
```php
setAttributeNS(string $namespaceURI, string $qualifiedName, string $value): void
```

---

<a name="method-removeattributens"></a>

### removeAttributeNS()
```php
removeAttributeNS(string $namespaceURI, string $localName): void
```

---

<a name="method-setidattribute"></a>

### setIdAttribute()
```php
setIdAttribute(string $name, bool $isId): void
```

---

<a name="method-setidattributens"></a>

### setIdAttributeNS()
```php
setIdAttributeNS(string $namespaceURI, string $localName, string $isId): void
```
# DomDocument

- **class** `DomDocument` (`php\xml\DomDocument`) **extends** [`DomElement`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomElement.md)
- **source** `php/xml/DomDocument.php`

**Description**

Class DomDocument

---

#### Methods

- `->`[`getDocumentElement()`](#method-getdocumentelement)
- `->`[`getElementById()`](#method-getelementbyid)
- `->`[`getInputEncoding()`](#method-getinputencoding)
- `->`[`getXmlEncoding()`](#method-getxmlencoding)
- `->`[`getXmlVersion()`](#method-getxmlversion)
- `->`[`getXmlStandalone()`](#method-getxmlstandalone)
- `->`[`setXmlStandalone()`](#method-setxmlstandalone)
- `->`[`getStrictErrorChecking()`](#method-getstricterrorchecking)
- `->`[`setStrictErrorChecking()`](#method-setstricterrorchecking)
- `->`[`getDocumentURI()`](#method-getdocumenturi)
- `->`[`setDocumentURI()`](#method-setdocumenturi)
- `->`[`createElement()`](#method-createelement)
- `->`[`createElementNS()`](#method-createelementns)
- `->`[`createProcessingInstruction()`](#method-createprocessinginstruction)
- `->`[`importNode()`](#method-importnode)
- `->`[`importElement()`](#method-importelement)
- `->`[`adoptNode()`](#method-adoptnode)
- `->`[`renameNode()`](#method-renamenode)
- `->`[`normalizeDocument()`](#method-normalizedocument)
- See also in the parent class [DomElement](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomElement.md)

---
# Methods

<a name="method-getdocumentelement"></a>

### getDocumentElement()
```php
getDocumentElement(): DomDocument
```

---

<a name="method-getelementbyid"></a>

### getElementById()
```php
getElementById(string $id): DomElement
```

---

<a name="method-getinputencoding"></a>

### getInputEncoding()
```php
getInputEncoding(): string
```

---

<a name="method-getxmlencoding"></a>

### getXmlEncoding()
```php
getXmlEncoding(): string
```

---

<a name="method-getxmlversion"></a>

### getXmlVersion()
```php
getXmlVersion(): string
```

---

<a name="method-getxmlstandalone"></a>

### getXmlStandalone()
```php
getXmlStandalone(): bool
```

---

<a name="method-setxmlstandalone"></a>

### setXmlStandalone()
```php
setXmlStandalone(bool $value): void
```

---

<a name="method-getstricterrorchecking"></a>

### getStrictErrorChecking()
```php
getStrictErrorChecking(): bool
```

---

<a name="method-setstricterrorchecking"></a>

### setStrictErrorChecking()
```php
setStrictErrorChecking(bool $value): void
```

---

<a name="method-getdocumenturi"></a>

### getDocumentURI()
```php
getDocumentURI(): string
```

---

<a name="method-setdocumenturi"></a>

### setDocumentURI()
```php
setDocumentURI(string $value): void
```

---

<a name="method-createelement"></a>

### createElement()
```php
createElement(string $tagName, Traversable|array $model): DomElement
```

---

<a name="method-createelementns"></a>

### createElementNS()
```php
createElementNS(string $namespaceURI, string $qualifiedName): DomElement
```

---

<a name="method-createprocessinginstruction"></a>

### createProcessingInstruction()
```php
createProcessingInstruction(string $name, string $value): DomNode
```

---

<a name="method-importnode"></a>

### importNode()
```php
importNode(php\xml\DomNode $importedNode, bool $deep): DomNode
```

---

<a name="method-importelement"></a>

### importElement()
```php
importElement(php\xml\DomElement $importedNode, bool $deep): DomElement
```

---

<a name="method-adoptnode"></a>

### adoptNode()
```php
adoptNode(php\xml\DomNode $source): DomNode
```

---

<a name="method-renamenode"></a>

### renameNode()
```php
renameNode(php\xml\DomNode $node, string $namespaceURI, string $qualifiedName): void
```

---

<a name="method-normalizedocument"></a>

### normalizeDocument()
```php
normalizeDocument(): void
```
# DomNode

- **класс** `DomNode` (`php\xml\DomNode`)
- **исходники** `php/xml/DomNode.php`

**Классы наследники**

> [DomElement](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-xml-ext/api-docs/classes/php/xml/DomElement.ru.md)

**Описание**

Class DomNode

---

#### Методы

- `->`[`get()`](#method-get)
- `->`[`find()`](#method-find)
- `->`[`findAll()`](#method-findall)
- `->`[`getBaseURI()`](#method-getbaseuri)
- `->`[`getNamespaceURI()`](#method-getnamespaceuri)
- `->`[`getLocalName()`](#method-getlocalname)
- `->`[`getNodeType()`](#method-getnodetype)
- `->`[`getNodeName()`](#method-getnodename)
- `->`[`getNodeValue()`](#method-getnodevalue)
- `->`[`getPrefix()`](#method-getprefix)
- `->`[`getTextContent()`](#method-gettextcontent)
- `->`[`getFirstChild()`](#method-getfirstchild)
- `->`[`getLastChild()`](#method-getlastchild)
- `->`[`getNextSibling()`](#method-getnextsibling)
- `->`[`getPreviousSibling()`](#method-getprevioussibling)
- `->`[`getParentNode()`](#method-getparentnode)
- `->`[`getOwnerDocument()`](#method-getownerdocument)
- `->`[`hasAttributes()`](#method-hasattributes)
- `->`[`hasChildNodes()`](#method-haschildnodes)
- `->`[`getChildNodes()`](#method-getchildnodes)
- `->`[`isDefaultNamespace()`](#method-isdefaultnamespace)
- `->`[`isEqualNode()`](#method-isequalnode)
- `->`[`isSameNode()`](#method-issamenode)
- `->`[`isSupported()`](#method-issupported)
- `->`[`lookupNamespaceURI()`](#method-lookupnamespaceuri)
- `->`[`lookupPrefix()`](#method-lookupprefix)
- `->`[`normalize()`](#method-normalize)
- `->`[`setTextContent()`](#method-settextcontent)
- `->`[`setPrefix()`](#method-setprefix)
- `->`[`cloneNode()`](#method-clonenode)
- `->`[`appendChild()`](#method-appendchild)
- `->`[`removeChild()`](#method-removechild)
- `->`[`replaceChild()`](#method-replacechild)
- `->`[`insertBefore()`](#method-insertbefore)
- `->`[`toModel()`](#method-tomodel)

---
# Методы

<a name="method-get"></a>

### get()
```php
get(string $xpathExpression): string
```

---

<a name="method-find"></a>

### find()
```php
find(string $xpathExpression): DomNode
```

---

<a name="method-findall"></a>

### findAll()
```php
findAll(string $xpathExpression): DomNodeList
```

---

<a name="method-getbaseuri"></a>

### getBaseURI()
```php
getBaseURI(): string
```

---

<a name="method-getnamespaceuri"></a>

### getNamespaceURI()
```php
getNamespaceURI(): string
```

---

<a name="method-getlocalname"></a>

### getLocalName()
```php
getLocalName(): string
```

---

<a name="method-getnodetype"></a>

### getNodeType()
```php
getNodeType(): int
```

---

<a name="method-getnodename"></a>

### getNodeName()
```php
getNodeName(): string
```

---

<a name="method-getnodevalue"></a>

### getNodeValue()
```php
getNodeValue(): string
```

---

<a name="method-getprefix"></a>

### getPrefix()
```php
getPrefix(): string
```

---

<a name="method-gettextcontent"></a>

### getTextContent()
```php
getTextContent(): string
```

---

<a name="method-getfirstchild"></a>

### getFirstChild()
```php
getFirstChild(): DomNode
```

---

<a name="method-getlastchild"></a>

### getLastChild()
```php
getLastChild(): DomNode
```

---

<a name="method-getnextsibling"></a>

### getNextSibling()
```php
getNextSibling(): DomNode
```

---

<a name="method-getprevioussibling"></a>

### getPreviousSibling()
```php
getPreviousSibling(): DomNode
```

---

<a name="method-getparentnode"></a>

### getParentNode()
```php
getParentNode(): DomNode
```

---

<a name="method-getownerdocument"></a>

### getOwnerDocument()
```php
getOwnerDocument(): DomDocument
```

---

<a name="method-hasattributes"></a>

### hasAttributes()
```php
hasAttributes(): bool
```

---

<a name="method-haschildnodes"></a>

### hasChildNodes()
```php
hasChildNodes(): bool
```

---

<a name="method-getchildnodes"></a>

### getChildNodes()
```php
getChildNodes(): DomNodeList
```

---

<a name="method-isdefaultnamespace"></a>

### isDefaultNamespace()
```php
isDefaultNamespace(string $namespace): void
```

---

<a name="method-isequalnode"></a>

### isEqualNode()
```php
isEqualNode(php\xml\DomNode $node): bool
```

---

<a name="method-issamenode"></a>

### isSameNode()
```php
isSameNode(php\xml\DomNode $node): bool
```

---

<a name="method-issupported"></a>

### isSupported()
```php
isSupported(string $feature, string $version): bool
```

---

<a name="method-lookupnamespaceuri"></a>

### lookupNamespaceURI()
```php
lookupNamespaceURI(string $prefix): string
```

---

<a name="method-lookupprefix"></a>

### lookupPrefix()
```php
lookupPrefix(string $namespaceURI): string
```

---

<a name="method-normalize"></a>

### normalize()
```php
normalize(): void
```

---

<a name="method-settextcontent"></a>

### setTextContent()
```php
setTextContent(string $content): void
```

---

<a name="method-setprefix"></a>

### setPrefix()
```php
setPrefix(string $prefix): void
```

---

<a name="method-clonenode"></a>

### cloneNode()
```php
cloneNode(bool $deep): DomNode
```

---

<a name="method-appendchild"></a>

### appendChild()
```php
appendChild(php\xml\DomNode $node): $this
```

---

<a name="method-removechild"></a>

### removeChild()
```php
removeChild(php\xml\DomNode $node): $this
```

---

<a name="method-replacechild"></a>

### replaceChild()
```php
replaceChild(php\xml\DomNode $newNode, php\xml\DomNode $oldNode): $this
```

---

<a name="method-insertbefore"></a>

### insertBefore()
```php
insertBefore(php\xml\DomNode $newNode, php\xml\DomNode $refNode): $this
```

---

<a name="method-tomodel"></a>

### toModel()
```php
toModel(): array
```
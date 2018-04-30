# UXTreeView

- **класс** `UXTreeView` (`php\gui\UXTreeView`) **унаследован от** [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)
- **пакет** `gui`
- **исходники** [`php/gui/UXTreeView.php`](./src/main/resources/JPHP-INF/sdk/php/gui/UXTreeView.php)

**Описание**

Class UXTreeView

---

#### Свойства

- `->`[`editable`](#prop-editable) : `bool`
- `->`[`root`](#prop-root) : `UXTreeItem`
- `->`[`rootVisible`](#prop-rootvisible) : `bool`
- `->`[`fixedCellSize`](#prop-fixedcellsize) : `double|int`
- `->`[`expandedItemCount`](#prop-expandeditemcount) : `int`
- `->`[`multipleSelection`](#prop-multipleselection) : `bool`
- `->`[`selectedItems`](#prop-selecteditems) : `UXTreeItem[]`
- `->`[`selectedIndexes`](#prop-selectedindexes) : `int[]`
- `->`[`focusedItem`](#prop-focuseditem) : `UXTreeItem`
- `->`[`expandedItems`](#prop-expandeditems) : `UXTreeItem[]`

---

#### Методы

- `->`[`getTreeItem()`](#method-gettreeitem)
- `->`[`getTreeItemIndex()`](#method-gettreeitemindex)
- `->`[`getTreeItemLevel()`](#method-gettreeitemlevel)
- `->`[`isTreeItemFocused()`](#method-istreeitemfocused)
- `->`[`edit()`](#method-edit)
- `->`[`scrollTo()`](#method-scrollto)
- `->`[`expandAll()`](#method-expandall)
- `->`[`collapseAll()`](#method-collapseall)

---
# Методы

<a name="method-gettreeitem"></a>

### getTreeItem()
```php
getTreeItem(int $index): UXTreeItem
```

---

<a name="method-gettreeitemindex"></a>

### getTreeItemIndex()
```php
getTreeItemIndex(php\gui\UXTreeItem $item): int
```

---

<a name="method-gettreeitemlevel"></a>

### getTreeItemLevel()
```php
getTreeItemLevel(php\gui\UXTreeItem $item): int
```

---

<a name="method-istreeitemfocused"></a>

### isTreeItemFocused()
```php
isTreeItemFocused(php\gui\UXTreeItem $item): bool
```

---

<a name="method-edit"></a>

### edit()
```php
edit(php\gui\UXTreeItem $item): void
```

---

<a name="method-scrollto"></a>

### scrollTo()
```php
scrollTo(php\gui\UXTreeItem $item): void
```

---

<a name="method-expandall"></a>

### expandAll()
```php
expandAll(): void
```

---

<a name="method-collapseall"></a>

### collapseAll()
```php
collapseAll(): void
```
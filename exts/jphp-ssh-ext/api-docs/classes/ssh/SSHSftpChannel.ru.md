# SSHSftpChannel

- **класс** `SSHSftpChannel` (`ssh\SSHSftpChannel`) **унаследован от** [`SSHChannel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.ru.md)
- **пакет** `ssh`
- **исходники** `ssh/SSHSftpChannel.php`

**Описание**

Class SSHSftpChannel

---

#### Свойства

- `->`[`home`](#prop-home) : `string`
- `->`[`version`](#prop-version) : `string`
- `->`[`serverVersion`](#prop-serverversion) : `int`
- `->`[`bulkRequests`](#prop-bulkrequests) : `int`
- *См. также в родительском классе* [SSHChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.ru.md).

---

#### Методы

- `->`[`pwd()`](#method-pwd)
- `->`[`lpwd()`](#method-lpwd)
- `->`[`cd()`](#method-cd)
- `->`[`lcd()`](#method-lcd)
- `->`[`chgrp()`](#method-chgrp)
- `->`[`chmod()`](#method-chmod)
- `->`[`chown()`](#method-chown)
- `->`[`ls()`](#method-ls)
- `->`[`rename()`](#method-rename)
- `->`[`symlink()`](#method-symlink)
- `->`[`hardlink()`](#method-hardlink)
- `->`[`mkdir()`](#method-mkdir)
- `->`[`rmdir()`](#method-rmdir)
- `->`[`rm()`](#method-rm)
- `->`[`realpath()`](#method-realpath)
- `->`[`readlink()`](#method-readlink)
- `->`[`stat()`](#method-stat)
- `->`[`lstat()`](#method-lstat)
- `->`[`get()`](#method-get)
- `->`[`put()`](#method-put)
- `->`[`getExtension()`](#method-getextension)
- См. также в родительском классе [SSHChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.ru.md)

---
# Методы

<a name="method-pwd"></a>

### pwd()
```php
pwd(): string
```

---

<a name="method-lpwd"></a>

### lpwd()
```php
lpwd(): string
```

---

<a name="method-cd"></a>

### cd()
```php
cd(string $path): void
```

---

<a name="method-lcd"></a>

### lcd()
```php
lcd(string $path): void
```

---

<a name="method-chgrp"></a>

### chgrp()
```php
chgrp(int $gid, string $path): void
```

---

<a name="method-chmod"></a>

### chmod()
```php
chmod(int $permissions, string $path): void
```

---

<a name="method-chown"></a>

### chown()
```php
chown(int $uid, string $path): void
```

---

<a name="method-ls"></a>

### ls()
```php
ls(string $path, callable $lsSelector): array[]
```

---

<a name="method-rename"></a>

### rename()
```php
rename(string $oldpath, string $newpath): void
```

---

<a name="method-symlink"></a>

### symlink()
```php
symlink(string $oldpath, string $newpath): void
```

---

<a name="method-hardlink"></a>

### hardlink()
```php
hardlink(string $oldpath, string $newpath): void
```

---

<a name="method-mkdir"></a>

### mkdir()
```php
mkdir(string $dir): void
```

---

<a name="method-rmdir"></a>

### rmdir()
```php
rmdir(string $dir): void
```

---

<a name="method-rm"></a>

### rm()
```php
rm(string $path): void
```

---

<a name="method-realpath"></a>

### realpath()
```php
realpath(string $path): string
```

---

<a name="method-readlink"></a>

### readlink()
```php
readlink(string $path): string
```

---

<a name="method-stat"></a>

### stat()
```php
stat(string $path): array|null
```

---

<a name="method-lstat"></a>

### lstat()
```php
lstat(string $path): array|null
```

---

<a name="method-get"></a>

### get()
```php
get(string $src, int $skip, callable|null $progressHandler): php\io\MiscStream
```

---

<a name="method-put"></a>

### put()
```php
put(string $src, string $mode, callable|null $progressHandler, int $offset): php\io\MiscStream
```

---

<a name="method-getextension"></a>

### getExtension()
```php
getExtension(string $key): string
```
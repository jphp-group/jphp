# fs

- **class** `fs` (`php\lib\fs`)
- **package** `std`
- **source** [`php/lib/fs.php`](./src/main/resources/JPHP-INF/sdk/php/lib/fs.php)

**Description**

File System class.

Class fs

---

#### Static Methods

- `fs ::`[`separator()`](#method-separator) - _Return the local filesystem's name-separator character._
- `fs ::`[`pathSeparator()`](#method-pathseparator) - _Return the local filesystem's path-separator character._
- `fs ::`[`valid()`](#method-valid) - _Validate file name._
- `fs ::`[`abs()`](#method-abs) - _Returns absolute real path._
- `fs ::`[`name()`](#method-name) - _Returns name of the path._
- `fs ::`[`nameNoExt()`](#method-namenoext) - _Returns name of the path without extension._
- `fs ::`[`pathNoExt()`](#method-pathnoext) - _Returns path without extension._
- `fs ::`[`relativize()`](#method-relativize)
- `fs ::`[`ext()`](#method-ext) - _Returns extension of path._
- `fs ::`[`hasExt()`](#method-hasext) - _Check that $path has an extension from the extension set._
- `fs ::`[`parent()`](#method-parent) - _Returns parent directory._
- `fs ::`[`ensureParent()`](#method-ensureparent) - _Checks parent of path and if it is not exists, tries to create parent directory._
- `fs ::`[`normalize()`](#method-normalize) - _Normalizes file path for current OS._
- `fs ::`[`exists()`](#method-exists) - _Checks that file is exists._
- `fs ::`[`size()`](#method-size) - _Returns size of file in bytes._
- `fs ::`[`isFile()`](#method-isfile) - _Checks that path is file._
- `fs ::`[`isDir()`](#method-isdir) - _Checks that path is directory._
- `fs ::`[`isHidden()`](#method-ishidden) - _Checks that path is hidden._
- `fs ::`[`time()`](#method-time) - _Returns last modification time of file or directory._
- `fs ::`[`makeDir()`](#method-makedir) - _Creates empty directory (mkdirs) if not exists._
- `fs ::`[`makeFile()`](#method-makefile) - _Creates empty file, if file already exists then rewrite it._
- `fs ::`[`delete()`](#method-delete) - _Deletes file or empty directory._
- `fs ::`[`clean()`](#method-clean) - _Deletes all files in path. This method does not delete the $path directory._
- `fs ::`[`scan()`](#method-scan) - _Scans the path with callback or array filter and can returns found list_
- `fs ::`[`crc32()`](#method-crc32) - _Calculates crc32 sum of file or stream, returns null if it's failed._
- `fs ::`[`hash()`](#method-hash) - _Calculates hash of file or stream._
- `fs ::`[`copy()`](#method-copy) - _Copies $source stream to $dest stream._
- `fs ::`[`move()`](#method-move) - _Renames or moves a file or empty dir._
- `fs ::`[`rename()`](#method-rename) - _Set name for file, returns true if success._
- `fs ::`[`get()`](#method-get) - _Reads fully data from source and returns it as binary string._
- `fs ::`[`parseAs()`](#method-parseas) - _Read fully data from source, parse as format and return result._
- `fs ::`[`parse()`](#method-parse) - _Read fully data from source, parse as format by extensions and return result._
- `fs ::`[`formatAs()`](#method-formatas) - _Write formatted data to source (path)._
- `fs ::`[`format()`](#method-format) - _Write formatted (based on path extension) data to source (path)._
- `fs ::`[`match()`](#method-match) - _Tells if given path matches this matcher's pattern._
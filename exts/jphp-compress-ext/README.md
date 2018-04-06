## jphp-compress-ext

> Library for working with archive files and streams, for compressing and uncompressing data.

Supported archive formats:

1. Tar
2. Zip
3. Gzip (*.gz)
4. BZ2 (*.bz2)
5. Lz4 (framed & block).

### Classes:

- namespace: `compress`.
- classes:
  - `Archive` - abstract archive class.
    - `TarArchive` - class for working with tar archives.
    - `ZipArchive` - class for working with zip archives.
  - `ArchiveEntry` - abstract entry of archive.
    - `TarArchiveEntry` - entry of tar archives.
    - `ZipArchiveEntry` - entry of zip archives.
  - `ArchiveInput` - abstract basic class for reading archive.
    - `TarArchiveInput` - basic class for reading tar files.
    - `ZipArchiveInput` - basic class for reading zip files.
  - `ArchiveOutput` - abstract basic class for writing archive.
    - `TarArchiveOutput` - basic class for writing tar files.
    - `ZipArchiveOutput` - basic class for writing zip files.
  - `Bzip2InputStream` - stream class for bz2 un-compressing.
  - `Bzip2OutputStream` - stream class for bz2 compressing.
  - `GzipInputStream` - stream class for gz un-compressing.
  - `GzipOutputStream` - stream class for gz compressing.
  - `Lz4InputStream` - stream class for lz4 un-compressing.
  - `Lz4OutputStream` - stream class for lz4 compressing.

### How to use?

1. Read tar.gz archive:

```php
use compress, std;

$tar = new TarArchive(new GzipInputStream('file.tar.gz'));

// unpack all
$tar->readAll(function (TarArchiveEntry $entry, ?Stream $stream) {
    echo "Unpack: ", $entry->name, "\n";
    fs::copy($stream, '/path/to/dir/' . $entry->name); 
});

// unpack one
$tar->read('file.txt', function (TarArchiveEntry $entry, ?Stream $stream) {
    echo "Unpack: ", $entry->name, "\n";
    var_dump($stream->readAll());
});
```

2. Write tar.gz archive:
```php
use compress, std;

$tar = new TarArchive(new GzipOutputStream('file.tar.gz'));
$tar->open(); // open for write.

// from string
$tar->addFromString(new TarArchiveEntry('file.txt'), 'Hello World'));

// add file
$tar->addFile('path/to/file', 'file', function ($copied, $n) { 
    // copy progress
}, 1024 * 8 /* buffer size */); 

// add stream
$tar->addEntry(new TarArchiveEntry('file.txt', 5 /* size of stream */), new MemoryStream('hello'));

$tar->close(); // save.
```

3. Compress string to gz format:
```php
use compress, std;

$gz = new GzipOutputStream($stream = new MemoryStream());
$gz->write('hello world');
$gz->close();

$stream->seek(0);
$compressedString = $stream->readAll();
```

4. Un-compress from string, gz format:
```php
use compress, std;

$gz = new GzipInputStream(new MemoryStream($compressedString));
$uncompressedString = $gz->readAll();
```
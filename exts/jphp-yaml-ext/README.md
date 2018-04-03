## jphp-yaml-ext

> Library for working with yaml text format.

### How to use?

1. Read from string:
```php
use std;

$data = str::parseAs('name: value', 'yaml');

echo $data['name']; // value.
```

2. Write to string:
```php
use std;

$yamlString = str::formatAs(['name' => 'value'], 'yaml');

echo $yamlString;
```

3. Read from file:
```php
use std;

$data = fs::parseAs('path/to/file.yml', 'yaml');
```

4. Write to file:
```php
use std;

fs::formatAs('path/to/file.yml', ['name' => 'value'], 'yaml');
```

5. Read from stream:
```php
use std;

$stream = Stream::of('http://example.com/file.yml');
$data = $stream->parseAs('yaml');
```

6. Write to stream:
```php
use std;

$stream = Stream::of('file.yml', 'w+');
$data = $stream->writeFormatted(['name' => 'value'], 'yaml');
$stream->close();
```
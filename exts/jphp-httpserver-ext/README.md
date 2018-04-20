
## jphp-httpserver-ext

> Library for create http servers.

### How to use?

1. Run server with hello world page.
```php
use php\http\{HttpServer, HttpServerRequest, HttpServerResponse};

// create server at 8888 port, 127.0.0.1 host.
$server = new HttpServer(8888, '127.0.0.1'); // port & host.

// add route with method + path + handler.
$server->route('GET', '/hello-world', function (HttpServerRequest $req, HttpServerResponse $res) {
    $res->contentType('text/html');
    $res->body('Hello, <b>World</b>');
});

// run server.
$server->run();
```
Check it, open [http://localhost:8888/hello-world](http://localhost:8888/hello-world) in your browser.

2. Template routing.

```php
use php\http\{HttpServer, HttpServerRequest, HttpServerResponse};

// create server at 8888 port, 127.0.0.1 host.
$server = new HttpServer(8888, '127.0.0.1'); // port & host.

// add route with method + path + handler.
$server->route('GET', '/hello/{name}', function (HttpServerRequest $req, HttpServerResponse $res) {
    $name = $req->attribute('name');
    
    $res->contentType('text/html');
    $res->body("Hello, <b>$name</b>.");
});

// run server.
$server->run();
```

Check it, open [http://localhost:8888/hello/YourName](http://localhost:8888/hello/YourName) in your browser.

3. Start server in background:

```php
$server->runInBackground(); // run in background thread.
```

4. Stop server, check is running.
```php
if ($server->isRunning()) {
    $server->shutdown();
}
```
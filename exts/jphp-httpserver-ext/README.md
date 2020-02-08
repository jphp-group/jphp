
## jphp-httpserver-ext

> Library for create http servers.

### How to use?

0. **See** [API documentation](api-docs).

1. **Run server with hello world page**.
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

---

2. **Template routing**.

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

---

3. **Show client data**.
```php
use php\http\{HttpServer, HttpServerRequest, HttpServerResponse};

// create server at 8888 port, 127.0.0.1 host.
$server = new HttpServer(8888, '127.0.0.1'); // port & host.

// add route with method + path + handler.
$server->route('GET', '/client-data', function (HttpServerRequest $req, HttpServerResponse $res) {
    $res->contentType('text/html');
    $text = $req->attribute('text');
    $client_data = '<b>Remove Adress</b> - '.$req->remoteAddress().'<br>'; //Add Remote Adress to $client_data
    $client_data .= '<b>Headers</b>:<br>'.print_r($req->headers(),1).'<br>'; //Add Headers to $client_data
    $client_data .= '<b>LocalAddress</b> - '.$req->localAddress().'<br>'; //Add LocalAddress to $client_data
    $client_data .= '<b>LocalPort</b> - '.$req->localPort().'<br>'; //Add LocalPort to $client_data
    $client_data .= '<b>Cookies</b> - '.print_r($req->cookies(),1).'<br>'; //Add Cookies to $client_data
    $res->contentType('text/html');
    $res->body(
    	"You client data: <br>$client_data" //Show $client_data
    );
});

// run server.
$server->run();
```
Check it, open [http://localhost:8888/client-data](http://localhost:8888/client-data) in your browser.

---

4. **Query parameters**.
```php
use php\http\{HttpServer, HttpServerRequest, HttpServerResponse};

// create server at 8888 port, 127.0.0.1 host.
$server = new HttpServer(8888, '127.0.0.1'); // port & host.

// add route with method + path + handler.
$server->route('GET', '/demo-get', function (HttpServerRequest $req, HttpServerResponse $res) {
    $res->contentType('text/html');
    $name=$req->queryParameters()['name']; //get GET parameter "name"
    $res->body("You name: $name"); //Show get parameter "name"
});

// run server.
$server->run();
```
Check it, open [http://localhost:8888/demo-get?name=Mike](http://localhost:8888/demo-get?name=Mike) in your browser.

---

5. **Start server in background**:

```php
$server->runInBackground(); // run in background thread.
```

---

6. **Stop server, check is running**.
```php
if ($server->isRunning()) {
    $server->shutdown();
}
```

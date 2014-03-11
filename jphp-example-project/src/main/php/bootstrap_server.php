<?php
namespace {

    use php\concurrent\ExecutorService;
    use php\io\IOException;
    use php\lang\Environment;
    use php\net\ServerSocket;

    $server = new ServerSocket();
    $server->bind('localhost', 8080);
    $service = ExecutorService::newFixedThreadPool(5); // create a thread pool

    echo "Start HTTP Server on http://localhost:8080/ ...\n";

    while (true) {
        $socket = $server->accept();
        echo "Connect client ... \n";

        $service->execute(function() use ($socket) { // processing a http request in a thread
            ob_implicit_flush(true);

            $out = $socket->getOutput();
            try {
                $out->write("HTTP/1.1 200 OK\r\n");
                $out->write("Content-Type: text/html\r\n");
                $out->write("Connection: close\r\n\r\n");

                $out->write("Hello world!");
                $out->flush();
            } catch (IOException $e) {
                echo "Error: " . $e->getMessage(), "\n";
            } finally { // JPHP supports `finally` as in PHP 5.5
                $socket->shutdownOutput();
            }

        }, new Environment(null));
    }
}

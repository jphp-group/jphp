<?php

use php\webserver\WebRequest;
use php\webserver\WebResponse;

class Bootstrap {

    public static function run(WebRequest $request, WebResponse $response) {
        echo "Hello World!";
    }
}
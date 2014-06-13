<?php

namespace php\net;

/**
 * Class HttpRequest
 * @package php\net
 */
class HttpRequest {

    /**
     * @return HttpResponse
     */
    public function execute() { return new HttpResponse(); }
}

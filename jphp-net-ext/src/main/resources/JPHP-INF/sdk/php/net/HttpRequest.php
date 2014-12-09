<?php

namespace php\net;

/**
 * Class HttpRequest
 * @package php\net
 */
class HttpRequest {

    /**
     * @param HttpEntity|null $entity
     * @return HttpResponse
     */
    public function execute(HttpEntity $entity = null) { return new HttpResponse(); }
}

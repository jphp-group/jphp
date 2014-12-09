<?php
namespace php\net;

/**
 * Class HttpResponse
 * @package php\net
 */
class HttpResponse {

    public function __construct() { }

    /**
     * @return int
     */
    public function getStatusCode() { return 0; }

    /**
     * @return array
     */
    public function getHeaders() { return []; }

    /**
     * @param HttpEntity $entity
     */
    public function setEntity(HttpEntity $entity = null) {  }

    /**
     * @return HttpEntity
     */
    public function getEntity() { return new HttpEntity(); }
}

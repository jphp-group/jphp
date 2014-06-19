<?php
namespace php\net;

use php\io\File;

/**
 * Class HttpFileEntity
 * @package php\net
 */
class HttpFileEntity extends HttpEntity {

    /**
     * @param string|File $file
     * @param null $contentType
     */
    public function __construct($file, $contentType = null) { }
}

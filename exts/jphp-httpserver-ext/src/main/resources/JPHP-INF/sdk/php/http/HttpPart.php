<?php

namespace php\http;

use php\io\Stream;

class HttpPart
{
    /**
     * @return string
     */
    public function readAll(): string {}

    /**
     * @return string
     */
    public function getName(): string {}

    /**
     * @return string
     */
    public function getContentType(): string {}

    /**
     * @return string
     */
    public function getSubmittedFileName(): string {}

    /**
     * @return int
     */
    public function getSize(): int {}
}

<?php
namespace php\http;

use php\io\File;

/**
 * Class HttpDownloadFileHandler
 * @package php\http
 * @packages http
 */
class HttpDownloadFileHandler extends HttpAbstractHandler
{
    /**
     * HttpDownloadFileHandler constructor.
     * @param string|File $file
     * @param string|null $fileName
     * @param string|null $contentType
     */
    public function __construct($file, string $fileName = null, string $contentType = null)
    {
    }

    /**
     * @return File
     */
    public function file(): File
    {
    }

    /**
     * @return string
     */
    public function fileName(): string
    {
    }

    /**
     * @return string
     */
    public function contentType(): string
    {
    }

    /**
     * @param string|File $file
     * @param string|null $fileName
     * @param string|null $contentType
     */
    public function reset($file, string $fileName = null, string $contentType = null)
    {
    }

    /**
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     * @return bool
     */
    public function __invoke(HttpServerRequest $request, HttpServerResponse $response): bool
    {
    }
}
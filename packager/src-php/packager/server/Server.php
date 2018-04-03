<?php
namespace packager\server;

use packager\cli\Console;
use packager\Repository;
use php\format\JsonProcessor;
use php\http\HttpDownloadFileHandler;
use php\http\HttpServer;
use php\http\HttpServerRequest;
use php\http\HttpServerResponse;
use php\io\IOException;
use php\io\Stream;
use php\lib\str;

/**
 * Class Server
 * @package packager\server
 */
class Server
{
    const PORT = 6333;

    /**
     * @var Repository
     */
    private $repository;

    function __construct(Repository $repository)
    {
        $this->repository = $repository;
    }

    function run()
    {
        Stream::putContents("jppm-server.pid", getmypid());

        $server = new HttpServer(self::PORT, '0.0.0.0');
        $server->get('/', [$this, 'handleIndex']);
        $server->post('/repo/upload', [$this, 'handleUpload']);
        $server->get('/repo/{name}/find', [$this, 'handleFind']);
        $server->get('/repo/{name}', [$this, 'handleGet']);

        Console::log("Run jppm server at port: {0}, PID: {1}",  self::PORT, getmypid());
        Console::log(" --> use command 'kill -9 {0}' to stop server.", getmypid());
        $server->run();
    }

    function handleFind(HttpServerRequest $req, HttpServerResponse $res)
    {
        Console::log("Find package {0}, ip: {1}", $req->attribute('name'), $req->remoteAddress());

        $versions = $this->repository->getPackageVersions($req->attribute('name'));
        $res->contentType("application/json");
        $res->body(str::formatAs($versions, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT));
    }

    function handleIndex(HttpServerRequest $req, HttpServerResponse $res)
    {
        $res->contentType("application/json");
        $res->body('{"status": "OK"}');
    }

    function handleGet(HttpServerRequest $req, HttpServerResponse $res)
    {
        try {
            $package = $this->repository->getPackage($req->attribute('name'), $req->query('version'));
        } catch (IOException $e) {
            $package = null;
        }

        $res->contentType("application/json");

        if ($package == null) {
            $res->status(404);
            $res->body('{"status": "NotFound"}');
        } else {
            if ((bool) $req->query('download')) {
                Console::log("Download package {0}@{1}, ip: {2}", $package->getName(), $package->getVersion(), $req->remoteAddress());

                $zipFile = $this->repository->archivePackage($package);

                $handler = new HttpDownloadFileHandler(
                    $zipFile->getPath(), "{$package->getName()}-{$package->getVersion()}.tar.gz", "application/gzip"
                );
                $handler($req, $res);
                return;
            }

            Console::log("Get package {0}@{1}, ip: {2}", $package->getName(), $package->getVersion(), $req->remoteAddress());
            $res->body(str::formatAs($package->toArray(), "json", JsonProcessor::SERIALIZE_PRETTY_PRINT));
        }
    }

    function handleUpload(HttpServerRequest $req, HttpServerResponse $res)
    {

    }
}
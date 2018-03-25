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
        $server = new HttpServer(self::PORT, '0.0.0.0');
        $server->get('/', function (HttpServerRequest $req, HttpServerResponse $res) {
            $res->contentType("application/json");
            $res->body('{"status": "OK"}');
        });

        $server->get('/repo/{name}/find', function (HttpServerRequest $req, HttpServerResponse $res) {
            $versions = $this->repository->getPackageVersions($req->attribute('name'));
            $res->contentType("application/json");
            $res->body(str::formatAs($versions, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT));
        });

        $server->get('/repo/{name}', function (HttpServerRequest $req, HttpServerResponse $res) {
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
                    $zipFile = $this->repository->archivePackage($package);

                    $handler = new HttpDownloadFileHandler(
                        $zipFile->getPath(), "{$package->getName()}-{$package->getVersion()}.zip", "application/zip"
                    );
                    $handler($req, $res);
                    return;
                }

                $res->body(str::formatAs($package->toArray(), "json", JsonProcessor::SERIALIZE_PRETTY_PRINT));
            }
        });

        Console::log("Run jppm server at port: " . self::PORT);
        $server->run();
    }
}
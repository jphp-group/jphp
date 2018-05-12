<?php

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use packager\Event;
use packager\Package;
use php\format\ProcessorException;
use php\format\YamlProcessor;
use php\io\IOException;
use php\lib\fs;
use php\lib\str;

/**
 * Class HubPlugin
 *
 * @jppm-task-prefix hub
 *
 * @jppm-task login
 * @jppm-task publish
 * @jppm-task unpublish
 * @jppm-task user
 * @jppm-task status
 */
class HubPlugin
{
    /**
     * @var string
     */
    private $endpoint;

    /**
     * @var array
     */
    private $config;

    private $defaultLogin;

    private $configDir = "./";

    /**
     * HubPlugin constructor.
     */
    public function __construct(Event $event)
    {
        if ($event->package()) {
            $hub = $event->package()->getAny('hub');

            $this->endpoint = $hub['endpoint'] ?? 'http://api.develnext.org';
            $this->defaultLogin = $hub['login'] ?? null;
            $this->configDir = $event->package()->getAny('hub.config-dir', "./");

            $this->readConfig();
        }
    }


    /**
     * @return array
     */
    protected function readConfig(): array
    {
        try {
            $file = $this->configDir . "package.hub.yml";

            $config = [];
            if (fs::isFile($file)) {
                $config = fs::parse($file);
            }

            $config['login'] = $this->defaultLogin ?? $config['login'];

            return $this->config = $config;
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to read package.hub.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }

    protected function writeConfig()
    {
        try {
            $file = $this->configDir . "package.hub.yml";

            fs::format($file, $this->config, YamlProcessor::SERIALIZE_PRETTY_FLOW);
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to save package.hub.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }


    public function requireAuth()
    {
        if (!$this->config['auth']) {
            Tasks::run('hub:login');
        } else {
            $client = $this->client(false);
            $res = $client->get('/auth/account');

            if (!$res->isSuccess()) {
                $this->config['auth'] = null;
                $this->writeConfig();

                Console::log("-> you need to re-login, access token is invalid.");
                Tasks::run('hub:login');
            }
        }
    }


    /**
     * @param bool $requiredAuth
     * @return HttpClient
     */
    public function client(bool $requiredAuth = true): HttpClient
    {
        if ($requiredAuth) {
            $this->requireAuth();
        }

        $client = new HttpClient($this->endpoint);

        $client->requestType = 'JSON';
        $client->responseType = 'JSON';
        $client->handlers = [
            function (HttpRequest $request) {
                Console::debug("-> {0} {1}", $request->method(), $request->url());
            }
        ];

        $client->headers['X-Token'] = $this->config['auth'];

        return $client;
    }


    private function printRelease(?array $release)
    {
        Console::log("Release Info:");
        Console::log("--------------------------------");

        if ($release) {
            Console::log("     name: {0}", $release['name']);
            Console::log("  version: {0}", $release['version']);
            Console::log("      url: {0}", $release['downloadUrl']);
        } else {
            Console::log("\n   Release not found.\n");
        }

        Console::log("--------------------------------");
    }

    /**
     * @jppm-description Show data of logged user on hub.
     * @param Event $event
     */
    public function user(Event $event)
    {
        $client = $this->client();
        $res = $client->get('/auth/account');

        if ($res->isSuccess()) {
            Console::log("User Info:");
            Console::log("--------------------------------");

            $info = $res->body();

            Console::log("     id: {0}", $info['id']);
            Console::log("  email: {0}", $info['email']);
            Console::log("   name: {0}", $info['login']);
            Console::log("  token: {0}", $this->config['auth']);
            Console::log("--------------------------------");
        } else {
            Console::log("User is not logged.");
        }
    }

    /**
     * @jppm-need-package
     * @jppm-description Login on Hub repository.
     * @param Event $event
     */
    public function login(Event $event)
    {
        if ($this->config['auth']) {
            if (!Console::readYesNo('Do you want to re-login?')) {
                exit();
            }
        }

        $login = str::trim(Console::read('Your Email:', $this->config['login']));
        $password = str::trim(Console::read('Your Password:'));

        $this->config['login'] = $login;

        $client = $this->client(false);

        $res = $client->post('/auth/login', [
            'login' => $login,
            'password' => $password
        ]);

        if ($res->isSuccess()) {
            if (!$res->body()['id']) {
                Console::log("Failed to log in, invalid response body (id not found).");
                exit(-1);
            }

            $this->config['auth'] = $res->body()['id'];

            Console::log("--------------------------------");
            Console::log("     login: {0}", $login);
            Console::log("  password: {0}", '*****');
            Console::log("--------------------------------");

            Console::log("You have successfully logged in, auth data saved to package.hub.yml.");
            Console::log("-> (!) WARNING: Add 'package.hub.yml' to your ignore files.");
            $this->writeConfig();
        } else {
            Console::log("Failed to log in, reason is '{0}'", $res->statusMessage());
            exit(-1);
        }
    }

    /**
     * @jppm-need-package
     * @jppm-description Un-Publish package from remote hub repository.
     * @param Event $event
     */
    public function unpublish(Event $event)
    {
        $this->requireAuth();

        $client = $this->client();
        $pkg = $event->package();

        if (!$client->get('/repo/get', ['name' => $pkg->getName(), 'version' => $pkg->getVersion()])->isSuccess()) {
            Console::log("Package {0} is not published on hub.", $pkg->getNameWithVersion());
            exit();
        }

        $request = new HttpRequest('DELETE', '/repo/unpublish', [], [
            'name' => $pkg->getName(),
            'version' => $pkg->getVersion(),
        ]);

        if ($event->isFlag('y', 'yes')
            || Console::readYesNo("Are you sure to un-publish {$pkg->getNameWithVersion()}?")) {

            $res = $client->send($request);

            if ($res->isSuccess()) {
                $this->printRelease($res->body());
                Console::log("The package '{0}' has been un-published successfully.", $pkg->getNameWithVersion());
            } else {
                Console::error("Failed to un-publish release of current package, {0} {1}.", $res->statusCode(), $res->statusMessage());
                exit(-1);
            }
        } else {
            Console::log("... canceled.");
            exit();
        }
    }

    /**
     * @jppm-need-package
     * @jppm-depends-on build
     * @jppm-description Publish package on remote hub repository.
     * @param Event $event
     */
    public function publish(Event $event)
    {
        $this->requireAuth();
        $client = $this->client();

        $res = $client->get('/repo/get', ['name' => $event->package()->getName(), 'version' => $event->package()->getVersion()]);

        if ($res->isSuccess()) {
            if (!$event->isFlag('f', 'force')) {
                Console::error("Package {0} already has published.", $event->package()->getNameWithVersion());

                Console::log("\n    Use 'hub:unpublish' task to delete this release.");
                Console::log("\n         or\n");
                Console::log("    Use 'hub:publish -f' task to re-publish this release.");
                exit(-1);
            } else {
                Console::warn("-> re-publishing ...");
            }
        }

        Tasks::run('publish', [], 'yes');

        $repo = $event->packager()->getRepo();
        $repo->index($event->package()->getName());

        $pkg = $repo->getPackage($event->package()->getName(), $event->package()->getVersion());
        $archFile = $repo->getPackageArchiveFile($pkg->getName(), $pkg->getVersion());

        Console::print("-> uploading {0} ...", fs::name($archFile));

        $client = $this->client();
        $request = new HttpRequest('POST', '/repo/publish-archive', [], [
            'name' => $pkg->getName(),
            'version' => $pkg->getVersion(),
            'size' => $pkg->getSize(),
            'sha256' => $pkg->getHash(),
            'file' => $archFile,
        ]);
        $request->type('MULTIPART');

        $res = $client->send($request);

        if ($res->isSuccess()) {
            Console::log(" done.");
            $this->printRelease($res->body());
            Console::log("The package '{0}' has been uploaded successfully.", $pkg->getNameWithVersion());
        } else {
            Console::error("Failed to publish release of current package, {0} {1}.", $res->statusCode(), $res->statusMessage());
            exit(-1);
        }
    }


    /**
     * @jppm-description Show status of package on hub!
     * @param Event $event
     */
    public function status(Event $event)
    {
        $pkg = $event->package();

        Console::log("-> Hub of package: {0}", $this->endpoint ?: '*empty*');

        if ($this->config['auth']) {
            Console::log("-> Logged as {0}.", $this->config['login']);
        } else {
            Console::log("-> Not logged.");
        }

        if ($pkg) {
            $client = $this->client(false);
            $res = $client->get('/repo/get', ['name' => $pkg->getName(), 'version' => $pkg->getVersion()]);

            if ($res->isSuccess()) {
                $this->printRelease($res->body());
            } else {
                $this->printRelease(null);
            }
        }
    }
}
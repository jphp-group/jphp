<?php

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use packager\Event;
use php\format\ProcessorException;
use php\format\YamlProcessor;
use php\io\IOException;
use php\io\Stream;
use php\lib\fs;
use php\lib\str;
use php\net\URL;
use text\TextWord;

/**
 * Class GitHubPlugin
 *
 * @jppm-task-prefix github
 *
 * @jppm-task login
 * @jppm-task user
 * @jppm-task publish
 * @jppm-task unpublish
 * @jppm-task status
 */
class GitHubPlugin
{
    /**
     * @var array
     */
    protected $config;

    /**
     * GitHubPlugin constructor.
     * @param Event $event
     */
    public function __construct(Event $event)
    {
        $this->config = $this->readConfig();
        $event->packager()->getIgnore()->addRule('/package.github.yml');
    }

    /**
     * @param bool $requiredAuth
     * @return HttpClient
     */
    public function github(bool $requiredAuth = true): HttpClient
    {
        $client = new HttpClient("https://api.github.com");
        $client->headers['Accept'] = 'application/vnd.github.v3+json';
        $client->requestType = 'JSON';
        $client->responseType = 'JSON';
        $client->handlers = [
            function (HttpRequest $request) {
                //Console::log("-> {0} {1}", $request->method(), $request->url());
            }
        ];

        if ($requiredAuth) {
            $client->headers['Authorization'] = $this->config['auth'];

            if (!$client->headers['Authorization']) {
                Tasks::run("github:login");
            }
        }

        return $client;
    }

    /**
     * @param $id
     */
    private function deleteRelease($id)
    {
        $address = new URL($this->config['address']);

        $github = $this->github(true);
        $response = $github->delete("/repos{$address->getPath()}/releases/$id");

        if (!$response->isSuccess()) {
            Console::log("Failed to delete release with id = {0}, {1}", $id, $response->statusMessage());
            exit(-1);
        }
    }

    /**
     * @param string $tag
     * @return array|null
     */
    private function getRelease(string $tag): ?array
    {
        $address = new URL($this->config['address']);

        $github = $this->github(true);
        $response = $github->get("/repos{$address->getPath()}/releases/tags/$tag");

        if ($response->isSuccess()) {
            return $response->body();
        } elseif ($response->isNotFound()) {
            return null;
        } else {
            Console::error("Failed to get release from {0}, {1}", $this->config['address'], $response->statusMessage());
            exit(-1);
        }
    }

    private function printRelease(?array $release)
    {
        Console::log("Release Info:");
        Console::log("--------------------------------");

        if ($release) {
            Console::log("      tag: {0}", $release['tag_name']);
            Console::log("       id: {0}", $release['id']);
            Console::log("     name: {0}", $release['name']);
            Console::log("published: {0}", $release['published_at']);
            Console::log("   author: {0} ({1})", $release['author']['login'], $release['author']['url']);
            Console::log("      url: {0}", $release['html_url']);
        } else {
            Console::log("\n   Release not found.\n");
        }

        Console::log("--------------------------------");
    }

    /**
     * @return array
     */
    protected function readConfig(): array
    {
        try {
            $file = "./package.github.yml";

            if (!fs::isFile($file)) {
                return [];
            }

            return fs::parse($file);
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to read package.gitlab.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }

    protected function writeConfig()
    {
        try {
            $file = "./package.github.yml";

            fs::format($file, $this->config, YamlProcessor::SERIALIZE_PRETTY_FLOW);
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to save package.gitlab.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }

    /**
     * @jppm-description show status of package on github!
     * @param Event $event
     */
    public function status(Event $event)
    {
        $pkg = $event->package();

        Console::log("-> Repository of package: {0}", $this->config['address'] ?: '*empty*');

        if ($this->config['auth'] && $this->config['login']) {
            Console::log("-> Logged as {0}.", $this->config['login']);
        } else {
            Console::log("-> Not logged.");
        }

        if ($pkg) {
            $release = $this->getRelease($pkg->getVersion());
            $this->printRelease($release);
        }
    }

    /**
     * @jppm-description create and upload release of current package.
     * @param Event $event
     */
    public function publish(Event $event)
    {
        global $app;

        $pkg = $event->package();
        if ($pkg) {
            $release = $this->getRelease($pkg->getVersion());

            if ($release) {
                $this->printRelease($release);

                if ($app->isFlag('f', 'force')) {
                    Tasks::run('github:unpublish', ['yes']);
                } else {
                    Console::error("Package {0} already has released on {1}.", $pkg->getNameWithVersion(), $this->config['address']);

                    Console::log("\n    Use 'github:unpublish' task for delete this release.");
                    exit(-1);
                }
            }

            Tasks::run('pack');

            $github = $this->github();

            $address = new URL($this->config['address']);


            $response = $github->post('/repos' . $address->getPath() . '/releases', [
                'tag_name' => $pkg->getVersion(),
                'name' => Console::read("Enter title of release:", (new TextWord($pkg->getName() . ' ' . $pkg->getVersion()))->capitalizeFully() . ""),
                'body' => Console::read("Enter description of release:", $pkg->getDescription()),
                'draft' => $draft = Console::readYesNo('Publish as draft release?'),
                'prerelease' => $prerelease = Console::readYesNo('Publish as pre-release?')
            ]);

            if ($response->statusCode() === 201) {
                $body = $response->body();

                $uploadUrl = str::replace($body['upload_url'], '{?name,label}', '');

                $request = new HttpRequest(
                    'POST', $uploadUrl . "?name={$pkg->getName()}-{$pkg->getVersion()}.tar.gz"
                );

                $request->absoluteUrl(true);
                $request->type('STREAM');
                $request->responseType('JSON');
                $request->contentType('application/gzip');
                $request->body(Stream::of("./{$pkg->getName()}-{$pkg->getVersion()}.tar.gz"));

                Console::log("Release of current package has successfuly created.");

                Console::log("-> uploading archive tar.gz ...");
                $response = $github->send($request);

                if ($response->isSuccess()) {
                    Console::log("Release archive file has successfuly uploaded.");
                } else {
                    Console::error("Failed to upload release file of current package, {0}.", $response->statusMessage());
                }
            } else {
                Console::error("Failed to create release of current package, {0}.", $response->statusMessage());
            }
        }
    }

    /**
     * @param Event $event
     */
    public function unpublish(Event $event)
    {
        global $app;
        $pkg = $event->package();
        if ($pkg) {
            $release = $this->getRelease($pkg->getVersion());

            if (!$release) {
                Console::log("Package {0} is not published on {1}", $pkg->getNameWithVersion(), $this->config['address']);
            } else {
                if ($app->isFlag('y', 'yes')
                    || Console::readYesNo("Are you sure to un-publish release {$pkg->getNameWithVersion()}?")) {
                    $this->deleteRelease($release['id']);
                    Console::log("Package {0} has successfully un-published.", $pkg->getNameWithVersion());
                } else {
                    Console::log("... canceled.");
                    exit();
                }
            }
        }
    }

    /**
     * @jppm-description show data of logged user on github.
     */
    public function user()
    {
        $github = $this->github();
        $response = $github->get('/user');

        if ($response->isSuccess()) {
            Console::log("User Info:");
            Console::log("--------------------------------");

            $info = $response->body();

            Console::log("       id: {0}", $info['id']);
            Console::log("    login: {0}", $info['login']);
            Console::log("     name: {0}", $info['name']);
            Console::log("    email: {0}", $info['email']);
            Console::log(" location: {0}", $info['location'] ?: '?');
            Console::log("followers: {0}", $info['followers'] ?: 0);
            Console::log("     repo: {0}", $this->config['address']);
            Console::log("--------------------------------");
        } else {
            Console::log("User is not logged.");
        }
    }

    /**
     * @jppm-description set github repo address and log in.
     * @param Event $event
     */
    public function login(Event $event)
    {
        $config = $this->config;

        $address = Console::read('Repo address:', $config['address']);

        $address = str::trim($address);
        if (!str::startsWith($address, 'https://github.com/') && !str::startsWith($address, 'http://github.com/')) {
            Console::error("Address {0} is invalid, enter full http link of your gitlab repository", $address);
            exit(-1);
        }

        $client = new HttpClient();
        $response = $client->get($address);

        if (!$response->isSuccess()) {
            Console::error("Your github repository is not available, reason is '{0}'", $response->statusMessage());
            exit(-1);
        }

        $this->config['address'] = $address;

        if ($config['auth']) {
            if (!Console::readYesNo('Do you want to re-login to github.com?')) {
                Console::log("Address of repo has changed, all data saved to package.github.yml.");
                self::writeConfig();
                exit();
            }
        }

        $login = str::trim(Console::read('GitHub Login:', $config['login']));
        $password = str::trim(Console::read('GitHub Password:'));

        $this->config['login'] = $login;
        $this->config['auth'] = 'Basic ' . base64_encode("$login:$password");

        $github = self::github(false);
        $github->headers['Authorization'] = $this->config['auth'];
        $response = $github->get('/user');

        if ($response->isSuccess()) {
            Console::log("--------------------------------");
            Console::log("   address: {0}", $address);
            Console::log("     login: {0}", $login);
            Console::log("  password: {0}", '*****');
            Console::log("--------------------------------");

            Console::log("You have successfully logged in, auth data saved to package.github.yml.");
            self::writeConfig();
        } else {
            Console::log("Failed to log in, reason is '{0}'", $response->statusMessage());
            exit(-1);
        }
    }
}
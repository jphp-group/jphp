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
use php\net\URLConnection;
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
     * @var string
     */
    protected $tagPrefix = '';

    /**
     * @var string
     */
    protected $defaultTitle;

    /**
     * @var string
     */
    protected $defaultDescription;

    /**
     * @var array
     */
    protected $additionalAssets = [];

    /**
     * @var string
     */
    protected $defaultRepo;

    /**
     * @var string
     */
    protected $defaultLogin;
    protected $configDir = "./";

    /**
     * GitHubPlugin constructor.
     * @param Event $event
     */
    public function __construct(Event $event)
    {
        if ($event->package()) {
            $github = $event->package()->getAny('github');

            $this->tagPrefix = $github['tag-prefix'] ?? '';
            $this->defaultTitle = $github['title'] ?? null;
            $this->defaultDescription = $github['description'] ?? null;
            $this->defaultRepo = $github['address'] ?? null;
            $this->defaultLogin = $github['login'] ?? null;
            $this->additionalAssets = $github['assets'] ?? [];

            $this->configDir = $github['config-dir'] ?? './';
        }

        $this->readConfig();
    }

    public function requireAuth()
    {
        if (!$this->config['auth']) {
            Tasks::run('github:login');
        }
    }

    /**
     * @param bool $requiredAuth
     * @return HttpClient
     */
    public function github(bool $requiredAuth = true): HttpClient
    {
        if ($requiredAuth) {
            $this->requireAuth();
        }

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
        }

        return $client;
    }

    /**
     * @param string $uploadUrl
     * @param string $filename
     * @param string $contentType
     */
    private function uploadFileForRelease(string $uploadUrl, string $filename, string $contentType = '')
    {
        if (!fs::isFile($filename)) {
            Console::error("File '{0}' doesn't exist", $filename);
            exit(-1);
        }

        $github = $this->github();

        $uploadUrl = str::replace($uploadUrl, '{?name,label}', '');

        $name = fs::name($filename);
        $encName = URL::encode($name);

        $request = new HttpRequest(
            'POST', $uploadUrl . "?name={$encName}"
        );

        $contentType = $contentType ?: URLConnection::guessContentTypeFromName($filename);

        $request->absoluteUrl(true);
        $request->type('STREAM');
        $request->responseType('JSON');
        $request->contentType($contentType);
        $request->body(Stream::of($filename));

        Console::print("-> uploading {0} ({1}) ...", $encName, $contentType);

        $response = $github->send($request);

        if ($response->isSuccess()) {
            Console::log(" done.");
        } else {
            Console::log(" error.");
            Console::error("Failed to upload release file of current package, {0}.", $response->statusMessage());
            exit(-1);
        }
    }

    /**
     * @param $id
     */
    private function deleteRelease($id)
    {
        $this->requireAuth();

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
        if (!$this->config['address']) return null;

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
            Console::log("   author: {0} ({1})", $release['author']['login'], $release['author']['html_url']);
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
            $file = $this->configDir . "package.github.yml";

            $config = [];
            if (fs::isFile($file)) {
                $config = fs::parse($file);
            }

            $config['address'] = $this->defaultRepo ?? $config['address'];
            $config['login'] = $this->defaultLogin ?? $config['login'];

            return $this->config = $config;
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to read package.gitlab.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }

    protected function writeConfig()
    {
        try {
            $file =  $this->configDir . "package.github.yml";

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
            $release = $this->getRelease($this->tagPrefix . $pkg->getVersion());
            $this->printRelease($release);
        }
    }

    /**
     * @jppm-description create and upload release of current package.
     * @param Event $event
     */
    public function publish(Event $event)
    {
        $this->requireAuth();

        $pkg = $event->package();

        if ($pkg) {
            $release = $this->getRelease($this->tagPrefix . $pkg->getVersion());

            if ($release) {
                $this->printRelease($release);

                if ($event->isFlag('f', 'force')) {
                    Tasks::run('github:unpublish', [], 'yes');
                } else {
                    Console::error("Package {0} already has released on {1}.", $pkg->getNameWithVersion(), $this->config['address']);

                    Console::log("\n    Use 'github:unpublish' task to delete this release.");
                    Console::log("\n         or\n");
                    Console::log("    Use 'github:publish -f' task to re-publish this release.");
                    exit(-1);
                }
            }

            Tasks::run('pack');

            $github = $this->github();
            $address = new URL($this->config['address']);

            $defaultName = (new TextWord(str::replace($pkg->getName(), '-', ' ') . ' ' . $pkg->getVersion()))->capitalizeFully() . "";
            $defaultDescription = $pkg->getDescription();

            $bodyJsonInfo = "<details><summary>{$pkg->getName()}-{$pkg->getVersion()}.json</summary><pre>"
                . fs::get("./{$pkg->getName()}-{$pkg->getVersion()}.json") .
            "</pre></details>";
            
            $data = [
                'tag_name' => $this->tagPrefix . $pkg->getVersion(),
                'name' => $this->defaultTitle ?? $defaultName,
                'body' => $this->defaultDescription ?? $defaultDescription
            ];

            if (!$event->isFlag('y', 'yes')) {
                $data = flow($data, [
                    'name' => $this->defaultTitle ?? Console::read("Enter title of release:", $defaultName),
                    'body' => $this->defaultDescription ?? Console::read("Enter description of release:", $defaultDescription),
                    'draft' => $draft = Console::readYesNo('Publish as draft release?'),
                    'prerelease' => $prerelease = Console::readYesNo('Publish as pre-release?')
                ])->toMap();
            }

            $data['body'] .= "\n$bodyJsonInfo";
            $response = $github->post('/repos' . $address->getPath() . '/releases', $data);

            if ($response->statusCode() === 201) {
                Console::log("Release of current package has successfully created.");

                $body = $response->body();

                $this->uploadFileForRelease($body['upload_url'], "./{$pkg->getName()}-{$pkg->getVersion()}.tar.gz", 'application/gzip');
                $this->uploadFileForRelease($body['upload_url'], "./{$pkg->getName()}-{$pkg->getVersion()}.json", 'application/json');

                foreach ($this->additionalAssets as $asset) {
                    if (!fs::isFile($asset['file'])) {
                        if ($asset['ifExists']) {
                            Console::log("-> skip uploading, file '{0}' doesn't exist", $asset['file']);
                            continue;
                        }
                    }

                    $this->uploadFileForRelease($body['upload_url'], $asset['file'], $asset['contentType']);
                }

                $this->printRelease($this->getRelease($data['tag_name']));
            } else {
                Console::error("Failed to create release of current package, {0} {1}.", $response->statusCode(), $response->statusMessage());
            }
        }
    }

    /**
     * @param Event $event
     */
    public function unpublish(Event $event)
    {
        $this->requireAuth();

        $pkg = $event->package();

        if ($pkg) {
            $release = $this->getRelease($this->tagPrefix . $pkg->getVersion());

            if (!$release) {
                Console::log("Package {0} is not published on {1}", $pkg->getNameWithVersion(), $this->config['address']);
            } else {
                if ($event->isFlag('y', 'yes')
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

        if (!$address) {
            $address = $config['address'];
        }

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
<?php

use httpclient\HttpClient;
use httpclient\HttpRequest;
use packager\cli\Console;
use packager\Event;
use php\format\ProcessorException;
use php\io\IOException;
use php\io\Stream;
use php\lib\fs;
use php\lib\str;
use php\net\URL;

/**
 * Class GitHubPlugin
 *
 * @jppm-task-prefix github
 *
 * @jppm-task login
 * @jppm-task user
 * @jppm-task publish
 */
class GitHubPlugin
{
    /**
     * @param bool $requiredAuth
     * @return HttpClient
     */
    public static function github(bool $requiredAuth = true): HttpClient
    {
        $client = new HttpClient("https://api.github.com");
        $client->headers['Accept'] = 'application/vnd.github.v3+json';
        $client->requestType = 'JSON';
        $client->responseType = 'JSON';

        if ($requiredAuth) {
            $client->headers['Authorization'] = self::readConfig()['auth'];

            if (!$client->headers['Authorization']) {
                Tasks::run("github:login");
            }
        }

        return $client;
    }

    /**
     * @return array
     */
    public static function readConfig(): array
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

    public static function writeConfig(array $data)
    {
        try {
            $file = "./package.github.yml";

            fs::format($file, $data);
        } catch (ProcessorException|IOException $e) {
            Console::error("Failed to save package.gitlab.yaml, reason = '{0}'", $e->getMessage());
            exit(-1);
        }
    }

    /**
     * @jppm-description create and upload release of current package.
     * @param Event $event
     */
    public static function publish(Event $event)
    {
        Tasks::run('pack');

        $pkg = $event->package();
        if ($pkg) {
            $github = static::github();

            $address = new URL(static::readConfig()['address']);

            $response = $github->post('/repos' . $address->getPath() . '/releases', [
                'tag_name' => $event->package()->getVersion(),
                'name' => $event->package()->getName(),
            ]);

            if ($response->statusCode() === 201) {
                $body = $response->body();

                $uploadUrl = str::replace($body['upload_url'], '{?name,label}', '');

                $request = new HttpRequest(
                    'POST', $uploadUrl . "?name={$pkg->getName()}-{$pkg->getVersion()}.tar.gz"
                );

                $request->absoluteUrl(true);
                $request->type('RAW');
                $request->responseType('JSON');
                $request->contentType('application/gzip');
                $request->data(Stream::of("./{$pkg->getName()}-{$pkg->getVersion()}.tar.gz"));

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

        exit();
    }

    /**
     * @jppm-description show data of logged user on github.
     */
    public static function user()
    {
        $github = static::github();
        $response = $github->get('/user');

        Console::log("User Info:");
        Console::log("--------------------------------");

        $info = $response->body();
        Console::log("       id: {0}", $info['id']);
        Console::log("    login: {0}", $info['login']);
        Console::log("     name: {0}", $info['name']);
        Console::log("    email: {0}", $info['email']);
        Console::log(" location: {0}", $info['location'] ?: '?');
        Console::log("followers: {0}", $info['followers'] ?: 0);
        Console::log("--------------------------------");

        exit();
    }

    /**
     * @jppm-description set github repo address and log in.
     * @param Event $event
     */
    public static function login(Event $event)
    {
        $config = static::readConfig();

        $address = Console::read('Repo address:', $config['address']);

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

        $login = Console::read('GitHub Login:', $config['login']);
        $password = Console::read('GitHub Password:');

        $config = [
            'address' => $address, 'login' => $login, 'auth' => 'Basic ' . base64_encode("$login:$password")
        ];

        $github = self::github(false);
        $github->headers['Authorization'] = $config['auth'];
        $response = $github->get('/user');

        if ($response->isSuccess()) {
            Console::log("--------------------------------");
            Console::log("   address: {0}", $address);
            Console::log("     login: {0}", $login);
            Console::log("  password: {0}", '*****');
            Console::log("--------------------------------");

            Console::log("You have successfully logged in, auth data saved to package.github.yml.");
            self::writeConfig($config);
            exit();
        } else {
            Console::log("Failed to log in, reason is '{0}'", $response->statusMessage());
            exit(-1);
        }
    }
}
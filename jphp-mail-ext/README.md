## Email Extension

A simple extension for sending mail via smtp servers.

#### Examples

Sending email via Gmail account.

```php
use php\mail\Email;
use php\mail\EmailBackend;

// Create email server backend configuration.
$backend = new EmailBackend();

// set username and password of gmail account
$backend->setAuthentication('yourname@gmail.com', 'password');

// host name of gmail smtp server
$backend->hostName     = 'smtp.googlemail.com';

$backend->smtpPort     = 465;
$backend->sslOnConnect = true;

$email = new Email();
$email->setSubject('Hello!');
$email->setFrom('noreply@jphp.ru', 'JPHP Compiler');
$email->setTo(['dz@dim-s.net', 'd.zayceff@gmail.com']);

// Set html text
$email->setHtmlMessage('<b>Foobar</b>');

// Set simple text
$email->setTextMessage('Foobar');

// Add attachment using file, also you can use php\io\Stream objects.
$email->attach('path/to/file', 'image/jpeg', 'My Picture');

$email->send($backend);
```
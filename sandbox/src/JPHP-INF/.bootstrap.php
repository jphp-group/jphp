<?php

use php\mail\Email;
use php\mail\EmailBackend;

$backend = new EmailBackend();
$backend->setAuthentication('bot@dim-s.net', 'myfghjfghj1');

$backend->hostName     = 'smtp.gmail.com';
$backend->sslSmtpPort  = 465;
$backend->sslOnConnect = true;

$email = new Email();
$email->setTo(['dz@dim-s.net', 'd.zayceff@gmail.com']);
$email->send($backend);
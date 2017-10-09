<?php
namespace org\develnext\sandbox;

use php\android\Android;
use php\android\app\BootstrapActivity;

class AppActivity extends BootstrapActivity
{
    public function onCreate()
    {
        parent::onCreate();

        Android::startActivity(MainActivity::class);
    }
}
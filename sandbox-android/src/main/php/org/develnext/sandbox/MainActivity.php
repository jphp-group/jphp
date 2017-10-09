<?php
namespace org\develnext\sandbox;

use php\android\app\Activity;
use php\android\widget\LinearLayout;
use php\android\widget\TextView;
use php\android\widget\Toast;

class MainActivity extends Activity
{
    public function onCreate()
    {
        parent::onCreate();

        $text = new TextView($this);
        $text->setText('Hello World');
        $pane = new LinearLayout($this);

        $text->on('click', function () {
            Toast::makeText('Hi!!!')->show();
        });

        $pane->addView($text);

        $this->setContentView($pane);
    }
}
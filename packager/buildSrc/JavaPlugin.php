<?php

use java\JavaCompiler;
use packager\Event;
use packager\JavaExec;
use packager\Vendor;


/**
 * Class JavaPlugin
 *
 * @task-prefix java
 *
 * @task build
 */
class JavaPlugin
{
    /**
     * @var array
     */
    private $config = [];

    /**
     * AppPlugin constructor.
     */
    public function __construct(Event $event)
    {
        $event->packager()->getIgnore()->addRule('/build/**');

        $this->config = $event->package()->getAny('java', []);
    }

    /**
     * @jppm-description build java sources to jar file into jars/ directory.
     * @param Event $event
     */
    public function build(Event $event)
    {
        $vendor = new Vendor("./vendor");

        $javac = new JavaCompiler();
        $javac->addVendorClassPath($vendor);
        $javac->addPackageClassPath($event->package());

        if (isset($this->config['encoding'])) {
            $javac->setEncoding($this->config['encoding']);
        }

        if (isset($this->config['target'])) {
            $javac->setTarget($this->config['target']);
        }

        if (isset($this->config['source'])) {
            $javac->setTarget($this->config['source']);
        }

        foreach ((array) $this->config['sources'] as $source) {
            $javac->addSourcePathWithFiles($source);
        }

        foreach ((array) $this->config['resources'] as $source) {
            $javac->addClassPath($source);
        }

        $process = $javac->compile("./","./build/java-classes");

        $process = $process->inheritIO()->startAndWait();
    }
}
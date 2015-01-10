<?php
namespace php\swing;

class UIForm extends UIWindow {
    const NOTHING_ON_CLOSE  = 0;
    const HIDE_ON_CLOSE     = 1;
    const DISPOSE_ON_CLOSE  = 2;
    const EXIT_ON_CLOSE     = 3;

    /** @var bool */
    public $maximized;

    /**
     * @param int $action
     */
    public function setDefaultCloseOperation($action){ }
}

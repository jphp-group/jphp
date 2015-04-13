<?php
namespace php\android\app;
use php\android\view\View;

/**
 * Class Activity
 */
class Activity {
    /**  */
    public function __construct() { }

    /**
     * @param View $view
     */
    public function setContentView(View $view) { }

    /**
     * @return View
     */
    public function getContentView() { }

    /**
     * @param string $title
     */
    public function setTitle($title) { }

    /**
     * @param int $id
     * @return View
     */
    public function findViewById($id) { }

    /**
     * @return bool
     */
    public function isChild() { }

    /**
     * @return Activity
     */
    public function getParent() { }

    public function onCreate() {
    }
}
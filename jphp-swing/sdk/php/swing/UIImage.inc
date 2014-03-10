<?php
namespace php\swing;

/**
 * Class UIImage
 * @package php\swing
 */
class UIImage extends UIContainer {
    /**
     * @var bool
     */
    public $stretch;

    /**
     * @var bool
     */
    public $centered;

    /**
     * @var bool
     */
    public $proportional;

    /**
     * @var bool
     */
    public $smooth;

    /**
     * @var bool
     */
    public $mosaic;

    /**
     * @param Image $image
     */
    public function setImage(Image $image) { }
}
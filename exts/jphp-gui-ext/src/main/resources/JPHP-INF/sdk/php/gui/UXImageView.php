<?php
namespace php\gui;

/**
 * Class UXImageView
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXImageView extends UXNode
{


    /**
     * @var UXImage
     */
    public $image;

    /**
     * @var bool
     */
    public $smooth;

    /**
     * @var bool
     */
    public $preserveRatio;

    /**
     * @var double
     */
    public $fitWidth;

    /**
     * @var double
     */
    public $fitHeight;

    /**
     * UXImageView constructor.
     * @param UXImage|string|null $image
     */
    public function __construct($image = null)
    {
    }
}
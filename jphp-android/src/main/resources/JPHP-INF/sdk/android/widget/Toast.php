<?php
namespace android\widget;

/**
 * Class Toast
 * @package android\widget
 */
class Toast {
    const LENGTH_SHORT = 0;
    const LENGTH_LONG  = 0;

    /**
     * Shows toast.
     */
    public function show() { }

    /**
     * Cancel showing toast.
     */
    public function cancel() { }

    /**
     * @param string $text
     */
    public function setText($text) { }

    /**
     * @param int $duration
     */
    public function setDuration($duration) { }

    /**
     * @return int
     */
    public function getDuration() { }

    /**
     * Show text
     * @param string $text
     * @param int $duration
     * @return Toast
     */
    public static function makeText($text, $duration = self::LENGTH_SHORT) { }
}
<?php
namespace php\gdx;
use php\gdx\graphics\Pixmap;

/**
 * Class Input
 * @package php\gdx
 */
final class Input {

    private function __construct() { }

    /**
     * @return double The value of the accelerometer on its x-axis. ranges between [-10,10].
     */
    public function getAccelerometerX() { }

    /**
     * @return double The value of the accelerometer on its y-axis. ranges between [-10,10].
     */
    public function getAccelerometerY() { }

    /**
     * @return double The value of the accelerometer on its y-axis. ranges between [-10,10].
     */
    public function getAccelerometerZ() { }

    /**
     * Returns the x coordinate of the last touch on touch screen devices and the current mouse position on desktop for the first
     *         pointer in screen coordinates. The screen origin is the top left corner.
     *
     * @param int $pointer (optional) the pointer id. Returns the x coordinate in screen coordinates of the given pointer.
     * @return int
     */
    public function getX($pointer) { }

    /**
     * @param int $pointer (optional) the pointer id.
     * @return int the different between the current pointer location and the last pointer location on the x-axis.
     */
    public function getDeltaX($pointer) { }

    /**
     * Returns the y coordinate of the last touch on touch screen devices and the current mouse position on desktop for the first
     *         pointer in screen coordinates. The screen origin is the top left corner.
     *
     * @param int $pointer (optional) the pointer id. Returns the y coordinate in screen coordinates of the given pointer.
     * @return int
     */
    public function getY($pointer) { }

    /**
     * @param int $pointer (optional) the pointer id.
     * @return int the different between the current pointer location and the last pointer location on the y-axis.
     */
    public function getDeltaY($pointer) { }

    /**
     * @param int $pointer (optional)
     * @return bool whether the screen is currently touched.
     */
    public function isTouched($pointer) { }

    /**
     * @return bool whether a new touch down event just occurred.
     */
    public function justTouched() { }

    /**
     * Whether a given button is pressed or not. Button constants can be found in {@link Buttons}. On Android only the Button#LEFT
     * constant is meaningful.
     *
     * @param int $button
     * @return bool
     */
    public function isButtonPressed($button) { }

    /**
     * Returns whether the key is pressed.
     * @param int $key
     */
    public function isKeyPressed($key) { }

    /**
     * Sets the on-screen keyboard visible if available.
     * @param bool $visible
     */
    public function setOnscreenKeyboardVisible($visible) { }

    /**
     * Vibrates for the given amount of time. Note that you'll need the permission
     * <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for this to work.
     *
     * @param int $millis the number of milliseconds to vibrate.
     */
    public function vibrate($millis) { }

    /**
     * Stops the vibrator
     */
    public function cancelVibrate() { }

    /**
     * The azimuth is the angle of the device's orientation around the z-axis. The positive z-axis points towards the earths
     * center.
     *
     * @return double
     */
    public function getAzimuth() { }

    /**
     * The pitch is the angle of the device's orientation around the x-axis. The positive x-axis roughly points to the west and is
     * orthogonal to the z- and y-axis.
     *
     * @return double
     */
    public function getPitch() { }

    /**
     * The roll is the angle of the device's orientation around the y-axis. The positive y-axis points to the magnetic north pole
     * of the earth.
     *
     * @return double
     */
    public function getRoll() { }

    /**
     * @return int the time of the event currently reported to the InputProcessor.
     */
    public function getCurrentEventTime() { }

    /**
     * Sets whether the BACK button on Android should be caught. This will prevent the app from being paused. Will have no effect
     * on the desktop.
     *
     * @param bool $catchBack
     */
    public function setCatchBackKey($catchBack) { }

    /**
     * Sets whether the MENU button on Android should be caught. This will prevent the onscreen keyboard to show up. Will have no
     * effect on the desktop.
     *
     * @param bool $catchMenu
     */
    public function setCatchMenuKey($catchMenu) { }

    /**
     * @return int the rotation of the device with respect to its native orientation.
     */
    public function getRotation() { }

    /**
     * @return string the native orientation of the device.
     */
    public function getNativeOrientation() { }

    /**
     * Only viable on the desktop. Will confine the mouse cursor location to the window and hide the mouse cursor.
     *
     * @param bool $catched whether to catch or not to catch the mouse cursor
     */
    public function setCursorCatched($catched) { }

    /**
     * @return bool whether the mouse cursor is catched.
     */
    public function isCursorCatched() { }

    /**
     * Only viable on the desktop. Will set the mouse cursor location to the given window coordinates (origin top-left corner).
     *
     * @param int $x
     * @param int $y
     */
    public function setCursorPosition($x, $y) { }

    /**
     * Only viable on the desktop. Will set the mouse cursor image to the image represented by the
     * Pixmap. The Pixmap must be in RGBA8888 format, width & height must be powers-of-two
     * greater than zero (not necessarily equal), and alpha transparency must be single-bit (i.e., 0x00 or 0xFF only). To revert to
     * the default operating system cursor, pass in a null Pixmap; xHotspot & yHotspot are ignored in this case.
     *
     * @param Pixmap $pixmap
     * @param int $xHotspot
     * @param int $yHotspot
     */
    public function setCursorImage(Pixmap $pixmap, $xHotspot, $yHotspot) { }
}
<?php
namespace game;

use action\Geometry;
use php\gui\UXNode;

/**
 * Class Jumping
 * --RU--
 * Утилитный класс для перемещения игровых объектов.
 *
 * @package game
 */
class Jumping
{
    const DATA_SOLID_PROPERTY = '--jumping-solid';

    /**
     * Прилипить объект к сетке.
     *
     * @param UXNode $node
     * @param $gridX
     * @param $gridY
     */
    static function toGrid(UXNode $node, $gridX, $gridY)
    {
        $gridX = $gridX < 1 ? 1 : $gridX;
        $gridY = $gridY < 1 ? 1 : $gridY;

        $x = $node->x;
        $y = $node->y;

        $x = round($x / $gridX) * $gridX;
        $y = round($y / $gridY) * $gridY;

        $node->x = $x;
        $node->y = $y;
    }

    /**
     * Переместить объект в случайную позицию (можно указать сетку).
     *
     * @param UXNode $node
     * @param int $gridX
     * @param int $gridY
     * @param int $tryIndex
     */
    static function toRand(UXNode $node, $gridX = 1, $gridY = 1, $tryIndex = 0)
    {
        $parent = $node->parent;

        if ($parent) {
            $x = rand(0, $parent->width);
            $y = rand(0, $parent->height);

            $node->x = $x;
            $node->y = $y;

            if ($gridX > 1 || $gridY > 1) {
                Jumping::toGrid($node, $gridX, $gridY);
            }
        }
    }

    /**
     * Переместить объект к начальной позиции.
     *
     * @param UXNode $node
     */
    static function toStart(UXNode $node)
    {
        if ($position = $node->data('--start-position')) {
            $node->position = $position;
        }
    }

    /**
     * Переместить объект к позиции x, y (можно относительно).
     *
     * @param UXNode $node
     * @param $x
     * @param $y
     * @param bool $relative
     */
    static function to(UXNode $node, $x, $y, $relative = false)
    {
        if ($relative) {
            $node->x += $x;
            $node->y += $y;
        } else {
            $node->x = $x;
            $node->y = $y;
        }
    }
}
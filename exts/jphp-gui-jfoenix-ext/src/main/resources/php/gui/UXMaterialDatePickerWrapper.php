<?php
namespace php\gui;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialDatePickerWrapper extends UXNodeWrapper
{
    public function applyData(UXData $data)
    {
        parent::applyData($data);

        /** @var UXDatePicker $node */
        $node = $this->node;

        $node->format = $data->get('format');
        $node->value = $data->get('value');
    }
}
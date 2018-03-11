<?php
namespace php\gui;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialTimePickerWrapper extends UXNodeWrapper
{
    public function applyData(UXData $data)
    {
        parent::applyData($data);

        /** @var UXMaterialTimePicker $node */
        $node = $this->node;

        $node->format = $data->get('format');
        $node->value = $data->get('value');
        $node->hourView24 = $data->get('hourView24');
    }
}
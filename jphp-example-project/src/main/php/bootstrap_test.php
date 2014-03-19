<?php
namespace {

    use php\io\Stream;
    use php\lang\System;
    use php\swing\Image;
    use php\swing\SwingUtilities;
    use php\swing\UIDialog;
    use php\swing\UIForm;
    use php\swing\UIManager;
    use php\swing\UIReader;

    UIManager::setLookAndFeel(UIManager::getSystemLookAndFeel());

    SwingUtilities::invokeLater(function(){
        $reader = new UIReader();
        /** @var UIForm $form */
        $form = $reader->read(Stream::create('res://Form.xml'));
        $form->moveToCenter();
        $form->visible = true;
        $form->on('windowClosing', function(){
            System::halt(0);
        });


    });
}

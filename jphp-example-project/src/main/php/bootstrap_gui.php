<?php
namespace {

    use php\lang\System;
    use php\swing\SwingUtilities;
    use php\swing\UIForm;
    use php\swing\UIManager;
    use php\swing\UIProgress;

    UIManager::setLookAndFeel(UIManager::getSystemLookAndFeel());

    SwingUtilities::invokeLater(function(){
        $form = new UIForm();
        $form->size = [500, 500];
        $form->moveToCenter();
        $form->visible = true;

        $p = new UIProgress();
        $p->size = [300, 40];
        $p->position = [100, 100];
        $p->value = 50;
        $form->add($p);

        $form->on('windowClosing', function(){
            System::halt(0);
        });
    });
}

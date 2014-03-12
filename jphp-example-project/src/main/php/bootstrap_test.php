<?php
namespace {

    use php\lang\System;
    use php\lang\Thread;
    use php\swing\Border;
    use php\swing\Image;
    use php\swing\SwingUtilities;
    use php\swing\tree\TreeNode;
    use php\swing\UIForm;
    use php\swing\UILabel;
    use php\swing\UIManager;
    use php\swing\UIPanel;
    use php\swing\UIProgress;
    use php\swing\UITree;

    UIManager::setLookAndFeel(UIManager::getSystemLookAndFeel());

    SwingUtilities::invokeLater(function(){
        $form = new UIForm();
        $form->size = [500, 500];
        $form->moveToCenter();
        $form->visible = true;

        $p = new UITree();
        $p->size = [300, 300];
        $p->position = [100, 100];
        $p->rootVisible = false;

        $p->model->root->add(new TreeNode("One"));
        $p->model->root->add(new TreeNode("Two5765765"));
        $p->rowHeight = 20;

        $p->expandNode($p->model->root);
        $p->onCellRender(function(TreeNode $node, UILabel $template){
            return $template;
        });

        $form->add($p);

        $form->on('windowClosing', function(){
            System::halt(0);
        });
    });
}

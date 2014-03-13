<?php
namespace {

    use php\lang\System;
    use php\swing\Image;
    use php\swing\SwingUtilities;
    use php\swing\tree\TreeNode;
    use php\swing\UIDialog;
    use php\swing\UIForm;
    use php\swing\UIManager;
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

        $p->root->add($one = new TreeNode("One"));
            $one->add(new TreeNode("USA"));
            $one->add(new TreeNode("RUSSIA"));

        $p->root->add(new TreeNode("Two"));
        $p->root->add(new TreeNode("Three"));
        $p->root->add(new TreeNode("Four"));
        $p->root->add(new TreeNode("Five"));
        $p->root->add(new TreeNode("Six"));
        $p->root->add(new TreeNode("Seven"));
        $p->root->add(new TreeNode("Eight"));

        $p->rowHeight = 20;
        $form->add($p);
        $p->makeVisible($one->getFirstChild());


        $p->on('selected', function(UITree $tree, TreeNode $node = null, TreeNode $oldNode = null){
            if ($node != null) {
                $tree->model->removeNodeFromParent($node);
            }
            //$tree->model->reload();
        });

        $form->on('windowClosing', function(){
            System::halt(0);
        });
    });
}

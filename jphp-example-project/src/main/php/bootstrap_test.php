<?php

use php\swing\Scope;
use php\swing\ScopeValue;
use php\swing\UIForm;

/**
 * Class FormScope
 * @property $title
 * @property FormScope $sub
 */
class FormScope extends Scope {

}

$scope = new FormScope();

$form = new UIForm();
$form->title = $scope->sub->title;
$form->size = [500, 500];

$form->visible = true;

$scope->sub->title = 'Ура!!!!';
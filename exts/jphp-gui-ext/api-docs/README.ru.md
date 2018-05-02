#### [English](README.md) / **Русский**

---

## jphp-gui-ext
> версия 1.0.0, создано с помощью JPPM.

GUI based on JavaFX library.

### Установка
```
jppm add jphp-gui-ext@1.0.0
```

### АПИ
**Классы**

#### `php\gui\animation`

- [`UXAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.ru.md)- _Abstract animation class._
- [`UXAnimationTimer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimationTimer.ru.md)- _Class UXAnimationTimer_
- [`UXFadeAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXFadeAnimation.ru.md)- _Class UXFadeAnimation_
- [`UXKeyFrame`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXKeyFrame.ru.md)- _Class UXKeyFrame_
- [`UXPathAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXPathAnimation.ru.md)- _Class UXPathAnimation_
- [`UXTimeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXTimeline.ru.md)- _Class UXTimeline_

#### `php\gui\effect`

- [`UXBloomEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXBloomEffect.ru.md)- _Class UXBloomEffect_
- [`UXColorAdjustEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXColorAdjustEffect.ru.md)- _Class UXColorAdjustEffect_
- [`UXDropShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXDropShadowEffect.ru.md)- _Class UXDropShadowEffect_
- [`UXEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.ru.md)- _Class UXEffect_
- [`UXEffectPipeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffectPipeline.ru.md)- _Class UXEffectPipeline_
- [`UXGaussianBlurEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGaussianBlurEffect.ru.md)- _Class UXGaussianBlurEffect_
- [`UXGlowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGlowEffect.ru.md)- _Class UXGlowEffect_
- [`UXInnerShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXInnerShadowEffect.ru.md)- _Class UXInnerShadowEffect_
- [`UXLightingEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXLightingEffect.ru.md)- _Class UXLightingEffect_
- [`UXReflectionEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXReflectionEffect.ru.md)- _Class UXReflectionEffect_
- [`UXSepiaToneEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXSepiaToneEffect.ru.md)- _Class UXSepiaToneEffect_

#### `php\gui\event`

- [`UXContextMenuEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXContextMenuEvent.ru.md)- _Class UXContextMenuEvent_
- [`UXDragEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXDragEvent.ru.md)- _Class UXDragEvent_
- [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)- _Class Event_
- [`UXKeyboardManager`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyboardManager.ru.md)- _Менеджер для отлова событий клавиатуры._
- [`UXKeyEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyEvent.ru.md)- _Class UXKeyEvent_
- [`UXMouseEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXMouseEvent.ru.md)- _Class UXMouseEvent_
- [`UXScrollEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXScrollEvent.ru.md)- _Class UXScrollEvent_
- [`UXWebErrorEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebErrorEvent.ru.md)- _Class UXWebErrorEvent_
- [`UXWebEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebEvent.ru.md)- _Class UXWebEvent_
- [`UXWindowEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWindowEvent.ru.md)- _Class UXWindowEvent_

#### `php\gui`

- [`JSException`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/JSException.ru.md)- _Class JSException_
- [`UXAlert`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXAlert.ru.md)- _Класс для отображения всплывающих диалогов с кнопками._
- [`UXApplication`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXApplication.ru.md)- _Class UXApplication_
- [`UXButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButton.ru.md)- _Class UXButton_
- [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)- _Class UXButtonBase_
- [`UXCanvas`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.ru.md)- _Class UXCanvas_
- [`UXCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCell.ru.md)- _Class UXCell_
- [`UXCheckbox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCheckbox.ru.md)- _Class UXCheckbox_
- [`UXChoiceBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXChoiceBox.ru.md)- _Class UXChoiceBox_
- [`UXClipboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXClipboard.ru.md)- _Класс для работы с буфером обмена._
- [`UXColorChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorChooser.ru.md)- _Class UXColorChooser_
- [`UXColorPicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorPicker.ru.md)- _Class UXColorPicker_
- [`UXComboBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBox.ru.md)- _Class UXComboBox_
- [`UXComboBoxBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.ru.md)- _Class UXComboBoxBase_
- [`UXContextMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXContextMenu.ru.md)- _Class UXContextMenu_
- [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)- _Class UXControl_
- [`UXCustomNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCustomNode.ru.md)- _Class UXCustomNode_
- [`UXData`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXData.ru.md)- _Class UXData_
- [`UXDatePicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDatePicker.ru.md)- _Class UXDatePicker_
- [`UXDesktop`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDesktop.ru.md)- _Class UXDesktop_
- [`UXDialog`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDialog.ru.md)- _Class UXDialog_
- [`UXDirectoryChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDirectoryChooser.ru.md)- _Class UXDirectoryChooser_
- [`UXDragboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDragboard.ru.md)- _Class UXDragboard_
- [`UXDraggableTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDraggableTab.ru.md)- _Class UXDraggableTab_
- [`UXFileChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFileChooser.ru.md)- _Class UXFileChooser_
- [`UXFlatButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFlatButton.ru.md)- _Class UXFlatButton_
- [`UXForm`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXForm.ru.md)
- [`UXGeometry`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGeometry.ru.md)- _Class UXGeometry_
- [`UXGraphicsContext`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGraphicsContext.ru.md)- _Class UXGraphicsContext_
- [`UXGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGroup.ru.md)- _Class UXGroup_
- [`UXHtmlEditor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHtmlEditor.ru.md)- _Class UXHtmlEditor_
- [`UXHyperlink`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHyperlink.ru.md)- _Class UXHyperlink_
- [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.ru.md)- _Class UXImage_
- [`UXImageArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageArea.ru.md)- _Class UXImageArea_
- [`UXImageView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageView.ru.md)- _Class UXImageView_
- [`UXLabel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabel.ru.md)- _Class UXLabel_
- [`UXLabeled`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.ru.md)- _Class UXLabeled_
- [`UXLabelEx`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabelEx.ru.md)- _Class UXLabelEx_
- [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md)- _Class UXList_
- [`UXListCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListCell.ru.md)- _Class UXTableCell_
- [`UXListView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListView.ru.md)- _Class UXListView_
- [`UXLoader`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLoader.ru.md)- _Class UXLoader_
- [`UXMaskTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMaskTextField.ru.md)
- [`UXMedia`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMedia.ru.md)- _Class UXMedia_
- [`UXMediaPlayer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaPlayer.ru.md)- _Class UXMediaPlayer_
- [`UXMediaView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaView.ru.md)- _Class UXMediaView_
- [`UXMediaViewBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaViewBox.ru.md)- _Class UXMediaViewBox_
- [`UXMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenu.ru.md)- _Class UXMenu_
- [`UXMenuBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuBar.ru.md)- _Class UXMenuBar_
- [`UXMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuButton.ru.md)- _Class UXMenuButton_
- [`UXMenuItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuItem.ru.md)- _Class UXMenuItem_
- [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)- _Class UXNode_
- [`UXNumberSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNumberSpinner.ru.md)
- [`UXPagination`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPagination.ru.md)- _Class UXPagination_
- [`UXParent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXParent.ru.md)- _Class UXParent_
- [`UXPasswordField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPasswordField.ru.md)- _Class UXPasswordField_
- [`UXPopupWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md)- _Class UXPopupWindow_
- [`UXProgressBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressBar.ru.md)- _Class UXProgressBar_
- [`UXProgressIndicator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressIndicator.ru.md)- _Class ProgressIndicator_
- [`UXRadioGroupPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXRadioGroupPane.ru.md)- _Class UXRadioGroupPane_
- [`UXScene`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScene.ru.md)- _Class UXScene_
- [`UXScreen`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScreen.ru.md)- _Class UXScreen_
- [`UXSeparator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSeparator.ru.md)- _Class UXSeparator_
- [`UXSlider`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSlider.ru.md)- _Class UXSlider_
- [`UXSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSpinner.ru.md)- _Class UXSpinner_
- [`UXSplitMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitMenuButton.ru.md)
- [`UXSplitPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitPane.ru.md)- _Class UXSplitPane_
- [`UXTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTab.ru.md)- _Class UXTab_
- [`UXTableCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableCell.ru.md)- _Class UXTableCell_
- [`UXTableColumn`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableColumn.ru.md)- _Class UXTableColumn_
- [`UXTableView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableView.ru.md)- _Class UXTableView_
- [`UXTabPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTabPane.ru.md)- _Class UXTabPane_
- [`UXTextArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextArea.ru.md)- _Class UXTextArea_
- [`UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.ru.md)- _Class UXTextField_
- [`UXTextInputControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextInputControl.ru.md)- _Class UXTextInputControl_
- [`UXTitledPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTitledPane.ru.md)- _Class UXTitledPane_
- [`UXToggleButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleButton.ru.md)- _Class UXToggleButton_
- [`UXToggleGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleGroup.ru.md)- _Class UXToggleGroup_
- [`UXTooltip`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTooltip.ru.md)- _Class UXTooltip_
- [`UXTrayNotification`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTrayNotification.ru.md)- _Class UXTrayNotification_
- [`UXTreeItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeItem.ru.md)- _Class UXTreeItem_
- [`UXTreeView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeView.ru.md)- _Class UXTreeView_
- [`UXValue`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXValue.ru.md)- _Class UXValue_
- [`UXWebEngine`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebEngine.ru.md)- _Class UXWebEngine_
- [`UXWebHistory`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebHistory.ru.md)- _Class UXWebHistory_
- [`UXWebView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebView.ru.md)- _Class UXWebView_
- [`UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md)- _Class UXWindow_

#### `php\gui\layout`

- [`UXAnchorPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXAnchorPane.ru.md)- _Class UXAnchorPane_
- [`UXFlowPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFlowPane.ru.md)- _Class UXFlowPane_
- [`UXFragmentPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFragmentPane.ru.md)
- [`UXHBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXHBox.ru.md)- _Class UXHBox_
- [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)- _Class UXPane_
- [`UXPanel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPanel.ru.md)- _Class UXPanel_
- [`UXRegion`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXRegion.ru.md)- _Class UXRegion_
- [`UXScrollPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXScrollPane.ru.md)- _Class UXScrollPane_
- [`UXStackPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXStackPane.ru.md)- _Class UXStackPane_
- [`UXTilePane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXTilePane.ru.md)
- [`UXVBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXVBox.ru.md)- _Class UXVBox_

#### `php\gui\paint`

- [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.ru.md)- _Class UXColor_

#### `php\gui\printing`

- [`UXPrinter`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinter.ru.md)
- [`UXPrinterJob`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinterJob.ru.md)

#### `php\gui\shape`

- [`UXCircle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXCircle.ru.md)- _Class UXCircle_
- [`UXEllipse`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXEllipse.ru.md)- _Class UXEllipse_
- [`UXPolygon`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXPolygon.ru.md)- _Class UXEllipse_
- [`UXRectangle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXRectangle.ru.md)- _Class UXCircle_
- [`UXShape`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXShape.ru.md)- _Class UXShape_

#### `php\gui\text`

- [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.ru.md)- _Class UXFont_
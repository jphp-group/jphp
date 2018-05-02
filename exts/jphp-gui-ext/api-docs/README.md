#### **English** / [Русский](README.ru.md)

---

## jphp-gui-ext
> version 1.0.0, created by JPPM.

GUI based on JavaFX library.

### Install
```
jppm add jphp-gui-ext@1.0.0
```

### API
**Classes**

#### `php\gui\animation`

- [`UXAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.md)- _Abstract animation class._
- [`UXAnimationTimer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimationTimer.md)- _Class UXAnimationTimer_
- [`UXFadeAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXFadeAnimation.md)- _Class UXFadeAnimation_
- [`UXKeyFrame`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXKeyFrame.md)- _Class UXKeyFrame_
- [`UXPathAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXPathAnimation.md)- _Class UXPathAnimation_
- [`UXTimeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXTimeline.md)- _Class UXTimeline_

#### `php\gui\effect`

- [`UXBloomEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXBloomEffect.md)- _Class UXBloomEffect_
- [`UXColorAdjustEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXColorAdjustEffect.md)- _Class UXColorAdjustEffect_
- [`UXDropShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXDropShadowEffect.md)- _Class UXDropShadowEffect_
- [`UXEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.md)- _Class UXEffect_
- [`UXEffectPipeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffectPipeline.md)- _Class UXEffectPipeline_
- [`UXGaussianBlurEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGaussianBlurEffect.md)- _Class UXGaussianBlurEffect_
- [`UXGlowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGlowEffect.md)- _Class UXGlowEffect_
- [`UXInnerShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXInnerShadowEffect.md)- _Class UXInnerShadowEffect_
- [`UXLightingEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXLightingEffect.md)- _Class UXLightingEffect_
- [`UXReflectionEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXReflectionEffect.md)- _Class UXReflectionEffect_
- [`UXSepiaToneEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXSepiaToneEffect.md)- _Class UXSepiaToneEffect_

#### `php\gui\event`

- [`UXContextMenuEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXContextMenuEvent.md)- _Class UXContextMenuEvent_
- [`UXDragEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXDragEvent.md)- _Class UXDragEvent_
- [`UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.md)- _Class Event_
- [`UXKeyboardManager`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyboardManager.md)- _Менеджер для отлова событий клавиатуры._
- [`UXKeyEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyEvent.md)- _Class UXKeyEvent_
- [`UXMouseEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXMouseEvent.md)- _Class UXMouseEvent_
- [`UXScrollEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXScrollEvent.md)- _Class UXScrollEvent_
- [`UXWebErrorEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebErrorEvent.md)- _Class UXWebErrorEvent_
- [`UXWebEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebEvent.md)- _Class UXWebEvent_
- [`UXWindowEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWindowEvent.md)- _Class UXWindowEvent_

#### `php\gui`

- [`JSException`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/JSException.md)- _Class JSException_
- [`UXAlert`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXAlert.md)- _Class UXAlert_
- [`UXApplication`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXApplication.md)- _Class UXApplication_
- [`UXButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButton.md)- _Class UXButton_
- [`UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.md)- _Class UXButtonBase_
- [`UXCanvas`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.md)- _Class UXCanvas_
- [`UXCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCell.md)- _Class UXCell_
- [`UXCheckbox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCheckbox.md)- _Class UXCheckbox_
- [`UXChoiceBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXChoiceBox.md)- _Class UXChoiceBox_
- [`UXClipboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXClipboard.md)- _Class UXClipboard_
- [`UXColorChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorChooser.md)- _Class UXColorChooser_
- [`UXColorPicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorPicker.md)- _Class UXColorPicker_
- [`UXComboBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBox.md)- _Class UXComboBox_
- [`UXComboBoxBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.md)- _Class UXComboBoxBase_
- [`UXContextMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXContextMenu.md)- _Class UXContextMenu_
- [`UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.md)- _Class UXControl_
- [`UXCustomNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCustomNode.md)- _Class UXCustomNode_
- [`UXData`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXData.md)- _Class UXData_
- [`UXDatePicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDatePicker.md)- _Class UXDatePicker_
- [`UXDesktop`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDesktop.md)- _Class UXDesktop_
- [`UXDialog`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDialog.md)- _Class UXDialog_
- [`UXDirectoryChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDirectoryChooser.md)- _Class UXDirectoryChooser_
- [`UXDragboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDragboard.md)- _Class UXDragboard_
- [`UXDraggableTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDraggableTab.md)- _Class UXDraggableTab_
- [`UXFileChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFileChooser.md)- _Class UXFileChooser_
- [`UXFlatButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFlatButton.md)- _Class UXFlatButton_
- [`UXForm`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXForm.md)
- [`UXGeometry`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGeometry.md)- _Class UXGeometry_
- [`UXGraphicsContext`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGraphicsContext.md)- _Class UXGraphicsContext_
- [`UXGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGroup.md)- _Class UXGroup_
- [`UXHtmlEditor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHtmlEditor.md)- _Class UXHtmlEditor_
- [`UXHyperlink`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHyperlink.md)- _Class UXHyperlink_
- [`UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.md)- _Class UXImage_
- [`UXImageArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageArea.md)- _Class UXImageArea_
- [`UXImageView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageView.md)- _Class UXImageView_
- [`UXLabel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabel.md)- _Class UXLabel_
- [`UXLabeled`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.md)- _Class UXLabeled_
- [`UXLabelEx`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabelEx.md)- _Class UXLabelEx_
- [`UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.md)- _Class UXList_
- [`UXListCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListCell.md)- _Class UXTableCell_
- [`UXListView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListView.md)- _Class UXListView_
- [`UXLoader`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLoader.md)- _Class UXLoader_
- [`UXMaskTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMaskTextField.md)
- [`UXMedia`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMedia.md)- _Class UXMedia_
- [`UXMediaPlayer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaPlayer.md)- _Class UXMediaPlayer_
- [`UXMediaView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaView.md)- _Class UXMediaView_
- [`UXMediaViewBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaViewBox.md)- _Class UXMediaViewBox_
- [`UXMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenu.md)- _Class UXMenu_
- [`UXMenuBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuBar.md)- _Class UXMenuBar_
- [`UXMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuButton.md)- _Class UXMenuButton_
- [`UXMenuItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuItem.md)- _Class UXMenuItem_
- [`UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.md)- _Class UXNode_
- [`UXNumberSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNumberSpinner.md)
- [`UXPagination`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPagination.md)- _Class UXPagination_
- [`UXParent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXParent.md)- _Class UXParent_
- [`UXPasswordField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPasswordField.md)- _Class UXPasswordField_
- [`UXPopupWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.md)- _Class UXPopupWindow_
- [`UXProgressBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressBar.md)- _Class UXProgressBar_
- [`UXProgressIndicator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressIndicator.md)- _Class ProgressIndicator_
- [`UXRadioGroupPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXRadioGroupPane.md)- _Class UXRadioGroupPane_
- [`UXScene`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScene.md)- _Class UXScene_
- [`UXScreen`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScreen.md)- _Class UXScreen_
- [`UXSeparator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSeparator.md)- _Class UXSeparator_
- [`UXSlider`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSlider.md)- _Class UXSlider_
- [`UXSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSpinner.md)- _Class UXSpinner_
- [`UXSplitMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitMenuButton.md)
- [`UXSplitPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitPane.md)- _Class UXSplitPane_
- [`UXTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTab.md)- _Class UXTab_
- [`UXTableCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableCell.md)- _Class UXTableCell_
- [`UXTableColumn`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableColumn.md)- _Class UXTableColumn_
- [`UXTableView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableView.md)- _Class UXTableView_
- [`UXTabPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTabPane.md)- _Class UXTabPane_
- [`UXTextArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextArea.md)- _Class UXTextArea_
- [`UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.md)- _Class UXTextField_
- [`UXTextInputControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextInputControl.md)- _Class UXTextInputControl_
- [`UXTitledPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTitledPane.md)- _Class UXTitledPane_
- [`UXToggleButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleButton.md)- _Class UXToggleButton_
- [`UXToggleGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleGroup.md)- _Class UXToggleGroup_
- [`UXTooltip`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTooltip.md)- _Class UXTooltip_
- [`UXTrayNotification`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTrayNotification.md)- _Class UXTrayNotification_
- [`UXTreeItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeItem.md)- _Class UXTreeItem_
- [`UXTreeView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeView.md)- _Class UXTreeView_
- [`UXValue`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXValue.md)- _Class UXValue_
- [`UXWebEngine`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebEngine.md)- _Class UXWebEngine_
- [`UXWebHistory`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebHistory.md)- _Class UXWebHistory_
- [`UXWebView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebView.md)- _Class UXWebView_
- [`UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.md)- _Class UXWindow_

#### `php\gui\layout`

- [`UXAnchorPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXAnchorPane.md)- _Class UXAnchorPane_
- [`UXFlowPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFlowPane.md)- _Class UXFlowPane_
- [`UXFragmentPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFragmentPane.md)
- [`UXHBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXHBox.md)- _Class UXHBox_
- [`UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.md)- _Class UXPane_
- [`UXPanel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPanel.md)- _Class UXPanel_
- [`UXRegion`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXRegion.md)- _Class UXRegion_
- [`UXScrollPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXScrollPane.md)- _Class UXScrollPane_
- [`UXStackPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXStackPane.md)- _Class UXStackPane_
- [`UXTilePane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXTilePane.md)
- [`UXVBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXVBox.md)- _Class UXVBox_

#### `php\gui\paint`

- [`UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.md)- _Class UXColor_

#### `php\gui\printing`

- [`UXPrinter`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinter.md)
- [`UXPrinterJob`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinterJob.md)

#### `php\gui\shape`

- [`UXCircle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXCircle.md)- _Class UXCircle_
- [`UXEllipse`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXEllipse.md)- _Class UXEllipse_
- [`UXPolygon`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXPolygon.md)- _Class UXEllipse_
- [`UXRectangle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXRectangle.md)- _Class UXCircle_
- [`UXShape`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXShape.md)- _Class UXShape_

#### `php\gui\text`

- [`UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.md)- _Class UXFont_
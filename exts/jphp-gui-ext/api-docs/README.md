#### **English** / [Русский](README.ru.md)

---

## jphp-gui-ext
> version 1.0.0, created by JPPM v0.1.15

GUI based on JavaFX library.

### Install
```
jppm add jphp-gui-ext@1.0.0
```

### API
**Classes**
- [`php\gui\animation\UXAnimation`](api-docs/classes/php/gui/animation/UXAnimation.md)- _Abstract animation class._
- [`php\gui\animation\UXAnimationTimer`](api-docs/classes/php/gui/animation/UXAnimationTimer.md)- _Class UXAnimationTimer_
- [`php\gui\animation\UXFadeAnimation`](api-docs/classes/php/gui/animation/UXFadeAnimation.md)- _Class UXFadeAnimation_
- [`php\gui\animation\UXKeyFrame`](api-docs/classes/php/gui/animation/UXKeyFrame.md)- _Class UXKeyFrame_
- [`php\gui\animation\UXPathAnimation`](api-docs/classes/php/gui/animation/UXPathAnimation.md)- _Class UXPathAnimation_
- [`php\gui\animation\UXTimeline`](api-docs/classes/php/gui/animation/UXTimeline.md)- _Class UXTimeline_
- [`php\gui\effect\UXBloomEffect`](api-docs/classes/php/gui/effect/UXBloomEffect.md)- _Class UXBloomEffect_
- [`php\gui\effect\UXColorAdjustEffect`](api-docs/classes/php/gui/effect/UXColorAdjustEffect.md)- _Class UXColorAdjustEffect_
- [`php\gui\effect\UXDropShadowEffect`](api-docs/classes/php/gui/effect/UXDropShadowEffect.md)- _Class UXDropShadowEffect_
- [`php\gui\effect\UXEffect`](api-docs/classes/php/gui/effect/UXEffect.md)- _Class UXEffect_
- [`php\gui\effect\UXEffectPipeline`](api-docs/classes/php/gui/effect/UXEffectPipeline.md)- _Class UXEffectPipeline_
- [`php\gui\effect\UXGaussianBlurEffect`](api-docs/classes/php/gui/effect/UXGaussianBlurEffect.md)- _Class UXGaussianBlurEffect_
- [`php\gui\effect\UXGlowEffect`](api-docs/classes/php/gui/effect/UXGlowEffect.md)- _Class UXGlowEffect_
- [`php\gui\effect\UXInnerShadowEffect`](api-docs/classes/php/gui/effect/UXInnerShadowEffect.md)- _Class UXInnerShadowEffect_
- [`php\gui\effect\UXLightingEffect`](api-docs/classes/php/gui/effect/UXLightingEffect.md)- _Class UXLightingEffect_
- [`php\gui\effect\UXReflectionEffect`](api-docs/classes/php/gui/effect/UXReflectionEffect.md)- _Class UXReflectionEffect_
- [`php\gui\effect\UXSepiaToneEffect`](api-docs/classes/php/gui/effect/UXSepiaToneEffect.md)- _Class UXSepiaToneEffect_
- [`php\gui\event\UXContextMenuEvent`](api-docs/classes/php/gui/event/UXContextMenuEvent.md)- _Class UXContextMenuEvent_
- [`php\gui\event\UXDragEvent`](api-docs/classes/php/gui/event/UXDragEvent.md)- _Class UXDragEvent_
- [`php\gui\event\UXEvent`](api-docs/classes/php/gui/event/UXEvent.md)- _Class Event_
- [`php\gui\event\UXKeyboardManager`](api-docs/classes/php/gui/event/UXKeyboardManager.md)- _Менеджер для отлова событий клавиатуры._
- [`php\gui\event\UXKeyEvent`](api-docs/classes/php/gui/event/UXKeyEvent.md)- _Class UXKeyEvent_
- [`php\gui\event\UXMouseEvent`](api-docs/classes/php/gui/event/UXMouseEvent.md)- _Class UXMouseEvent_
- [`php\gui\event\UXScrollEvent`](api-docs/classes/php/gui/event/UXScrollEvent.md)- _Class UXScrollEvent_
- [`php\gui\event\UXWebErrorEvent`](api-docs/classes/php/gui/event/UXWebErrorEvent.md)- _Class UXWebErrorEvent_
- [`php\gui\event\UXWebEvent`](api-docs/classes/php/gui/event/UXWebEvent.md)- _Class UXWebEvent_
- [`php\gui\event\UXWindowEvent`](api-docs/classes/php/gui/event/UXWindowEvent.md)- _Class UXWindowEvent_
- [`php\gui\JSException`](api-docs/classes/php/gui/JSException.md)- _Class JSException_
- [`php\gui\layout\UXAnchorPane`](api-docs/classes/php/gui/layout/UXAnchorPane.md)- _Class UXAnchorPane_
- [`php\gui\layout\UXFlowPane`](api-docs/classes/php/gui/layout/UXFlowPane.md)- _Class UXFlowPane_
- [`php\gui\layout\UXFragmentPane`](api-docs/classes/php/gui/layout/UXFragmentPane.md)
- [`php\gui\layout\UXHBox`](api-docs/classes/php/gui/layout/UXHBox.md)- _Class UXHBox_
- [`php\gui\layout\UXPane`](api-docs/classes/php/gui/layout/UXPane.md)- _Class UXPane_
- [`php\gui\layout\UXPanel`](api-docs/classes/php/gui/layout/UXPanel.md)- _Class UXPanel_
- [`php\gui\layout\UXRegion`](api-docs/classes/php/gui/layout/UXRegion.md)- _Class UXRegion_
- [`php\gui\layout\UXScrollPane`](api-docs/classes/php/gui/layout/UXScrollPane.md)- _Class UXScrollPane_
- [`php\gui\layout\UXStackPane`](api-docs/classes/php/gui/layout/UXStackPane.md)- _Class UXStackPane_
- [`php\gui\layout\UXTilePane`](api-docs/classes/php/gui/layout/UXTilePane.md)
- [`php\gui\layout\UXVBox`](api-docs/classes/php/gui/layout/UXVBox.md)- _Class UXVBox_
- [`php\gui\paint\UXColor`](api-docs/classes/php/gui/paint/UXColor.md)- _Class UXColor_
- [`php\gui\printing\UXPrinter`](api-docs/classes/php/gui/printing/UXPrinter.md)
- [`php\gui\printing\UXPrinterJob`](api-docs/classes/php/gui/printing/UXPrinterJob.md)
- [`php\gui\shape\UXCircle`](api-docs/classes/php/gui/shape/UXCircle.md)- _Class UXCircle_
- [`php\gui\shape\UXEllipse`](api-docs/classes/php/gui/shape/UXEllipse.md)- _Class UXEllipse_
- [`php\gui\shape\UXPolygon`](api-docs/classes/php/gui/shape/UXPolygon.md)- _Class UXEllipse_
- [`php\gui\shape\UXRectangle`](api-docs/classes/php/gui/shape/UXRectangle.md)- _Class UXCircle_
- [`php\gui\shape\UXShape`](api-docs/classes/php/gui/shape/UXShape.md)- _Class UXShape_
- [`php\gui\text\UXFont`](api-docs/classes/php/gui/text/UXFont.md)- _Class UXFont_
- [`php\gui\UXAlert`](api-docs/classes/php/gui/UXAlert.md)- _Class UXAlert_
- [`php\gui\UXApplication`](api-docs/classes/php/gui/UXApplication.md)- _Class UXApplication_
- [`php\gui\UXButton`](api-docs/classes/php/gui/UXButton.md)- _Class UXButton_
- [`php\gui\UXButtonBase`](api-docs/classes/php/gui/UXButtonBase.md)- _Class UXButtonBase_
- [`php\gui\UXCanvas`](api-docs/classes/php/gui/UXCanvas.md)- _Class UXCanvas_
- [`php\gui\UXCell`](api-docs/classes/php/gui/UXCell.md)- _Class UXCell_
- [`php\gui\UXCheckbox`](api-docs/classes/php/gui/UXCheckbox.md)- _Class UXCheckbox_
- [`php\gui\UXChoiceBox`](api-docs/classes/php/gui/UXChoiceBox.md)- _Class UXChoiceBox_
- [`php\gui\UXClipboard`](api-docs/classes/php/gui/UXClipboard.md)- _Class UXClipboard_
- [`php\gui\UXColorChooser`](api-docs/classes/php/gui/UXColorChooser.md)- _Class UXColorChooser_
- [`php\gui\UXColorPicker`](api-docs/classes/php/gui/UXColorPicker.md)- _Class UXColorPicker_
- [`php\gui\UXComboBox`](api-docs/classes/php/gui/UXComboBox.md)- _Class UXComboBox_
- [`php\gui\UXComboBoxBase`](api-docs/classes/php/gui/UXComboBoxBase.md)- _Class UXComboBoxBase_
- [`php\gui\UXContextMenu`](api-docs/classes/php/gui/UXContextMenu.md)- _Class UXContextMenu_
- [`php\gui\UXControl`](api-docs/classes/php/gui/UXControl.md)- _Class UXControl_
- [`php\gui\UXCustomNode`](api-docs/classes/php/gui/UXCustomNode.md)- _Class UXCustomNode_
- [`php\gui\UXData`](api-docs/classes/php/gui/UXData.md)- _Class UXData_
- [`php\gui\UXDatePicker`](api-docs/classes/php/gui/UXDatePicker.md)- _Class UXDatePicker_
- [`php\gui\UXDesktop`](api-docs/classes/php/gui/UXDesktop.md)- _Class UXDesktop_
- [`php\gui\UXDialog`](api-docs/classes/php/gui/UXDialog.md)- _Class UXDialog_
- [`php\gui\UXDirectoryChooser`](api-docs/classes/php/gui/UXDirectoryChooser.md)- _Class UXDirectoryChooser_
- [`php\gui\UXDragboard`](api-docs/classes/php/gui/UXDragboard.md)- _Class UXDragboard_
- [`php\gui\UXDraggableTab`](api-docs/classes/php/gui/UXDraggableTab.md)- _Class UXDraggableTab_
- [`php\gui\UXFileChooser`](api-docs/classes/php/gui/UXFileChooser.md)- _Class UXFileChooser_
- [`php\gui\UXFlatButton`](api-docs/classes/php/gui/UXFlatButton.md)- _Class UXFlatButton_
- [`php\gui\UXForm`](api-docs/classes/php/gui/UXForm.md)
- [`php\gui\UXGeometry`](api-docs/classes/php/gui/UXGeometry.md)- _Class UXGeometry_
- [`php\gui\UXGraphicsContext`](api-docs/classes/php/gui/UXGraphicsContext.md)- _Class UXGraphicsContext_
- [`php\gui\UXGroup`](api-docs/classes/php/gui/UXGroup.md)- _Class UXGroup_
- [`php\gui\UXHtmlEditor`](api-docs/classes/php/gui/UXHtmlEditor.md)- _Class UXHtmlEditor_
- [`php\gui\UXHyperlink`](api-docs/classes/php/gui/UXHyperlink.md)- _Class UXHyperlink_
- [`php\gui\UXImage`](api-docs/classes/php/gui/UXImage.md)- _Class UXImage_
- [`php\gui\UXImageArea`](api-docs/classes/php/gui/UXImageArea.md)- _Class UXImageArea_
- [`php\gui\UXImageView`](api-docs/classes/php/gui/UXImageView.md)- _Class UXImageView_
- [`php\gui\UXLabel`](api-docs/classes/php/gui/UXLabel.md)- _Class UXLabel_
- [`php\gui\UXLabeled`](api-docs/classes/php/gui/UXLabeled.md)- _Class UXLabeled_
- [`php\gui\UXLabelEx`](api-docs/classes/php/gui/UXLabelEx.md)- _Class UXLabelEx_
- [`php\gui\UXList`](api-docs/classes/php/gui/UXList.md)- _Class UXList_
- [`php\gui\UXListCell`](api-docs/classes/php/gui/UXListCell.md)- _Class UXTableCell_
- [`php\gui\UXListView`](api-docs/classes/php/gui/UXListView.md)- _Class UXListView_
- [`php\gui\UXLoader`](api-docs/classes/php/gui/UXLoader.md)- _Class UXLoader_
- [`php\gui\UXMaskTextField`](api-docs/classes/php/gui/UXMaskTextField.md)
- [`php\gui\UXMedia`](api-docs/classes/php/gui/UXMedia.md)- _Class UXMedia_
- [`php\gui\UXMediaPlayer`](api-docs/classes/php/gui/UXMediaPlayer.md)- _Class UXMediaPlayer_
- [`php\gui\UXMediaView`](api-docs/classes/php/gui/UXMediaView.md)- _Class UXMediaView_
- [`php\gui\UXMediaViewBox`](api-docs/classes/php/gui/UXMediaViewBox.md)- _Class UXMediaViewBox_
- [`php\gui\UXMenu`](api-docs/classes/php/gui/UXMenu.md)- _Class UXMenu_
- [`php\gui\UXMenuBar`](api-docs/classes/php/gui/UXMenuBar.md)- _Class UXMenuBar_
- [`php\gui\UXMenuButton`](api-docs/classes/php/gui/UXMenuButton.md)- _Class UXMenuButton_
- [`php\gui\UXMenuItem`](api-docs/classes/php/gui/UXMenuItem.md)- _Class UXMenuItem_
- [`php\gui\UXNode`](api-docs/classes/php/gui/UXNode.md)- _Class UXNode_
- [`php\gui\UXNumberSpinner`](api-docs/classes/php/gui/UXNumberSpinner.md)
- [`php\gui\UXPagination`](api-docs/classes/php/gui/UXPagination.md)- _Class UXPagination_
- [`php\gui\UXParent`](api-docs/classes/php/gui/UXParent.md)- _Class UXParent_
- [`php\gui\UXPasswordField`](api-docs/classes/php/gui/UXPasswordField.md)- _Class UXPasswordField_
- [`php\gui\UXPopupWindow`](api-docs/classes/php/gui/UXPopupWindow.md)- _Class UXPopupWindow_
- [`php\gui\UXProgressBar`](api-docs/classes/php/gui/UXProgressBar.md)- _Class UXProgressBar_
- [`php\gui\UXProgressIndicator`](api-docs/classes/php/gui/UXProgressIndicator.md)- _Class ProgressIndicator_
- [`php\gui\UXRadioGroupPane`](api-docs/classes/php/gui/UXRadioGroupPane.md)- _Class UXRadioGroupPane_
- [`php\gui\UXScene`](api-docs/classes/php/gui/UXScene.md)- _Class UXScene_
- [`php\gui\UXScreen`](api-docs/classes/php/gui/UXScreen.md)- _Class UXScreen_
- [`php\gui\UXSeparator`](api-docs/classes/php/gui/UXSeparator.md)- _Class UXSeparator_
- [`php\gui\UXSlider`](api-docs/classes/php/gui/UXSlider.md)- _Class UXSlider_
- [`php\gui\UXSpinner`](api-docs/classes/php/gui/UXSpinner.md)- _Class UXSpinner_
- [`php\gui\UXSplitMenuButton`](api-docs/classes/php/gui/UXSplitMenuButton.md)
- [`php\gui\UXSplitPane`](api-docs/classes/php/gui/UXSplitPane.md)- _Class UXSplitPane_
- [`php\gui\UXTab`](api-docs/classes/php/gui/UXTab.md)- _Class UXTab_
- [`php\gui\UXTableCell`](api-docs/classes/php/gui/UXTableCell.md)- _Class UXTableCell_
- [`php\gui\UXTableColumn`](api-docs/classes/php/gui/UXTableColumn.md)- _Class UXTableColumn_
- [`php\gui\UXTableView`](api-docs/classes/php/gui/UXTableView.md)- _Class UXTableView_
- [`php\gui\UXTabPane`](api-docs/classes/php/gui/UXTabPane.md)- _Class UXTabPane_
- [`php\gui\UXTextArea`](api-docs/classes/php/gui/UXTextArea.md)- _Class UXTextArea_
- [`php\gui\UXTextField`](api-docs/classes/php/gui/UXTextField.md)- _Class UXTextField_
- [`php\gui\UXTextInputControl`](api-docs/classes/php/gui/UXTextInputControl.md)- _Class UXTextInputControl_
- [`php\gui\UXTitledPane`](api-docs/classes/php/gui/UXTitledPane.md)- _Class UXTitledPane_
- [`php\gui\UXToggleButton`](api-docs/classes/php/gui/UXToggleButton.md)- _Class UXToggleButton_
- [`php\gui\UXToggleGroup`](api-docs/classes/php/gui/UXToggleGroup.md)- _Class UXToggleGroup_
- [`php\gui\UXTooltip`](api-docs/classes/php/gui/UXTooltip.md)- _Class UXTooltip_
- [`php\gui\UXTrayNotification`](api-docs/classes/php/gui/UXTrayNotification.md)- _Class UXTrayNotification_
- [`php\gui\UXTreeItem`](api-docs/classes/php/gui/UXTreeItem.md)- _Class UXTreeItem_
- [`php\gui\UXTreeView`](api-docs/classes/php/gui/UXTreeView.md)- _Class UXTreeView_
- [`php\gui\UXValue`](api-docs/classes/php/gui/UXValue.md)- _Class UXValue_
- [`php\gui\UXWebEngine`](api-docs/classes/php/gui/UXWebEngine.md)- _Class UXWebEngine_
- [`php\gui\UXWebHistory`](api-docs/classes/php/gui/UXWebHistory.md)- _Class UXWebHistory_
- [`php\gui\UXWebView`](api-docs/classes/php/gui/UXWebView.md)- _Class UXWebView_
- [`php\gui\UXWindow`](api-docs/classes/php/gui/UXWindow.md)- _Class UXWindow_
#### [English](README.md) / **Русский**

---

## jphp-gui-ext
> версия 1.0.0, создано с помощью JPPM v0.1.15

GUI based on JavaFX library.

### Установка
```
jppm add jphp-gui-ext@1.0.0
```

### АПИ
**Классы**
- [`php\gui\animation\UXAnimation`](api-docs/classes/php/gui/animation/UXAnimation.ru.md)- _Abstract animation class._
- [`php\gui\animation\UXAnimationTimer`](api-docs/classes/php/gui/animation/UXAnimationTimer.ru.md)- _Class UXAnimationTimer_
- [`php\gui\animation\UXFadeAnimation`](api-docs/classes/php/gui/animation/UXFadeAnimation.ru.md)- _Class UXFadeAnimation_
- [`php\gui\animation\UXKeyFrame`](api-docs/classes/php/gui/animation/UXKeyFrame.ru.md)- _Class UXKeyFrame_
- [`php\gui\animation\UXPathAnimation`](api-docs/classes/php/gui/animation/UXPathAnimation.ru.md)- _Class UXPathAnimation_
- [`php\gui\animation\UXTimeline`](api-docs/classes/php/gui/animation/UXTimeline.ru.md)- _Class UXTimeline_
- [`php\gui\effect\UXBloomEffect`](api-docs/classes/php/gui/effect/UXBloomEffect.ru.md)- _Class UXBloomEffect_
- [`php\gui\effect\UXColorAdjustEffect`](api-docs/classes/php/gui/effect/UXColorAdjustEffect.ru.md)- _Class UXColorAdjustEffect_
- [`php\gui\effect\UXDropShadowEffect`](api-docs/classes/php/gui/effect/UXDropShadowEffect.ru.md)- _Class UXDropShadowEffect_
- [`php\gui\effect\UXEffect`](api-docs/classes/php/gui/effect/UXEffect.ru.md)- _Class UXEffect_
- [`php\gui\effect\UXEffectPipeline`](api-docs/classes/php/gui/effect/UXEffectPipeline.ru.md)- _Class UXEffectPipeline_
- [`php\gui\effect\UXGaussianBlurEffect`](api-docs/classes/php/gui/effect/UXGaussianBlurEffect.ru.md)- _Class UXGaussianBlurEffect_
- [`php\gui\effect\UXGlowEffect`](api-docs/classes/php/gui/effect/UXGlowEffect.ru.md)- _Class UXGlowEffect_
- [`php\gui\effect\UXInnerShadowEffect`](api-docs/classes/php/gui/effect/UXInnerShadowEffect.ru.md)- _Class UXInnerShadowEffect_
- [`php\gui\effect\UXLightingEffect`](api-docs/classes/php/gui/effect/UXLightingEffect.ru.md)- _Class UXLightingEffect_
- [`php\gui\effect\UXReflectionEffect`](api-docs/classes/php/gui/effect/UXReflectionEffect.ru.md)- _Class UXReflectionEffect_
- [`php\gui\effect\UXSepiaToneEffect`](api-docs/classes/php/gui/effect/UXSepiaToneEffect.ru.md)- _Class UXSepiaToneEffect_
- [`php\gui\event\UXContextMenuEvent`](api-docs/classes/php/gui/event/UXContextMenuEvent.ru.md)- _Class UXContextMenuEvent_
- [`php\gui\event\UXDragEvent`](api-docs/classes/php/gui/event/UXDragEvent.ru.md)- _Class UXDragEvent_
- [`php\gui\event\UXEvent`](api-docs/classes/php/gui/event/UXEvent.ru.md)- _Class Event_
- [`php\gui\event\UXKeyboardManager`](api-docs/classes/php/gui/event/UXKeyboardManager.ru.md)- _Менеджер для отлова событий клавиатуры._
- [`php\gui\event\UXKeyEvent`](api-docs/classes/php/gui/event/UXKeyEvent.ru.md)- _Class UXKeyEvent_
- [`php\gui\event\UXMouseEvent`](api-docs/classes/php/gui/event/UXMouseEvent.ru.md)- _Class UXMouseEvent_
- [`php\gui\event\UXScrollEvent`](api-docs/classes/php/gui/event/UXScrollEvent.ru.md)- _Class UXScrollEvent_
- [`php\gui\event\UXWebErrorEvent`](api-docs/classes/php/gui/event/UXWebErrorEvent.ru.md)- _Class UXWebErrorEvent_
- [`php\gui\event\UXWebEvent`](api-docs/classes/php/gui/event/UXWebEvent.ru.md)- _Class UXWebEvent_
- [`php\gui\event\UXWindowEvent`](api-docs/classes/php/gui/event/UXWindowEvent.ru.md)- _Class UXWindowEvent_
- [`php\gui\JSException`](api-docs/classes/php/gui/JSException.ru.md)- _Class JSException_
- [`php\gui\layout\UXAnchorPane`](api-docs/classes/php/gui/layout/UXAnchorPane.ru.md)- _Class UXAnchorPane_
- [`php\gui\layout\UXFlowPane`](api-docs/classes/php/gui/layout/UXFlowPane.ru.md)- _Class UXFlowPane_
- [`php\gui\layout\UXFragmentPane`](api-docs/classes/php/gui/layout/UXFragmentPane.ru.md)
- [`php\gui\layout\UXHBox`](api-docs/classes/php/gui/layout/UXHBox.ru.md)- _Class UXHBox_
- [`php\gui\layout\UXPane`](api-docs/classes/php/gui/layout/UXPane.ru.md)- _Class UXPane_
- [`php\gui\layout\UXPanel`](api-docs/classes/php/gui/layout/UXPanel.ru.md)- _Class UXPanel_
- [`php\gui\layout\UXRegion`](api-docs/classes/php/gui/layout/UXRegion.ru.md)- _Class UXRegion_
- [`php\gui\layout\UXScrollPane`](api-docs/classes/php/gui/layout/UXScrollPane.ru.md)- _Class UXScrollPane_
- [`php\gui\layout\UXStackPane`](api-docs/classes/php/gui/layout/UXStackPane.ru.md)- _Class UXStackPane_
- [`php\gui\layout\UXTilePane`](api-docs/classes/php/gui/layout/UXTilePane.ru.md)
- [`php\gui\layout\UXVBox`](api-docs/classes/php/gui/layout/UXVBox.ru.md)- _Class UXVBox_
- [`php\gui\paint\UXColor`](api-docs/classes/php/gui/paint/UXColor.ru.md)- _Class UXColor_
- [`php\gui\printing\UXPrinter`](api-docs/classes/php/gui/printing/UXPrinter.ru.md)
- [`php\gui\printing\UXPrinterJob`](api-docs/classes/php/gui/printing/UXPrinterJob.ru.md)
- [`php\gui\shape\UXCircle`](api-docs/classes/php/gui/shape/UXCircle.ru.md)- _Class UXCircle_
- [`php\gui\shape\UXEllipse`](api-docs/classes/php/gui/shape/UXEllipse.ru.md)- _Class UXEllipse_
- [`php\gui\shape\UXPolygon`](api-docs/classes/php/gui/shape/UXPolygon.ru.md)- _Class UXEllipse_
- [`php\gui\shape\UXRectangle`](api-docs/classes/php/gui/shape/UXRectangle.ru.md)- _Class UXCircle_
- [`php\gui\shape\UXShape`](api-docs/classes/php/gui/shape/UXShape.ru.md)- _Class UXShape_
- [`php\gui\text\UXFont`](api-docs/classes/php/gui/text/UXFont.ru.md)- _Class UXFont_
- [`php\gui\UXAlert`](api-docs/classes/php/gui/UXAlert.ru.md)- _Класс для отображения всплывающих диалогов с кнопками._
- [`php\gui\UXApplication`](api-docs/classes/php/gui/UXApplication.ru.md)- _Class UXApplication_
- [`php\gui\UXButton`](api-docs/classes/php/gui/UXButton.ru.md)- _Class UXButton_
- [`php\gui\UXButtonBase`](api-docs/classes/php/gui/UXButtonBase.ru.md)- _Class UXButtonBase_
- [`php\gui\UXCanvas`](api-docs/classes/php/gui/UXCanvas.ru.md)- _Class UXCanvas_
- [`php\gui\UXCell`](api-docs/classes/php/gui/UXCell.ru.md)- _Class UXCell_
- [`php\gui\UXCheckbox`](api-docs/classes/php/gui/UXCheckbox.ru.md)- _Class UXCheckbox_
- [`php\gui\UXChoiceBox`](api-docs/classes/php/gui/UXChoiceBox.ru.md)- _Class UXChoiceBox_
- [`php\gui\UXClipboard`](api-docs/classes/php/gui/UXClipboard.ru.md)- _Класс для работы с буфером обмена._
- [`php\gui\UXColorChooser`](api-docs/classes/php/gui/UXColorChooser.ru.md)- _Class UXColorChooser_
- [`php\gui\UXColorPicker`](api-docs/classes/php/gui/UXColorPicker.ru.md)- _Class UXColorPicker_
- [`php\gui\UXComboBox`](api-docs/classes/php/gui/UXComboBox.ru.md)- _Class UXComboBox_
- [`php\gui\UXComboBoxBase`](api-docs/classes/php/gui/UXComboBoxBase.ru.md)- _Class UXComboBoxBase_
- [`php\gui\UXContextMenu`](api-docs/classes/php/gui/UXContextMenu.ru.md)- _Class UXContextMenu_
- [`php\gui\UXControl`](api-docs/classes/php/gui/UXControl.ru.md)- _Class UXControl_
- [`php\gui\UXCustomNode`](api-docs/classes/php/gui/UXCustomNode.ru.md)- _Class UXCustomNode_
- [`php\gui\UXData`](api-docs/classes/php/gui/UXData.ru.md)- _Class UXData_
- [`php\gui\UXDatePicker`](api-docs/classes/php/gui/UXDatePicker.ru.md)- _Class UXDatePicker_
- [`php\gui\UXDesktop`](api-docs/classes/php/gui/UXDesktop.ru.md)- _Class UXDesktop_
- [`php\gui\UXDialog`](api-docs/classes/php/gui/UXDialog.ru.md)- _Class UXDialog_
- [`php\gui\UXDirectoryChooser`](api-docs/classes/php/gui/UXDirectoryChooser.ru.md)- _Class UXDirectoryChooser_
- [`php\gui\UXDragboard`](api-docs/classes/php/gui/UXDragboard.ru.md)- _Class UXDragboard_
- [`php\gui\UXDraggableTab`](api-docs/classes/php/gui/UXDraggableTab.ru.md)- _Class UXDraggableTab_
- [`php\gui\UXFileChooser`](api-docs/classes/php/gui/UXFileChooser.ru.md)- _Class UXFileChooser_
- [`php\gui\UXFlatButton`](api-docs/classes/php/gui/UXFlatButton.ru.md)- _Class UXFlatButton_
- [`php\gui\UXForm`](api-docs/classes/php/gui/UXForm.ru.md)
- [`php\gui\UXGeometry`](api-docs/classes/php/gui/UXGeometry.ru.md)- _Class UXGeometry_
- [`php\gui\UXGraphicsContext`](api-docs/classes/php/gui/UXGraphicsContext.ru.md)- _Class UXGraphicsContext_
- [`php\gui\UXGroup`](api-docs/classes/php/gui/UXGroup.ru.md)- _Class UXGroup_
- [`php\gui\UXHtmlEditor`](api-docs/classes/php/gui/UXHtmlEditor.ru.md)- _Class UXHtmlEditor_
- [`php\gui\UXHyperlink`](api-docs/classes/php/gui/UXHyperlink.ru.md)- _Class UXHyperlink_
- [`php\gui\UXImage`](api-docs/classes/php/gui/UXImage.ru.md)- _Class UXImage_
- [`php\gui\UXImageArea`](api-docs/classes/php/gui/UXImageArea.ru.md)- _Class UXImageArea_
- [`php\gui\UXImageView`](api-docs/classes/php/gui/UXImageView.ru.md)- _Class UXImageView_
- [`php\gui\UXLabel`](api-docs/classes/php/gui/UXLabel.ru.md)- _Class UXLabel_
- [`php\gui\UXLabeled`](api-docs/classes/php/gui/UXLabeled.ru.md)- _Class UXLabeled_
- [`php\gui\UXLabelEx`](api-docs/classes/php/gui/UXLabelEx.ru.md)- _Class UXLabelEx_
- [`php\gui\UXList`](api-docs/classes/php/gui/UXList.ru.md)- _Class UXList_
- [`php\gui\UXListCell`](api-docs/classes/php/gui/UXListCell.ru.md)- _Class UXTableCell_
- [`php\gui\UXListView`](api-docs/classes/php/gui/UXListView.ru.md)- _Class UXListView_
- [`php\gui\UXLoader`](api-docs/classes/php/gui/UXLoader.ru.md)- _Class UXLoader_
- [`php\gui\UXMaskTextField`](api-docs/classes/php/gui/UXMaskTextField.ru.md)
- [`php\gui\UXMedia`](api-docs/classes/php/gui/UXMedia.ru.md)- _Class UXMedia_
- [`php\gui\UXMediaPlayer`](api-docs/classes/php/gui/UXMediaPlayer.ru.md)- _Class UXMediaPlayer_
- [`php\gui\UXMediaView`](api-docs/classes/php/gui/UXMediaView.ru.md)- _Class UXMediaView_
- [`php\gui\UXMediaViewBox`](api-docs/classes/php/gui/UXMediaViewBox.ru.md)- _Class UXMediaViewBox_
- [`php\gui\UXMenu`](api-docs/classes/php/gui/UXMenu.ru.md)- _Class UXMenu_
- [`php\gui\UXMenuBar`](api-docs/classes/php/gui/UXMenuBar.ru.md)- _Class UXMenuBar_
- [`php\gui\UXMenuButton`](api-docs/classes/php/gui/UXMenuButton.ru.md)- _Class UXMenuButton_
- [`php\gui\UXMenuItem`](api-docs/classes/php/gui/UXMenuItem.ru.md)- _Class UXMenuItem_
- [`php\gui\UXNode`](api-docs/classes/php/gui/UXNode.ru.md)- _Class UXNode_
- [`php\gui\UXNumberSpinner`](api-docs/classes/php/gui/UXNumberSpinner.ru.md)
- [`php\gui\UXPagination`](api-docs/classes/php/gui/UXPagination.ru.md)- _Class UXPagination_
- [`php\gui\UXParent`](api-docs/classes/php/gui/UXParent.ru.md)- _Class UXParent_
- [`php\gui\UXPasswordField`](api-docs/classes/php/gui/UXPasswordField.ru.md)- _Class UXPasswordField_
- [`php\gui\UXPopupWindow`](api-docs/classes/php/gui/UXPopupWindow.ru.md)- _Class UXPopupWindow_
- [`php\gui\UXProgressBar`](api-docs/classes/php/gui/UXProgressBar.ru.md)- _Class UXProgressBar_
- [`php\gui\UXProgressIndicator`](api-docs/classes/php/gui/UXProgressIndicator.ru.md)- _Class ProgressIndicator_
- [`php\gui\UXRadioGroupPane`](api-docs/classes/php/gui/UXRadioGroupPane.ru.md)- _Class UXRadioGroupPane_
- [`php\gui\UXScene`](api-docs/classes/php/gui/UXScene.ru.md)- _Class UXScene_
- [`php\gui\UXScreen`](api-docs/classes/php/gui/UXScreen.ru.md)- _Class UXScreen_
- [`php\gui\UXSeparator`](api-docs/classes/php/gui/UXSeparator.ru.md)- _Class UXSeparator_
- [`php\gui\UXSlider`](api-docs/classes/php/gui/UXSlider.ru.md)- _Class UXSlider_
- [`php\gui\UXSpinner`](api-docs/classes/php/gui/UXSpinner.ru.md)- _Class UXSpinner_
- [`php\gui\UXSplitMenuButton`](api-docs/classes/php/gui/UXSplitMenuButton.ru.md)
- [`php\gui\UXSplitPane`](api-docs/classes/php/gui/UXSplitPane.ru.md)- _Class UXSplitPane_
- [`php\gui\UXTab`](api-docs/classes/php/gui/UXTab.ru.md)- _Class UXTab_
- [`php\gui\UXTableCell`](api-docs/classes/php/gui/UXTableCell.ru.md)- _Class UXTableCell_
- [`php\gui\UXTableColumn`](api-docs/classes/php/gui/UXTableColumn.ru.md)- _Class UXTableColumn_
- [`php\gui\UXTableView`](api-docs/classes/php/gui/UXTableView.ru.md)- _Class UXTableView_
- [`php\gui\UXTabPane`](api-docs/classes/php/gui/UXTabPane.ru.md)- _Class UXTabPane_
- [`php\gui\UXTextArea`](api-docs/classes/php/gui/UXTextArea.ru.md)- _Class UXTextArea_
- [`php\gui\UXTextField`](api-docs/classes/php/gui/UXTextField.ru.md)- _Class UXTextField_
- [`php\gui\UXTextInputControl`](api-docs/classes/php/gui/UXTextInputControl.ru.md)- _Class UXTextInputControl_
- [`php\gui\UXTitledPane`](api-docs/classes/php/gui/UXTitledPane.ru.md)- _Class UXTitledPane_
- [`php\gui\UXToggleButton`](api-docs/classes/php/gui/UXToggleButton.ru.md)- _Class UXToggleButton_
- [`php\gui\UXToggleGroup`](api-docs/classes/php/gui/UXToggleGroup.ru.md)- _Class UXToggleGroup_
- [`php\gui\UXTooltip`](api-docs/classes/php/gui/UXTooltip.ru.md)- _Class UXTooltip_
- [`php\gui\UXTrayNotification`](api-docs/classes/php/gui/UXTrayNotification.ru.md)- _Class UXTrayNotification_
- [`php\gui\UXTreeItem`](api-docs/classes/php/gui/UXTreeItem.ru.md)- _Class UXTreeItem_
- [`php\gui\UXTreeView`](api-docs/classes/php/gui/UXTreeView.ru.md)- _Class UXTreeView_
- [`php\gui\UXValue`](api-docs/classes/php/gui/UXValue.ru.md)- _Class UXValue_
- [`php\gui\UXWebEngine`](api-docs/classes/php/gui/UXWebEngine.ru.md)- _Class UXWebEngine_
- [`php\gui\UXWebHistory`](api-docs/classes/php/gui/UXWebHistory.ru.md)- _Class UXWebHistory_
- [`php\gui\UXWebView`](api-docs/classes/php/gui/UXWebView.ru.md)- _Class UXWebView_
- [`php\gui\UXWindow`](api-docs/classes/php/gui/UXWindow.ru.md)- _Class UXWindow_
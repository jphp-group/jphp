#### [English](README.md) / **Русский**

---

## jphp-gui-ext
> версия 1.0.0, создано с помощью JPPM v0.1.16

GUI based on JavaFX library.

### Установка
```
jppm add jphp-gui-ext@1.0.0
```

### АПИ
**Классы**
- [`php\gui\animation\UXAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimation.ru.md)- _Abstract animation class._
- [`php\gui\animation\UXAnimationTimer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXAnimationTimer.ru.md)- _Class UXAnimationTimer_
- [`php\gui\animation\UXFadeAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXFadeAnimation.ru.md)- _Class UXFadeAnimation_
- [`php\gui\animation\UXKeyFrame`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXKeyFrame.ru.md)- _Class UXKeyFrame_
- [`php\gui\animation\UXPathAnimation`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXPathAnimation.ru.md)- _Class UXPathAnimation_
- [`php\gui\animation\UXTimeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/animation/UXTimeline.ru.md)- _Class UXTimeline_
- [`php\gui\effect\UXBloomEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXBloomEffect.ru.md)- _Class UXBloomEffect_
- [`php\gui\effect\UXColorAdjustEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXColorAdjustEffect.ru.md)- _Class UXColorAdjustEffect_
- [`php\gui\effect\UXDropShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXDropShadowEffect.ru.md)- _Class UXDropShadowEffect_
- [`php\gui\effect\UXEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffect.ru.md)- _Class UXEffect_
- [`php\gui\effect\UXEffectPipeline`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXEffectPipeline.ru.md)- _Class UXEffectPipeline_
- [`php\gui\effect\UXGaussianBlurEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGaussianBlurEffect.ru.md)- _Class UXGaussianBlurEffect_
- [`php\gui\effect\UXGlowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXGlowEffect.ru.md)- _Class UXGlowEffect_
- [`php\gui\effect\UXInnerShadowEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXInnerShadowEffect.ru.md)- _Class UXInnerShadowEffect_
- [`php\gui\effect\UXLightingEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXLightingEffect.ru.md)- _Class UXLightingEffect_
- [`php\gui\effect\UXReflectionEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXReflectionEffect.ru.md)- _Class UXReflectionEffect_
- [`php\gui\effect\UXSepiaToneEffect`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/effect/UXSepiaToneEffect.ru.md)- _Class UXSepiaToneEffect_
- [`php\gui\event\UXContextMenuEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXContextMenuEvent.ru.md)- _Class UXContextMenuEvent_
- [`php\gui\event\UXDragEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXDragEvent.ru.md)- _Class UXDragEvent_
- [`php\gui\event\UXEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXEvent.ru.md)- _Class Event_
- [`php\gui\event\UXKeyboardManager`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyboardManager.ru.md)- _Менеджер для отлова событий клавиатуры._
- [`php\gui\event\UXKeyEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXKeyEvent.ru.md)- _Class UXKeyEvent_
- [`php\gui\event\UXMouseEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXMouseEvent.ru.md)- _Class UXMouseEvent_
- [`php\gui\event\UXScrollEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXScrollEvent.ru.md)- _Class UXScrollEvent_
- [`php\gui\event\UXWebErrorEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebErrorEvent.ru.md)- _Class UXWebErrorEvent_
- [`php\gui\event\UXWebEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWebEvent.ru.md)- _Class UXWebEvent_
- [`php\gui\event\UXWindowEvent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/event/UXWindowEvent.ru.md)- _Class UXWindowEvent_
- [`php\gui\JSException`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/JSException.ru.md)- _Class JSException_
- [`php\gui\layout\UXAnchorPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXAnchorPane.ru.md)- _Class UXAnchorPane_
- [`php\gui\layout\UXFlowPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFlowPane.ru.md)- _Class UXFlowPane_
- [`php\gui\layout\UXFragmentPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXFragmentPane.ru.md)
- [`php\gui\layout\UXHBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXHBox.ru.md)- _Class UXHBox_
- [`php\gui\layout\UXPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPane.ru.md)- _Class UXPane_
- [`php\gui\layout\UXPanel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXPanel.ru.md)- _Class UXPanel_
- [`php\gui\layout\UXRegion`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXRegion.ru.md)- _Class UXRegion_
- [`php\gui\layout\UXScrollPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXScrollPane.ru.md)- _Class UXScrollPane_
- [`php\gui\layout\UXStackPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXStackPane.ru.md)- _Class UXStackPane_
- [`php\gui\layout\UXTilePane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXTilePane.ru.md)
- [`php\gui\layout\UXVBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/layout/UXVBox.ru.md)- _Class UXVBox_
- [`php\gui\paint\UXColor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/paint/UXColor.ru.md)- _Class UXColor_
- [`php\gui\printing\UXPrinter`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinter.ru.md)
- [`php\gui\printing\UXPrinterJob`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/printing/UXPrinterJob.ru.md)
- [`php\gui\shape\UXCircle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXCircle.ru.md)- _Class UXCircle_
- [`php\gui\shape\UXEllipse`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXEllipse.ru.md)- _Class UXEllipse_
- [`php\gui\shape\UXPolygon`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXPolygon.ru.md)- _Class UXEllipse_
- [`php\gui\shape\UXRectangle`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXRectangle.ru.md)- _Class UXCircle_
- [`php\gui\shape\UXShape`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/shape/UXShape.ru.md)- _Class UXShape_
- [`php\gui\text\UXFont`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/text/UXFont.ru.md)- _Class UXFont_
- [`php\gui\UXAlert`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXAlert.ru.md)- _Класс для отображения всплывающих диалогов с кнопками._
- [`php\gui\UXApplication`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXApplication.ru.md)- _Class UXApplication_
- [`php\gui\UXButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButton.ru.md)- _Class UXButton_
- [`php\gui\UXButtonBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXButtonBase.ru.md)- _Class UXButtonBase_
- [`php\gui\UXCanvas`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCanvas.ru.md)- _Class UXCanvas_
- [`php\gui\UXCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCell.ru.md)- _Class UXCell_
- [`php\gui\UXCheckbox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCheckbox.ru.md)- _Class UXCheckbox_
- [`php\gui\UXChoiceBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXChoiceBox.ru.md)- _Class UXChoiceBox_
- [`php\gui\UXClipboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXClipboard.ru.md)- _Класс для работы с буфером обмена._
- [`php\gui\UXColorChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorChooser.ru.md)- _Class UXColorChooser_
- [`php\gui\UXColorPicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXColorPicker.ru.md)- _Class UXColorPicker_
- [`php\gui\UXComboBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBox.ru.md)- _Class UXComboBox_
- [`php\gui\UXComboBoxBase`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXComboBoxBase.ru.md)- _Class UXComboBoxBase_
- [`php\gui\UXContextMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXContextMenu.ru.md)- _Class UXContextMenu_
- [`php\gui\UXControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXControl.ru.md)- _Class UXControl_
- [`php\gui\UXCustomNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXCustomNode.ru.md)- _Class UXCustomNode_
- [`php\gui\UXData`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXData.ru.md)- _Class UXData_
- [`php\gui\UXDatePicker`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDatePicker.ru.md)- _Class UXDatePicker_
- [`php\gui\UXDesktop`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDesktop.ru.md)- _Class UXDesktop_
- [`php\gui\UXDialog`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDialog.ru.md)- _Class UXDialog_
- [`php\gui\UXDirectoryChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDirectoryChooser.ru.md)- _Class UXDirectoryChooser_
- [`php\gui\UXDragboard`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDragboard.ru.md)- _Class UXDragboard_
- [`php\gui\UXDraggableTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXDraggableTab.ru.md)- _Class UXDraggableTab_
- [`php\gui\UXFileChooser`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFileChooser.ru.md)- _Class UXFileChooser_
- [`php\gui\UXFlatButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXFlatButton.ru.md)- _Class UXFlatButton_
- [`php\gui\UXForm`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXForm.ru.md)
- [`php\gui\UXGeometry`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGeometry.ru.md)- _Class UXGeometry_
- [`php\gui\UXGraphicsContext`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGraphicsContext.ru.md)- _Class UXGraphicsContext_
- [`php\gui\UXGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXGroup.ru.md)- _Class UXGroup_
- [`php\gui\UXHtmlEditor`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHtmlEditor.ru.md)- _Class UXHtmlEditor_
- [`php\gui\UXHyperlink`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXHyperlink.ru.md)- _Class UXHyperlink_
- [`php\gui\UXImage`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImage.ru.md)- _Class UXImage_
- [`php\gui\UXImageArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageArea.ru.md)- _Class UXImageArea_
- [`php\gui\UXImageView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXImageView.ru.md)- _Class UXImageView_
- [`php\gui\UXLabel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabel.ru.md)- _Class UXLabel_
- [`php\gui\UXLabeled`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabeled.ru.md)- _Class UXLabeled_
- [`php\gui\UXLabelEx`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLabelEx.ru.md)- _Class UXLabelEx_
- [`php\gui\UXList`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXList.ru.md)- _Class UXList_
- [`php\gui\UXListCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListCell.ru.md)- _Class UXTableCell_
- [`php\gui\UXListView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXListView.ru.md)- _Class UXListView_
- [`php\gui\UXLoader`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXLoader.ru.md)- _Class UXLoader_
- [`php\gui\UXMaskTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMaskTextField.ru.md)
- [`php\gui\UXMedia`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMedia.ru.md)- _Class UXMedia_
- [`php\gui\UXMediaPlayer`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaPlayer.ru.md)- _Class UXMediaPlayer_
- [`php\gui\UXMediaView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaView.ru.md)- _Class UXMediaView_
- [`php\gui\UXMediaViewBox`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMediaViewBox.ru.md)- _Class UXMediaViewBox_
- [`php\gui\UXMenu`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenu.ru.md)- _Class UXMenu_
- [`php\gui\UXMenuBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuBar.ru.md)- _Class UXMenuBar_
- [`php\gui\UXMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuButton.ru.md)- _Class UXMenuButton_
- [`php\gui\UXMenuItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXMenuItem.ru.md)- _Class UXMenuItem_
- [`php\gui\UXNode`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNode.ru.md)- _Class UXNode_
- [`php\gui\UXNumberSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXNumberSpinner.ru.md)
- [`php\gui\UXPagination`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPagination.ru.md)- _Class UXPagination_
- [`php\gui\UXParent`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXParent.ru.md)- _Class UXParent_
- [`php\gui\UXPasswordField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPasswordField.ru.md)- _Class UXPasswordField_
- [`php\gui\UXPopupWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXPopupWindow.ru.md)- _Class UXPopupWindow_
- [`php\gui\UXProgressBar`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressBar.ru.md)- _Class UXProgressBar_
- [`php\gui\UXProgressIndicator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXProgressIndicator.ru.md)- _Class ProgressIndicator_
- [`php\gui\UXRadioGroupPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXRadioGroupPane.ru.md)- _Class UXRadioGroupPane_
- [`php\gui\UXScene`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScene.ru.md)- _Class UXScene_
- [`php\gui\UXScreen`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXScreen.ru.md)- _Class UXScreen_
- [`php\gui\UXSeparator`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSeparator.ru.md)- _Class UXSeparator_
- [`php\gui\UXSlider`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSlider.ru.md)- _Class UXSlider_
- [`php\gui\UXSpinner`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSpinner.ru.md)- _Class UXSpinner_
- [`php\gui\UXSplitMenuButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitMenuButton.ru.md)
- [`php\gui\UXSplitPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXSplitPane.ru.md)- _Class UXSplitPane_
- [`php\gui\UXTab`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTab.ru.md)- _Class UXTab_
- [`php\gui\UXTableCell`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableCell.ru.md)- _Class UXTableCell_
- [`php\gui\UXTableColumn`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableColumn.ru.md)- _Class UXTableColumn_
- [`php\gui\UXTableView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTableView.ru.md)- _Class UXTableView_
- [`php\gui\UXTabPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTabPane.ru.md)- _Class UXTabPane_
- [`php\gui\UXTextArea`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextArea.ru.md)- _Class UXTextArea_
- [`php\gui\UXTextField`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextField.ru.md)- _Class UXTextField_
- [`php\gui\UXTextInputControl`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTextInputControl.ru.md)- _Class UXTextInputControl_
- [`php\gui\UXTitledPane`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTitledPane.ru.md)- _Class UXTitledPane_
- [`php\gui\UXToggleButton`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleButton.ru.md)- _Class UXToggleButton_
- [`php\gui\UXToggleGroup`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXToggleGroup.ru.md)- _Class UXToggleGroup_
- [`php\gui\UXTooltip`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTooltip.ru.md)- _Class UXTooltip_
- [`php\gui\UXTrayNotification`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTrayNotification.ru.md)- _Class UXTrayNotification_
- [`php\gui\UXTreeItem`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeItem.ru.md)- _Class UXTreeItem_
- [`php\gui\UXTreeView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXTreeView.ru.md)- _Class UXTreeView_
- [`php\gui\UXValue`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXValue.ru.md)- _Class UXValue_
- [`php\gui\UXWebEngine`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebEngine.ru.md)- _Class UXWebEngine_
- [`php\gui\UXWebHistory`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebHistory.ru.md)- _Class UXWebHistory_
- [`php\gui\UXWebView`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebView.ru.md)- _Class UXWebView_
- [`php\gui\UXWindow`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWindow.ru.md)- _Class UXWindow_
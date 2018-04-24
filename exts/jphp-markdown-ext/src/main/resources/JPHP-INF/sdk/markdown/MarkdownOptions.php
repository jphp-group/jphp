<?php
namespace markdown;


class MarkdownOptions
{
    /**
     * @param string $break
     * @return MarkdownOptions
     */
    public function setRenderSoftBreak(string $break): MarkdownOptions
    {
    }

    /**
     * @param string $break
     * @return MarkdownOptions
     */
    public function setRenderHardBreak(string $break): MarkdownOptions
    {
    }


    /**
     * @param bool $value
     * @return MarkdownOptions
     */
    public function setRenderEscapeHtml(bool $value): MarkdownOptions
    {
    }

    /**
     * @param bool $value
     * @return MarkdownOptions
     */
    public function setRenderSuppressHtml(bool $value): MarkdownOptions
    {
    }

    /**
     * @param int $indent
     * @return MarkdownOptions
     */
    public function setRenderIndentSize(int $indent): MarkdownOptions
    {
    }

    /**
     * @return MarkdownOptions
     */
    public function addTableExtension(): MarkdownOptions
    {
    }

    /**
     * @return MarkdownOptions
     */
    public function addAnchorLinkExtension(): MarkdownOptions
    {

    }

    /**
     * Options are:
     *  linkPrefix => string
     *  linkPrefixAbsolute => string
     *  imagePrefix => string
     *  imagePrefixAbsolute => string
     *  imageFileExtension => string
     *  imageLinks => bool (def false)
     *  disableRendering => bool (def false)
     *  allowAnchors => bool (def false)
     *  allowInlines => bool (def false)
     *
     * @param array $options
     * @return MarkdownOptions
     */
    public function addWikiLinkExtension(array $options = []): MarkdownOptions
    {
    }

    /**
     * Options are:
     *  htmlOpen => any string
     *  htmlClose => any string.
     *
     * @param array $options
     * @return MarkdownOptions
     */
    public function addSubscriptExtension(array $options = ['htmlOpen' => null, 'htmlClose' => null]): MarkdownOptions
    {
    }

    /**
     * Options are:
     *  htmlOpen => any string
     *  htmlClose => any string.
     *
     * @param array $options
     * @return MarkdownOptions
     */
    public function addSuperscriptExtension(array $options = ['htmlOpen' => null, 'htmlClose' => null]): MarkdownOptions
    {
    }

    /**
     * Options are:
     *  imageType => IMAGE_ONLY|UNICODE_FALLBACK_TO_IMAGE|UNICODE_ONLY
     *  shortcutType => EMOJI_CHEAT_SHEET|GITHUB|ANY_EMOJI_CHEAT_SHEET_PREFERRED|ANY_GITHUB_PREFERRED
     *  imagePath => any string (default /img/).
     *  imageClass => any string (style css class).
     *  imageSize => integer (default 20)
     *  useImageUrls => bool (default false)
     *
     * @param array $options
     * @return MarkdownOptions
     */
    public function addEmojiExtension(array $options = []): MarkdownOptions
    {
    }
}
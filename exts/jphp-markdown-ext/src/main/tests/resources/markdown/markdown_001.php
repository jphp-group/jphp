--TEST--
markdown basic
--FILE--
<?php

use markdown\Markdown;
use markdown\MarkdownOptions;
use php\io\MemoryStream;

$options = new MarkdownOptions();
$options
    ->addTableExtension()
    ->addAnchorLinkExtension()
    ->addEmojiExtension()
    ->addSubscriptExtension()
    ->addSuperscriptExtension()
    ->addWikiLinkExtension();

$md = new Markdown($options);

echo $md->render("**foobar**");

?>
--EXPECT--
<p><strong>foobar</strong></p>
package org.develnext.jphp.gradle

class PhpGradleExtension {
    def srcDir = 'src/main/php';
    ArrayList<String> extensions = ['php', 'phtml', 'php5']

    def charset = "UTF-8"
}

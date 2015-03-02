package org.develnext.jphp.gradle

class PhpGradleExtension {
    def version = "0.6-SNAPSHOT"

    def srcDir = 'src/main/php';
    ArrayList<String> extensions = ['php', 'phtml', 'php5']

    def charset = "UTF-8"
}

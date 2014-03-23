package org.develnext.jphp.stubBuilder;

/**
 * @author VISTALL
 * @since 19:59/23.03.14
 */
public class StubBuilderUtil {
    public static final String NameAnnotation = "Lphp/runtime/annotation/Reflection$Name;";
    public static final String SignatureAnnotation = "Lphp/runtime/annotation/Reflection$Signature;";
    public static final String ValueMethod = "value";

    public static String[] slitToNameAndNamespace(String qName) {
        qName = qName.replace("/", "\\");
        int lastIndex = qName.lastIndexOf('\\');
        if (lastIndex != -1) {
            String namespaceName = qName.substring(0, lastIndex);
            String className = qName.substring(lastIndex + 1, qName.length());
            return new String[]{namespaceName, className};
        } else {
            return new String[]{null, qName};
        }
    }

    public static String genFileNameFromFullClassName(String name) {
        StringBuilder builder = new StringBuilder();
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    builder.append("_");
                }
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        builder.append(".php");
        return builder.toString();
    }
}

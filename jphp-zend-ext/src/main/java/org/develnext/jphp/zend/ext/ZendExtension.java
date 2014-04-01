package org.develnext.jphp.zend.ext;

import org.develnext.jphp.zend.ext.json.JsonExtension;
import org.develnext.jphp.zend.ext.standard.BCMathExtension;
import org.develnext.jphp.zend.ext.standard.CTypeExtension;
import org.develnext.jphp.zend.ext.standard.DateExtension;
import org.develnext.jphp.zend.ext.standard.StandardExtension;
import php.runtime.ext.support.Extension;

public class ZendExtension extends Extension {
    @Override
    public String[] getRequiredExtensions() {
        return new String[]{
                StandardExtension.class.getName(),
                BCMathExtension.class.getName(),
                CTypeExtension.class.getName(),
                DateExtension.class.getName(),
                JsonExtension.class.getName()
        };
    }

    @Override
    public String getName() {
        return "zend";
    }

    @Override
    public String getVersion() {
        return "~";
    }
}

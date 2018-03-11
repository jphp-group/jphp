package org.develnext.jphp.ext.mail;

import org.develnext.jphp.ext.mail.bind.InternetAddressMemoryOperation;
import org.develnext.jphp.ext.mail.classes.PEmailBackend;
import org.develnext.jphp.ext.mail.classes.PHtmlEmail;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class MailExtension extends Extension {
    public static final String NS = "php\\mail";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "mail" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerMemoryOperation(InternetAddressMemoryOperation.class);

        registerClass(scope, PEmailBackend.class);
        registerClass(scope, PHtmlEmail.class);
    }
}

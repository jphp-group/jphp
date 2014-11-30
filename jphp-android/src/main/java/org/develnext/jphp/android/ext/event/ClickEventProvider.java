package org.develnext.jphp.android.ext.event;

import android.view.View;
import org.develnext.jphp.android.ext.EventProvider;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;

public class ClickEventProvider extends EventProvider {
    @Override
    public String getCode() {
        return "click";
    }

    @Override
    public void bind(Environment env, View view, final Invoker invoker) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoker.callAny(v.getClass().cast(v));
            }
        });
    }

    @Override
    public void unbind(Environment env, View view) {
        view.setOnClickListener(null);
    }

    @Override
    public void trigger(Environment env, View view) {
        view.callOnClick();
    }
}

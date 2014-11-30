package org.develnext.jphp.android.ext.event;

import android.view.View;
import org.develnext.jphp.android.ext.EventProvider;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;

public class LongClickEventProvider extends EventProvider {
    @Override
    public String getCode() {
        return "longClick";
    }

    @Override
    public void bind(Environment env, final View view, final Invoker invoker) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return invoker.callAny(view.getClass().cast(v)).toBoolean();
            }
        });
    }

    @Override
    public void unbind(Environment env, View view) {
        view.setOnLongClickListener(null);
    }

    @Override
    public void trigger(Environment env, View view) {
        // nop.
    }
}

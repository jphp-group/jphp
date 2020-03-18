package php.runtime.env;

import php.runtime.common.LangMode;

public class CompileScopeOptions {
    public final boolean bytecodeCalls;
    public final LangMode langMode;
    public final boolean debugMode;

    protected CompileScopeOptions(boolean bytecodeCalls, LangMode langMode, boolean debugMode) {
        this.bytecodeCalls = bytecodeCalls;
        this.langMode = langMode;
        this.debugMode = debugMode;
    }

    public CompileScopeOptions.Builder duplicate() {
        return new Builder()
                .bytecodeCalls(bytecodeCalls)
                .langMode(langMode)
                .debugMode(debugMode);
    }

    public static final class Builder {
        private boolean bytecodeCalls;
        private LangMode langMode = LangMode.MODERN;
        private boolean debugMode = false;

        public Builder() {
        }

        public Builder bytecodeCalls(boolean bytecodeCalls) {
            this.bytecodeCalls = bytecodeCalls;
            return this;
        }

        public Builder langMode(LangMode mode) {
            this.langMode = mode;
            return this;
        }

        public Builder debugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public CompileScopeOptions build() {
            return new CompileScopeOptions(bytecodeCalls, langMode, debugMode);
        }
    }
}

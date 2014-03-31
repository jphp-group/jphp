package php.runtime.env.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

abstract public class ShellExecHandler {
    public final static ShellExecHandler DEFAULT = new ShellExecHandler() {
        @Override
        public String onExecute(String s) {
            try {
                Process p = Runtime.getRuntime().exec(s);
                p.waitFor();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine())!= null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    abstract public String onExecute(String s);
}

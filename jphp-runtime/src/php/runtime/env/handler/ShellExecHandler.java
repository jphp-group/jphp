package php.runtime.env.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@FunctionalInterface
public interface ShellExecHandler {
    ShellExecHandler DEFAULT = s -> {
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
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    };

    String onExecute(String s);
}

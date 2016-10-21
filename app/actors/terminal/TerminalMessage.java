package actors.terminal;

import java.io.IOException;
import java.io.Writer;

public abstract class TerminalMessage {
    public abstract void writeTo(Writer out) throws IOException;

    protected void writeInt(int value, Writer out) throws IOException {
        writeAlgorisms(value, out);
        out.write(';');
    }

    private void writeAlgorisms(int value, Writer out) throws IOException {
        while (value > 0) {
            int algorism = value % 64;

            writeAlgorism(algorism, out);

            value /= 64;
        }
    }

    private void writeAlgorism(int algorism, Writer out)
            throws IOException {
        if (algorism < 26)
            out.write(algorism + 'A');
        else if (algorism < 52)
            out.write(algorism - 26 + 'a');
        else if (algorism < 62)
            out.write(algorism - 52 + '0');
        else if (algorism == 62)
            out.write('+');
        else if (algorism == 63)
            out.write('/');
    }
}

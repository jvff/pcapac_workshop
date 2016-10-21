package actors.terminal;

import java.io.IOException;
import java.io.Writer;

public class UploadFileMessage extends TerminalMessage {
    private final String path;
    private final String contents;

    public UploadFileMessage(String path, String contents) {
        this.path = path;
        this.contents = contents;
    }

    @Override
    public void writeTo(Writer out) throws IOException {
        writeString(path, out);
        writeString(contents, out);
    }

    public void writeString(String string, Writer out) throws IOException {
        writeInt(string.length(), out);
        out.write(string);
    }
}

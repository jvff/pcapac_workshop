package actors.terminal;

import java.io.IOException;
import java.io.Writer;

public class ResizeTerminalMessage extends TerminalMessage {
    private final short columns;
    private final short rows;

    public ResizeTerminalMessage(short columns, short rows) {
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public void writeTo(Writer out) throws IOException {
        writeInt(columns, out);
        writeInt(rows, out);
    }
}

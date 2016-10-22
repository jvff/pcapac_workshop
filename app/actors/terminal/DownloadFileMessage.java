package actors.terminal;

public class DownloadFileMessage {
    private final String path;

    public DownloadFileMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

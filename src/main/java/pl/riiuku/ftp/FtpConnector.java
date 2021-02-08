package pl.riiuku.ftp;

public class FtpConnector {

    private String url;
    private String username;
    private String password;
    private String connectStatus;


    private FtpConnector() {}

    public static class Builder {

        private String url;
        private String username;
        private String password;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public FtpConnector build() {
            validateBuilder();
            FtpConnector ftpConnector = new FtpConnector();
            ftpConnector.url = url;
            ftpConnector.username = username;
            ftpConnector.password = password;
            return ftpConnector;
        }

        private void validateBuilder() {
            if(url == null) {
                throw new NullPointerException("You have to set URL");
            }
            if(username == null) {
                throw new NullPointerException("You have to set Username");
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void connect() {
        this.connectStatus = "CONNECTED";
    }

    public void disconnect() {
        this.connectStatus = "DISCONNECTED";
    }
}

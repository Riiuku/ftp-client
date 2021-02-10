package pl.riiuku.ftp;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;

public class FtpConnector {

    private String url;
    private int port = 0;
    private String username;
    private String password;
    private String connectStatus;
    private FTPClient ftpClient = new FTPClient();


    private FtpConnector() {
    }

    public static class Builder {

        private String url;
        private String username;
        private String password;
        private int port;

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

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public FtpConnector build() {
            validateBuilder();
            FtpConnector ftpConnector = new FtpConnector();
            ftpConnector.url = url;
            ftpConnector.port = port;
            ftpConnector.username = username;
            ftpConnector.password = password;
            return ftpConnector;
        }

        private void validateBuilder() {
            if (url == null) {
                throw new NullPointerException("You have to set URL");
            }
            if (username == null) {
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

    public int getPort() {
        return port;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public FTPClient connect() {
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {
            ftpClient.connect(url, port);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new IOException(ftpClient.getReplyString());
            }
            ftpClient.login(this.username, this.password);
            this.checkUserIsLoggedToFtpServer(ftpClient.getReplyCode());
            return ftpClient;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            this.ftpClient.disconnect();
            this.connectStatus = "DISCONNECTED";
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void checkUserIsLoggedToFtpServer(int ftpClientStatusCode) {
        if(ftpClientStatusCode == 530) {
            throw new RuntimeException("User set incorrect username or password");
        } else {
            this.connectStatus = "CONNECTED";
        }
    }
}

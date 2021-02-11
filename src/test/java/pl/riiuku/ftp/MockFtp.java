package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.BeforeAll;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

abstract public class MockFtp {



    public static FakeFtpServer createFtpServer() {
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));
        fakeFtpServer.setServerControlPort(0);
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new DirectoryEntry("/data/test"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);
        return fakeFtpServer;
    }

    public static FTPClient createFtpConnector(int port) {
        return new FtpConnector
                .Builder()
                .url("localhost")
                .port(port)
                .username("user")
                .password("password")
                .build()
                .connect();
    }


}

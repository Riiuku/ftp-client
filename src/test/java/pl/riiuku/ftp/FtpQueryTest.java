package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FtpQueryTest {

    private static FTPClient ftpClient;
    private static FakeFtpServer fakeFtpServer;

    @BeforeAll
    public static void beforeAll() {
        fakeFtpServer = MockFtp.createFtpServer();
        fakeFtpServer.start();
        ftpClient = MockFtp.createFtpConnector(fakeFtpServer.getServerControlPort());
    }


    @Test()
    public void testGetDirectoryFiles() {
        FtpQuery ftpQuery = new FtpQuery(ftpClient);
        List<FtpFile> ftpFiles = ftpQuery.getFiles("");
        assertEquals(ftpFiles.size(), 2);
        List<FtpFileType> types = ftpFiles.stream().map(FtpFile::getType).collect(Collectors.toList());
        List<FtpFileType> expectedTypes = new ArrayList<>();
        expectedTypes.add(FtpFileType.DIRECTORY);
        expectedTypes.add(FtpFileType.FILE);
        assertEquals(expectedTypes, types);
    }

    @AfterAll
    public static void afterAll() {
        fakeFtpServer.stop();
    }
}

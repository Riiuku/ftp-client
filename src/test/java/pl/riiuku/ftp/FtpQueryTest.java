package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test()
    public void uploadFile() throws URISyntaxException {
        FtpQuery ftpQuery = new FtpQuery(ftpClient);
        File file = new File(getClass().getClassLoader().getResource("test.txt").toURI());
        ftpQuery.uploadFile(file, "/" +file.getName());
        assertTrue(fakeFtpServer.getFileSystem().exists("/" + file.getName()));
    }

    @Test()
    public void downloadFile() {
        FtpQuery ftpQuery = new FtpQuery(ftpClient);
        ftpQuery.downloadFile("/data/foobar.txt", "foobar.txt");
        assertTrue(new File("foobar.txt").exists());
        new File("foobar.txt").delete();
    }

    @AfterAll
    public static void afterAll() {
        fakeFtpServer.stop();
    }
}

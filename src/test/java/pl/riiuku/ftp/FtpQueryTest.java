package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FtpQueryTest {

    private static FTPClient ftpClient;
    private static FakeFtpServer fakeFtpServer;

    @BeforeEach
    public void beforeAll() {
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

    @Test()
    public void deleteFile() {
        FtpQuery ftpQuery = new FtpQuery(ftpClient);
        ftpQuery.deleteFile("/data/foobar.txt");
        assertFalse(fakeFtpServer.getFileSystem().exists("/data/foobar.txt"));

        assertThrows(RuntimeException.class, () ->  ftpQuery.deleteFile("/data/foobar.txt"));
    }

    @AfterEach
    public void afterAll() {
        fakeFtpServer.stop();
    }
}

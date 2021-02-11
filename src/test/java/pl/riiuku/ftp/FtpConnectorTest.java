package pl.riiuku.ftp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FtpConnectorTest  {


    private static FakeFtpServer fakeFtpServer;

    @BeforeAll
    public static void beforeAll() throws InterruptedException {
        fakeFtpServer = MockFtp.createFtpServer();
        fakeFtpServer.start();
    }


    @Test()
    public void testFtpConnectorBuilder() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("ftp://test.com")
                .port(8080)
                .username("user")
                .password("password")
                .build();

        assertEquals("ftp://test.com", ftp.getUrl());
        assertEquals("user", ftp.getUsername());
        assertEquals(8080, ftp.getPort());


        assertThrows(NullPointerException.class, () -> new FtpConnector.Builder().url(null).build(), "You have to set URL");
        assertThrows(NullPointerException.class, () -> new FtpConnector.Builder().url("ftp://test.com").username(null).build(), "You have to set Username");
    }

    @Test()
    public void testFtpConnectorCorrectConnectAndDisconnect() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("localhost")
                .port(fakeFtpServer.getServerControlPort())
                .username("user")
                .password("password")
                .build();

        ftp.connect();
        assertEquals(ftp.getConnectStatus(), "CONNECTED");
        ftp.disconnect();
        assertEquals(ftp.getConnectStatus(), "DISCONNECTED");
    }

    @Test()
    public void testFtpConnectorIncorrectUrlAndPort() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("random")
                .port(0)
                .username("user")
                .password("password")
                .build();
        assertThrows(RuntimeException.class, ftp::connect);
    }

    @Test()
    public void testFtpConnectorWithIncorrectUsernameAndPassword() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("localhost")
                .port(fakeFtpServer.getServerControlPort())
                .username("user1")
                .password("passw0rd")
                .build();
        assertThrows(RuntimeException.class, ftp::connect, "User set incorrect username or password");
    }


    @AfterAll
    public static void afterAll() {
        fakeFtpServer.stop();
    }

}

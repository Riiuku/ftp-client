package pl.riiuku.ftp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FtpConnectorTest {

    @Test()
    public void testFtpConnectorBuilder() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("ftp://test.com")
                .username("user")
                .password("password")
                .build();

        assertEquals("ftp://test.com", ftp.getUrl());
        assertEquals("user", ftp.getUsername());


        assertThrows(NullPointerException.class, () -> new FtpConnector.Builder().url(null).build(), "You have to set URL");
        assertThrows(NullPointerException.class, () -> new FtpConnector.Builder().url("ftp://test.com").username(null).build(), "You have to set Username");
    }

    @Test()
    public void testFtpConnectorCorrectConnectAndDisconnect() {
        FtpConnector ftp = new FtpConnector
                .Builder()
                .url("ftp://test.com")
                .username("user")
                .password("password")
                .build();

        ftp.connect();
        assertEquals(ftp.getConnectStatus(), "CONNECTED");
        ftp.disconnect();
        assertEquals(ftp.getConnectStatus(), "DISCONNECTED");
    }


}

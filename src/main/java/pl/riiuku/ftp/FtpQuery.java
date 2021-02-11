package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FtpQuery {

    private final FTPClient ftpClient;

    public FtpQuery(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public List<FtpFile> getFiles(String path) {
        try {
            FTPFile[] files = ftpClient.listFiles(path);
            return Arrays.stream(files)
                    .map(FtpFile::convert)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}

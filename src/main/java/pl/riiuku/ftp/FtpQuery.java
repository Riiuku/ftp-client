package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
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

    public void uploadFile(File file, String path) {
        try {
            ftpClient.storeFile(path, new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void downloadFile(String path, String out) {
        try {
            FileOutputStream stream = new FileOutputStream(out);
            ftpClient.retrieveFile(path, stream);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    public void deleteFile(String path) {
        try {
            boolean deleted = ftpClient.deleteFile(path);
            if(!deleted)
                throw new RuntimeException("File not deleted");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

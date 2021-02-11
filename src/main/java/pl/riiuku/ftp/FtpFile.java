package pl.riiuku.ftp;

import org.apache.commons.net.ftp.FTPFile;

import java.time.LocalDateTime;

public class FtpFile {
    private String fileName;
    private long sizeInBytes;
    private LocalDateTime createDate;
    private FtpFileType type;

    public FtpFile(String fileName, long sizeInBytes, LocalDateTime createDate, FtpFileType type) {
        this.fileName = fileName;
        this.sizeInBytes = sizeInBytes;
        this.createDate = createDate;
        this.type = type;
    }

    public static FtpFile convert(FTPFile source) {
        return new FtpFile(
                source.getName(),
                source.getSize(),
                LocalDateTime.ofInstant(source.getTimestamp().toInstant(), source.getTimestamp().getTimeZone().toZoneId()),
                source.isDirectory() ? FtpFileType.DIRECTORY : FtpFileType.FILE);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public FtpFileType getType() {
        return type;
    }

    public void setType(FtpFileType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FtpFile{" +
                "fileName='" + fileName + '\'' +
                ", sizeInBytes=" + sizeInBytes +
                ", createDate=" + createDate +
                ", type=" + type +
                '}';
    }
}

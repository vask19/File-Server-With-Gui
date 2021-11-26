package data;

import java.io.Serializable;

public class Response implements Serializable {
    private int statusCode;
    private int fileId;

    private String fileName;
    private byte[] fileContent;

    public int getStatusCode() {
        return statusCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", fileId=" + fileId +
                ", fileContent='" + fileContent + '\'' +
                '}';
    }
}

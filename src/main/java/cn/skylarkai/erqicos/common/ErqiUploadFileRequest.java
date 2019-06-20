package cn.skylarkai.erqicos.common;

import java.util.Arrays;

/**
 * UploadFileRequest
 *
 * @author chuhl
 */
public class ErqiUploadFileRequest {

    private String bucketName;

    private String cosPath;

    private String localPath;

    private byte[] contentBufer;

    public ErqiUploadFileRequest (String bucketName ,String cosPath,String localPath){
        this.bucketName = bucketName;
        this.cosPath = cosPath;
        this.localPath = localPath;
    }

    public ErqiUploadFileRequest (String bucketName ,String cosPath,byte[] contentBufer){
        this.bucketName = bucketName;
        this.cosPath = cosPath;
        this.localPath = null;
        this.contentBufer = contentBufer;
    }


    public String getBucketName() {
        return bucketName;
    }

    public String getCosPath() {
        return cosPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public byte[] getContentBufer() {
        return contentBufer;
    }
    @Override
    public String toString() {
        return "ErqiUploadFileRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                ", localPath='" + localPath + '\'' +
                ", contentBufer=" + Arrays.toString( contentBufer ) +
                '}';
    }
}

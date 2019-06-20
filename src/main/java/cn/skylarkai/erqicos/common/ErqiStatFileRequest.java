package cn.skylarkai.erqicos.common;
/**
 * StatFileRequest
 *
 * @author chuhl
 */
public class ErqiStatFileRequest {
    private String bucketName;

    private String cosPath;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCosPath() {
        return cosPath;
    }

    public void setCosPath(String cosPath) {
        this.cosPath = cosPath;
    }


    public ErqiStatFileRequest(String bucketName, String cosPath) {
        this.bucketName = bucketName;
        this.cosPath = cosPath;
    }

    @Override
    public String toString() {
        return "ErqiStatFileRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                '}';
    }
}

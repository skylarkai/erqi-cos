package cn.skylarkai.erqicos.common;
/**
 * ListFolderRequest
 *
 * @author chuhl
 * @date 2019/4/24
 */
public class ErqiListFolderRequest {
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

    @Override
    public String toString() {
        return "ErqiListFolderRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                '}';
    }

    public ErqiListFolderRequest(String bucketName, String cosPath) {
        this.bucketName = bucketName;
        this.cosPath = cosPath;
    }
}


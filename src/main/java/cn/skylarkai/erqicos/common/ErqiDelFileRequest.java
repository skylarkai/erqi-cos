package cn.skylarkai.erqicos.common;
/**
 * ErqiDelFileRequest
 *
 * @author chuhl
 * @date 2019/4/29
 */
public class ErqiDelFileRequest {
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


    public ErqiDelFileRequest(String bucketName, String cosPath) {
        this.bucketName = bucketName;
        this.cosPath = cosPath;
    }

    @Override
    public String toString() {
        return "ErqiDelFileRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                '}';
    }

}

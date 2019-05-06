package cn.skylarkai.erqicos.common;
/**
 * MoveFileRequest
 *
 * @author chuhl
 * @date 2019/4/24
 */
public class ErqiMoveFileRequest {
    private String bucketName;
    private String cosPath;
    private String dstCosPath;

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

    public String getDstCosPath() {
        return dstCosPath;
    }

    public void setDstCosPath(String dstCosPath) {
        this.dstCosPath = dstCosPath;
    }


    public ErqiMoveFileRequest(String bucketName, String cosPath, String dstCosPath) {
        this.bucketName = bucketName;
        this.cosPath = cosPath;
        this.dstCosPath = dstCosPath;
    }

    @Override
    public String toString() {
        return "ErqiMoveFileRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                ", dstCosPath='" + dstCosPath + '\'' +
                '}';
    }
}

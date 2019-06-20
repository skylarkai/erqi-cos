package cn.skylarkai.erqicos.common;
/**
 * ErqiClientConfig
 *
 * @author chuhl
 */
public class ErqiClientConfig {
    private long appid;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endpoint;
    private int cosType;

    public ErqiClientConfig (String cos_type) {
        switch (cos_type){
            case "TENCENT":
                this.cosType = 1;
                break;
            case "TENCENT_V4":
                this.cosType = 3;
                break;
            case "ALIYUN":
                this.cosType = 2;
                break;
            case "OTHER":
                this.cosType = 0;
                break;
            default:
                this.cosType = 0;
        }

    }

    public long getAppid() {
        return appid;
    }

    public int getCosType(){ return cosType; }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public static enum COS_TYPE {
        ALIYUN,
        TENCENT,
        TENCENT_V4,
        OTHER
    }
}

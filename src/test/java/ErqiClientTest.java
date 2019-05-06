import cn.skylarkai.erqicos.ErqiClient;
import cn.skylarkai.erqicos.common.ErqiClientConfig;
import cn.skylarkai.erqicos.common.ErqiDelFileRequest;
import cn.skylarkai.erqicos.common.ErqiUploadFileRequest;

/**
 * ErqiClientTest
 *
 * @author chuhl
 * @date 2019/5/6
 */
public class ErqiClientTest {

    public static void main(String[] args){
        ErqiClientConfig erqiClientConfig = new ErqiClientConfig( ErqiClientConfig.COS_TYPE.OTHER);
        //tencentV4
//        erqiClientConfig.setAppid( 1255605477 );
//        erqiClientConfig.setSecretKey( "60hEXdWBdsujLkwve7KIzQbWBajbEbiZ" );
//        erqiClientConfig.setAccessKey( "AKIDsZ1D8nCMGuOgjGIyABzb1FI8k9MMrj6G" );
//        erqiClientConfig.setRegion( "bj" );
//        String bucketName = "dev-cos";
        //minio
        erqiClientConfig.setAccessKey( "XUP7XS0B7N3NA17ST3XY" );
        erqiClientConfig.setEndpoint( "http://192.168.238.128:9009" );
        erqiClientConfig.setSecretKey( "NGO6U0dndvVyYFHdPu0vMoUlYlFGgDKFq62fAx7Q" );
//        erqiClientConfig.setRegion( "bj" );
        String bucketName = "test4";
        //aliyun
//        erqiClientConfig.setSecretKey( "qVGAgYsgYOQRxYnpye1pxMzJnQL0xz" );
//        erqiClientConfig.setAccessKey( "LTAIS76wo0iWNjkw" );
//        erqiClientConfig.setEndpoint( "http://oss-cn-zhangjiakou.aliyuncs.com" );
//        String bucketName = "chuhl";
        ErqiClient erqiClient=new ErqiClient( erqiClientConfig );
        if(!erqiClient.checkBucket( bucketName )){
            System.out.print( erqiClient.makeBucket( bucketName ) );
        }

        ErqiUploadFileRequest uploadFileRequest = new ErqiUploadFileRequest( bucketName,"test1.html","D:\\wcmtxt\\test.html" );

        String ret = erqiClient.uploadFile( uploadFileRequest );
        System.out.print( ret );
////        cn.skylarkai.openapi.common.util.ListFolderRequest listFolderRequest = new cn.skylarkai.openapi.common.util.ListFolderRequest( bucketName,"data/" );
////        String ret = erqiClient.listFolder( listFolderRequest );
        ErqiDelFileRequest erqiDelFileRequest = new ErqiDelFileRequest( bucketName,"test1.html" );
        String rets = erqiClient.delFile( erqiDelFileRequest );
        System.out.print( rets );
        erqiClient.shutdown();
    }
}

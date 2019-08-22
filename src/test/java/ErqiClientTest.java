import cn.skylarkai.erqicos.ErqiClient;
import cn.skylarkai.erqicos.common.ErqiClientConfig;
import cn.skylarkai.erqicos.common.ErqiDelFileRequest;
import cn.skylarkai.erqicos.common.ErqiGetFileInputStreamRequest;
import cn.skylarkai.erqicos.common.ErqiListFolderRequest;
import cn.skylarkai.erqicos.common.ErqiUploadFileRequest;

import java.io.InputStream;

/**
 * ErqiClientTest
 *
 * @author chuhl
 * @date 2019/5/6
 */
public class ErqiClientTest {

    public static void main(String[] args){
        ErqiClientConfig erqiClientConfig = new ErqiClientConfig( "ALIYUN");
        //tencentV4
//        erqiClientConfig.setAppid( 1255605477 );
//        erqiClientConfig.setSecretKey( "60hEXdWBdsujLkwve7KIzQbWBajbEbiZ" );
//        erqiClientConfig.setAccessKey( "AKIDsZ1D8nCMGuOgjGIyABzb1FI8k9MMrj6G" );
//        erqiClientConfig.setRegion( "bj" );
//        String bucketName = "dev-cos";
        //minio
//        erqiClientConfig.setAccessKey( "4OD84A4DJX995LPBJY9C" );
//        erqiClientConfig.setEndpoint( "http://localhost:9000" );
//        erqiClientConfig.setSecretKey( "AdwDDU5LMA7zHHrY5C+5ACCpabk7bdTxKkXrCpCs" );
//        erqiClientConfig.setRegion( "bj" );
//        String bucketName = "test4";
        //aliyun
        erqiClientConfig.setSecretKey( "qVGAgYsgYOQRxYnpye1pxMzJnQL0xz" );
        erqiClientConfig.setAccessKey( "LTAIS76wo0iWNjkw" );
        erqiClientConfig.setEndpoint( "http://oss-cn-zhangjiakou.aliyuncs.com" );
        String bucketName = "chuhl";
        ErqiClient erqiClient=new ErqiClient( erqiClientConfig );
        if(!erqiClient.checkBucket( bucketName )){
            System.out.print( erqiClient.makeBucket( bucketName ) );
        }

//        ErqiUploadFileRequest uploadFileRequest = new ErqiUploadFileRequest( bucketName,"test/test1.html","D:\\wcmtxt\\test.html" );
//
//        String ret = erqiClient.uploadFile( uploadFileRequest );
//        System.out.print( ret );
//
//        ErqiGetFileInputStreamRequest inputStreamRequest = new ErqiGetFileInputStreamRequest( bucketName,"/test1.html" );
//        InputStream ret1 = erqiClient.getFileInputStream( inputStreamRequest );


        ErqiListFolderRequest listFolderRequest = new ErqiListFolderRequest( bucketName,"test/" );
        String ret1 = erqiClient.listFolder( listFolderRequest );
//        ErqiDelFileRequest erqiDelFileRequest = new ErqiDelFileRequest( bucketName,"test1.html" );
//        String rets = erqiClient.delFile( erqiDelFileRequest );
        System.out.print( ret1 );
        erqiClient.shutdown();
    }
}

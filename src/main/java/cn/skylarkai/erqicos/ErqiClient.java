package cn.skylarkai.erqicos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.skylarkai.erqicos.common.ErqiClientConfig;
import cn.skylarkai.erqicos.common.ErqiDelFileRequest;
import cn.skylarkai.erqicos.common.ErqiGetFileInputStreamRequest;
import cn.skylarkai.erqicos.common.ErqiListFolderRequest;
import cn.skylarkai.erqicos.common.ErqiMoveFileRequest;
import cn.skylarkai.erqicos.common.ErqiStatFileRequest;
import cn.skylarkai.erqicos.common.ErqiUploadFileRequest;
import cn.skylarkai.erqicos.common.ReturnMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.meta.OverWrite;
import com.qcloud.cos.sign.Credentials;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Item;

//
//import com.qcloud.cos.auth.BasicCOSCredentials;
//import com.qcloud.cos.auth.COSCredentials;
//import com.qcloud.cos.region.Region;

/**
 * ErqiClient
 *
 * @author chuhl
 * @date 2019/4/24
 */
public class ErqiClient {
    private ErqiClientConfig erqiClientConfig;

    private COSClient cosClient;

    private OSSClient ossClient;

    private MinioClient minioClient;

    public ErqiClient (ErqiClientConfig erqiClientConfig){
        this.erqiClientConfig = erqiClientConfig;
        switch (erqiClientConfig.getCosType()){
            case 1:
//                COSCredentials cred = new BasicCOSCredentials(erqiClientConfig.getAccessKey(), erqiClientConfig.getSecretKey());
//                ClientConfig clientConfig = new ClientConfig();
//                clientConfig.setRegion(new Region( erqiClientConfig.getRegion() ));
//                this.cosClient = new COSClient(cred ,clientConfig );
                break;
            case 2:
                this.ossClient = new OSSClient( erqiClientConfig.getEndpoint(),erqiClientConfig.getAccessKey(),erqiClientConfig.getSecretKey());
                break;
            case 3:
                Credentials credentials = new Credentials(erqiClientConfig.getAppid(),erqiClientConfig.getAccessKey(),erqiClientConfig.getSecretKey());
                ClientConfig clientConfig = new ClientConfig();
                clientConfig.setRegion( erqiClientConfig.getRegion() );
                this.cosClient = new COSClient( clientConfig,credentials );
                break;
            default:
                try {
                    this.minioClient = new MinioClient( erqiClientConfig.getEndpoint(),erqiClientConfig.getAccessKey(),erqiClientConfig.getSecretKey(),erqiClientConfig.getRegion() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    public boolean checkBucket(String bucketName){
        switch (erqiClientConfig.getCosType()){
            case 1:
//                return  cosClient.doesBucketExist( bucketName );
                return false;
            case 2:
                return  ossClient.doesBucketExist( bucketName );
            case 3:
                return true;
            default:
                boolean ret = false;
                try {
                    ret = minioClient.bucketExists( bucketName );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
        }
    }

    public boolean makeBucket(String bucketName){
        switch (erqiClientConfig.getCosType()){
            case 1:
//              Bucket bucket = cosClient.createBucket(bucketName);
//                if(bucket != null){
//                    return true;
//                }
//                return  false;
                return true;
            case 2:
                Bucket bucket= ossClient.createBucket( bucketName );
                if(bucket != null){
                    return true;
                }
                return  false;
            case 3:
                return true;
            default:
                try {
                    minioClient.makeBucket( bucketName );
                    StringBuilder builder = new StringBuilder();
                    builder.append("{\n");
                    builder.append("    \"Statement\": [\n");
                    builder.append("        {\n");
                    builder.append("            \"Action\": [\n");
                    builder.append("                \"s3:GetBucketLocation\",\n");
                    builder.append("                \"s3:ListBucket\",\n");
                    builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                    builder.append("            ],\n");
                    builder.append("            \"Effect\": \"Allow\",\n");
                    builder.append("            \"Principal\": \"*\",\n");
                    builder.append("            \"Resource\": \"arn:aws:s3:::"+bucketName+"\"\n");
                    builder.append("        },\n");
                    builder.append("        {\n");
                    builder.append("            \"Action\": [\n");
                    builder.append("                \"s3:AbortMultipartUpload\",\n");
                    builder.append("                \"s3:DeleteObject\",\n");
                    builder.append("                \"s3:ListMultipartUploadParts\",\n");
                    builder.append("                \"s3:PutObject\",\n");
                    builder.append("                \"s3:GetObject\"\n");
                    builder.append("            ],\n");
                    builder.append("            \"Effect\": \"Allow\",\n");
                    builder.append("            \"Principal\": \"*\",\n");
                    builder.append("            \"Resource\": \"arn:aws:s3:::"+bucketName+"/*\"\n");
                    builder.append("        }\n");
                    builder.append("    ],\n");
                    builder.append("    \"Version\": \"2012-10-17\"\n");
                    builder.append("}\n");
                    minioClient.setBucketPolicy( bucketName,builder.toString() );
                    return true;
                } catch (Exception e) {
                    return false;
                }
        }
    }

    public String uploadFile(ErqiUploadFileRequest erqiUploadFileRequest){
        ReturnMap returnMap = new ReturnMap();
        switch (erqiClientConfig.getCosType()){
            case 1:
                break;
            case 2:
                PutObjectResult result;
                if(erqiUploadFileRequest.getLocalPath() == null){
                    ByteArrayOutputStream bas = new ByteArrayOutputStream(  );
                    try {
                        bas.write( erqiUploadFileRequest.getContentBufer() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream =new ByteArrayInputStream(bas.toByteArray());
                    result = ossClient.putObject(  erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath(),inputStream);
                }else {
                    result = ossClient.putObject( erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath(),new File(erqiUploadFileRequest.getLocalPath()) );
                }
                if(result.getETag()!= null){
                    OSSObject ossObject= ossClient.getObject( erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath() );
                    ResponseMessage responseMessage = ossObject.getResponse();
                    if(responseMessage.isSuccessful()){
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS" );
                        Map<String,Object> mapdata = new HashMap<>(  );
                        mapdata.put( "source_url",responseMessage.getUri() );
                        returnMap.setData( JSON.toJSONString( mapdata ) );
                    }else {
                        returnMap.setFlg( 1 );
                        returnMap.setMessage( "FAILURE" );
                    }
                }else{
                    returnMap.setFlg( 1 );
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            case 3:
                com.qcloud.cos.request.UploadFileRequest request;
                if(erqiUploadFileRequest.getLocalPath() == null){
                    request = new com.qcloud.cos.request.UploadFileRequest( erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath(),erqiUploadFileRequest.getContentBufer() );
                }else {
                    request = new com.qcloud.cos.request.UploadFileRequest( erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath(),erqiUploadFileRequest.getLocalPath() );

                }
                String uploadFileRet = cosClient.uploadFile( request );
                JSONObject jsonObject = JSON.parseObject( uploadFileRet );
                if(jsonObject.get( "code" ).toString().equals( "0" )){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                    JSONObject data = JSONObject.parseObject (jsonObject.getString( "data" ));
                    Map<String,Object> mapdata = new HashMap<>(  );
                    mapdata.put( "source_url",data.getString( "source_url" ) );
                    returnMap.setData( JSON.toJSONString( mapdata ) );
                }else {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                }
                break;
            default:
                try {
                    if(erqiUploadFileRequest.getLocalPath() == null) {
                        ByteArrayOutputStream bas = new ByteArrayOutputStream();
                        try {
                            bas.write( erqiUploadFileRequest.getContentBufer() );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        InputStream inputStream = new ByteArrayInputStream( bas.toByteArray() );
                        minioClient.putObject( erqiUploadFileRequest.getBucketName(), erqiUploadFileRequest.getCosPath(), inputStream, "application/octet-stream" );
                    }else {
                        minioClient.putObject( erqiUploadFileRequest.getBucketName(), erqiUploadFileRequest.getCosPath(), erqiUploadFileRequest.getLocalPath(), "application/octet-stream" );

                    }
                    String url = minioClient.getObjectUrl( erqiUploadFileRequest.getBucketName(),erqiUploadFileRequest.getCosPath() );
                    if(url != null){
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS" );
                        Map<String,Object> mapdata = new HashMap<>(  );
                        mapdata.put( "source_url",url);
                        returnMap.setData( JSON.toJSONString( mapdata ) );
                    } else {
                        returnMap.setFlg( 1 );
                        returnMap.setMessage( "FAILURE" );
                    }
                } catch (Exception e) {
                    returnMap.setFlg( 1 );
                    returnMap.setMessage( "FAILURE" );
                }

        }
        return  returnMap.ReturnMap();
    }

    public String delFile(ErqiDelFileRequest erqiDelFileRequest){
        ReturnMap returnMap = new ReturnMap();
        switch (erqiClientConfig.getCosType()) {
            case 1:
                break;
            case 2:
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest( erqiDelFileRequest.getBucketName() );
                deleteObjectsRequest.setKey( erqiDelFileRequest.getCosPath() );
                DeleteObjectsResult deleteObjectsResult= ossClient.deleteObjects( deleteObjectsRequest );
                ResponseMessage responseMessage = deleteObjectsResult.getResponse();
                if(responseMessage.isSuccessful()){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( "SUCCESS" );
                }else{
                    returnMap.setFlg( 1 );
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            case 3:
                com.qcloud.cos.request.DelFileRequest cosDelFileRequest = new com.qcloud.cos.request.DelFileRequest( erqiDelFileRequest.getBucketName(),erqiDelFileRequest.getCosPath() );
                String retStr = cosClient.delFile( cosDelFileRequest );
                JSONObject jsonObject = JSON.parseObject( retStr );
                if(jsonObject.get( "code" ).toString().equals( "0" )){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                }else {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                }
                break;
            default:
                try {
                    minioClient.removeObject( erqiDelFileRequest.getBucketName(), erqiDelFileRequest.getCosPath() );
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( "SUCCESS" );
                }catch (Exception ex){
                    returnMap.setFlg( 1 );
                    returnMap.setMessage( "FAILURE" );
                }
        }
        return  returnMap.ReturnMap();
    }

    public String moveFile(ErqiMoveFileRequest erqiMoveFileRequest){
        ReturnMap returnMap = new ReturnMap();
        switch (erqiClientConfig.getCosType()){
            case 1:
                break;
            case 2:
                CopyObjectRequest copyObjectRequest = new CopyObjectRequest( erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getCosPath(),erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getDstCosPath() );
                CopyObjectResult copyObjectResult= ossClient.copyObject( copyObjectRequest );
                ResponseMessage responseMessageq = copyObjectResult.getResponse();
                if(responseMessageq.isSuccessful()){
                    DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest( erqiMoveFileRequest.getBucketName() );
                    deleteObjectsRequest.setKey( erqiMoveFileRequest.getCosPath() );
                    DeleteObjectsResult deleteObjectsResult= ossClient.deleteObjects( deleteObjectsRequest );
                    ResponseMessage responseMessageM = deleteObjectsResult.getResponse();
                    if(responseMessageM.isSuccessful()){
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS" );
                    }else{
                        returnMap.setFlg( 1 );
                        returnMap.setMessage( "FAILURE" );
                    }
                }else{
                    returnMap.setFlg( 1 );
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            case 3:
                com.qcloud.cos.request.MoveFileRequest cosMoveFileRequest = new com.qcloud.cos.request.MoveFileRequest( erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getCosPath(),erqiMoveFileRequest.getDstCosPath() );
                cosMoveFileRequest.setOverWrite( OverWrite.OVER_WRITE);
                String moveRetStr= cosClient.moveFile( cosMoveFileRequest );
                JSONObject jsonObject = JSON.parseObject( moveRetStr );
                if(jsonObject.get( "code" ).toString().equals( "0" )){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                }else {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( jsonObject.getString( "message" ) );
                }
                break;
            default:
                try {
                    minioClient.copyObject( erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getCosPath(),erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getDstCosPath() );
                    ErqiDelFileRequest erqiDelFileRequest = new ErqiDelFileRequest( erqiMoveFileRequest.getBucketName(),erqiMoveFileRequest.getCosPath() );
                    String moveDelRetStr = this.delFile(erqiDelFileRequest  );
                    JSONObject jsonObjectRet = JSON.parseObject( moveDelRetStr );
                    if(jsonObjectRet.get( "code" ).toString().equals( "0" )){
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS");
                    }else {
                        returnMap.setFlg( 1);
                        returnMap.setMessage( "FAILURE" );
                    }
                }catch (Exception ex){
                    returnMap.setFlg( 1);
                    returnMap.setMessage("FAILURE");
                }
        }

        return  returnMap.ReturnMap();
    }

    public String statFile(ErqiStatFileRequest erqiStatFileRequest){
        ReturnMap returnMap = new ReturnMap();
        switch (erqiClientConfig.getCosType()){
            case 1:
                break;
            case 2:
                OSSObject ossObject= ossClient.getObject( erqiStatFileRequest.getBucketName(),erqiStatFileRequest.getCosPath() );
                if(ossObject.getResponse().isSuccessful()){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( "SUCCESS");
                    Map<String,Object> mapdata = new HashMap<>(  );
                    mapdata.put( "source_url",ossObject.getResponse().getUri() );
                    returnMap.setData(JSON.toJSONString( mapdata )  );
                }else{
                    returnMap.setFlg( 1);
                    returnMap.setMessage("FAILURE");
                }
                break;
            case 3:
                com.qcloud.cos.request.StatFileRequest cosStatFileRequest = new com.qcloud.cos.request.StatFileRequest( erqiStatFileRequest.getBucketName(),erqiStatFileRequest.getCosPath() );
                String statFileRestStr=  cosClient.statFile(cosStatFileRequest  );
                JSONObject jsonObjectStatRet = JSON.parseObject( statFileRestStr );
                if(jsonObjectStatRet.get( "code" ).toString().equals( "0" )){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( "SUCCESS");
                    JSONObject data = JSONObject.parseObject (jsonObjectStatRet.getString( "data" ));
                    Map<String,Object> mapdata = new HashMap<>(  );
                    mapdata.put( "source_url",data.getString( "source_url" ) );
                    returnMap.setData(JSON.toJSONString( mapdata )  );
                }else {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            default:
                try {
                    ObjectStat objectStat = minioClient.statObject( erqiStatFileRequest.getBucketName(),erqiStatFileRequest.getCosPath() );
                    if(objectStat.etag()!=null){
                        String url =  minioClient.getObjectUrl( erqiStatFileRequest.getBucketName(),erqiStatFileRequest.getCosPath() );
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS");
                        Map<String,Object> mapdata = new HashMap<>(  );
                        mapdata.put( "source_url",url );
                        returnMap.setData(JSON.toJSONString( mapdata ));
                    }else{
                        returnMap.setFlg( 1);
                        returnMap.setMessage( "FAILURE" );
                    }
                }catch (Exception ex){
                    returnMap.setFlg( 1);
                    returnMap.setMessage( "FAILURE" );
                }
        }
        return  returnMap.ReturnMap();
    }

    public InputStream getFileInputStream(ErqiGetFileInputStreamRequest erqiGetFileInputStreamRequest) {
        switch (erqiClientConfig.getCosType()) {
            case 1:
                break;
            case 2:
                OSSObject ossObject = ossClient.getObject( erqiGetFileInputStreamRequest.getBucketName(), erqiGetFileInputStreamRequest.getCosPath() );
                return ossObject.getObjectContent();
            case 3:
                com.qcloud.cos.request.GetFileInputStreamRequest cosGetFileInputStreamRequest = new com.qcloud.cos.request.GetFileInputStreamRequest( erqiGetFileInputStreamRequest.getBucketName(), erqiGetFileInputStreamRequest.getCosPath() );
                try {
                    return cosClient.getFileInputStream( cosGetFileInputStreamRequest );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    return minioClient.getObject( erqiGetFileInputStreamRequest.getBucketName(), erqiGetFileInputStreamRequest.getCosPath() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public String listFolder(ErqiListFolderRequest erqiListFolderRequest){
        ReturnMap returnMap = new ReturnMap();
        switch (erqiClientConfig.getCosType()){
            case 1:
                break;
            case 2:
                ObjectListing objectListing= ossClient.listObjects( erqiListFolderRequest.getBucketName(),erqiListFolderRequest.getCosPath() );
                List<OSSObjectSummary> ossObjectSummaries = objectListing.getObjectSummaries();
                if(ossObjectSummaries.size() >0){
                    returnMap.setFlg( 0);
                    returnMap.setMessage( "SUCCESS" );
                    JSONArray jsonArray = new JSONArray(  );
                    for(OSSObjectSummary ossObjectSummary:ossObjectSummaries){
                        Map<String,Object> tmpMap = new HashMap<>(  );
                        tmpMap.put( "name",ossObjectSummary.getKey().substring( ossObjectSummary.getKey().lastIndexOf( "/" ) + 1 ) );
                        tmpMap.put( "filesize",ossObjectSummary.getSize() );
                        tmpMap.put( "mtime" ,ossObjectSummary.getLastModified());
                        jsonArray.add( tmpMap );
                    }
                    Map<String,Object> mapdata = new HashMap<>(  );
                    mapdata.put( "infos",jsonArray );
                    returnMap.setData(JSON.toJSONString( mapdata ));
                }else{
                    returnMap.setFlg( 1);
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            case 3:
                com.qcloud.cos.request.ListFolderRequest cosListFolderRequest = new com.qcloud.cos.request.ListFolderRequest( erqiListFolderRequest.getBucketName(),"/"+erqiListFolderRequest.getCosPath() );
                String listFolderRetStr= cosClient.listFolder( cosListFolderRequest );
                JSONObject jsonObjectStatRet = JSON.parseObject( listFolderRetStr );
                if(jsonObjectStatRet.get( "code" ).toString().equals( "0" )){
                    returnMap.setFlg( 0 );
                    returnMap.setMessage( "SUCCESS");
                    returnMap.setData(jsonObjectStatRet.getString( "data" ) );
                }else {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( "FAILURE" );
                }
                break;
            default:
                try {
                    Iterable<Result<Item>> resultIterable=  minioClient.listObjects( erqiListFolderRequest.getBucketName(),erqiListFolderRequest.getCosPath() );
                    if(resultIterable.iterator().hasNext()){
                        returnMap.setFlg( 0 );
                        returnMap.setMessage( "SUCCESS");
                        JSONArray jsonArray = new JSONArray(  );
                        for(Result<Item> result:resultIterable){
                            Map<String,Object> tmpMap = new HashMap<>(  );
                            tmpMap.put( "name",result.get().objectName().substring( result.get().objectName().lastIndexOf( "/" ) + 1 )  );
                            tmpMap.put( "filesize",result.get().objectSize() );
                            tmpMap.put( "mtime" ,result.get().lastModified().getTime());
                            jsonArray.add( tmpMap );
                        }
                        Map<String,Object> mapdata = new HashMap<>(  );
                        mapdata.put( "infos",jsonArray );
                        returnMap.setData(JSON.toJSONString( mapdata ));
                    }else{
                        returnMap.setFlg( 1);
                        returnMap.setMessage( "FAILURE" );
                    }
                } catch (Exception e) {
                    returnMap.setFlg( 1);
                    returnMap.setMessage( "FAILURE" );
                }
        }
        return  returnMap.ReturnMap();
    }

    public void shutdown(){
        switch (erqiClientConfig.getCosType()){
            case 1:
                break;
            case 2:
                this.ossClient.shutdown();
                break;
            case 3:
                this.cosClient.shutdown();
                break;
            default:

        }
    }
}



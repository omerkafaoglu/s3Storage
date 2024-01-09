package org.core;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class S3StorageService {

    @Test
    public void main() throws IOException {
        /*try {
            List<Bucket> bucketList = connection().listBuckets();
            System.out.println("Bağlantı başarılı, toplam bucket sayısı: " + bucketList.size());
        } catch (MinioException e) {
            System.out.println("Bağlantı başarısız: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        BasicConfigurator.configure();
        UploadWithS3Client("Test Word.docx");

    }
    public static Properties credentialGonder()

    {
        InputStream input = null;
        try {
            input = new FileInputStream("D:\\IdeaProjects\\s3Storage\\src\\main\\resources\\s3StorageCredentials.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Properties prop = new Properties();
        try {
            prop.load(input);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

    public void UploadWithS3Client(String fileName) throws IOException {
        AmazonS3 s3Client = getAmazonS3Client(credentialGonder().getProperty("accessKey"), credentialGonder().getProperty("secretKey"), credentialGonder().getProperty("endPoint"));
        String fileToUpload = credentialGonder().getProperty("localFileFolder") + fileName;
        try {
            File file = new File(fileToUpload);

            PutObjectRequest putObjectRequest = new PutObjectRequest(credentialGonder().getProperty("bucketName"), fileName, file);
            s3Client.putObject(putObjectRequest);
        } catch (AmazonServiceException ase) {
            System.out.println("Hata Mesajı:    " + ase.getMessage());

        } catch (AmazonClientException ace) {
            System.out.println("Hata Mesajı: " + ace.getMessage());
        }
    }

    /*private static MinioClient connection () throws IOException {
        InputStream input = new FileInputStream("D:\\Projects\\DYOP\\core\\src\\main\\resources\\s3Storage\\s3StorageCredentials.properties");
        Properties prop = new Properties();
        prop.load(input);

        MinioClient minioClient = MinioClient.builder()
                .endpoint(prop.getProperty("endPoint"))
                .credentials(prop.getProperty("accessKey"), prop.getProperty("secretKey"))
                .build();

        return minioClient;
    }*/

    public static AmazonS3 getAmazonS3Client(String accessKey, String secretKey, String endPoint) {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return s3client;
    }

}

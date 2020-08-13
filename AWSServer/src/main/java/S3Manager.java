import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.*;
import java.util.Properties;

public class S3Manager {
    String access_key_id;
    String secret_access_key;
    String bucket_name;
    String file_name;
    AWSCredentials awsCreds;
    AmazonS3Client s3Client;

    public S3Manager() throws IOException
    {
        Properties props = new Properties();
        InputStream input = ServerDriver.class.getClassLoader().getResourceAsStream("s3.properties");
        props.load(input);
        this.access_key_id = props.getProperty("access_key_id");
        this.secret_access_key = props.getProperty("secret_access_key");
        this.bucket_name = props.getProperty("bucket_name");
        this.file_name = props.getProperty("file_name");
        this.awsCreds = new BasicAWSCredentials(access_key_id, secret_access_key);
        this.s3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.DEFAULT_REGION).build();
    }

    public InputStream getStream()
    {
        InputStream in = s3Client.getObject(bucket_name, file_name).getObjectContent();
        return in;
    }

    public void addEntry(String entryid, String name) throws IOException
    {
        InputStream in = getStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        File file = File.createTempFile("names", ".txt");
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(file));
        String line = "";
        while((line = reader.readLine()) != null)
        {
            if(!line.equals("\n"))
            {
                myWriter.write(line+"\n");
            }

        }
        myWriter.write(entryid+","+name);
        myWriter.close();
        //replace names file with this
        s3Client.deleteObject(bucket_name, file_name);
        s3Client.putObject(bucket_name, file_name, file);

    }


}

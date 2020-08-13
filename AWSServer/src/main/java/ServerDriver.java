import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.awt.peer.SystemTrayPeer;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ServerDriver {


    SQLManager SQLManage;
    S3Manager S3Manage;


    public ServerDriver() throws Exception
    {
        this.SQLManage = new SQLManager();
        SQLManage.initiate();
        this.S3Manage = new S3Manager();
    }

    public static void main(String[] args)throws Exception {

        System.out.print("starting\n");
        ServerDriver driver = new ServerDriver();
        driver.updateTable();
        driver.addPerson("19", "jackson");//add person
        System.out.println(driver.nameFromID("3"));

    }

    private void addPerson(String entryID, String entryName) throws SQLException, IOException
    {
        if(!SQLManage.contains(entryID))
        {
            SQLManage.addEntry(entryID, entryName);
            S3Manage.addEntry(entryID, entryName);
        }
    }

    private String nameFromID(String entryID) throws SQLException
    {
        if(SQLManage.contains(entryID))
        {
            return SQLManage.getName(entryID);
        }
        return "no entry with this id";

    }


    private void updateTable() throws Exception//read in names.txt from s3.
    {
        InputStream in = S3Manage.getStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        int id = -1;
        String name = "";
        while((line = reader.readLine()) != null)
        {
            if(line.contains(","))
            {
                String[] split = line.split(",");
                String entryID = split[0];
                String entryName = split[1];
                SQLManage.addEntry(entryID, entryName);
            }

        }

    }









}
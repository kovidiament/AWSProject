package org.kovi.server;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.net.*;

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
        ServerSocket serSock = new ServerSocket(3000);
        String receiveMessage, sendMessage;
         while(true) 
         {
                   
             Socket server = serSock.accept();
             new Thread(new ServerThread(server, driver)).start();
             
        }
        
    }
    
    protected String passwordFromUsername(String user) throws SQLException
    {
        return SQLManage.passwordFromUsername(user);
    }

    protected String addPerson(String entryID, String entryName) throws SQLException, IOException
    {
        if(!SQLManage.contains(entryID))
        {
            SQLManage.addEntry(entryID, entryName);
            S3Manage.addEntry(entryID, entryName);
            return "added entry.";
        }
        else
        {
            return "A person with this ID already exists.";
        }
    }

    protected String nameFromID(String entryID) throws SQLException
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




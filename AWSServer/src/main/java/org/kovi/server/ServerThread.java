package org.kovi.server;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.net.*;
import java.lang.*;

public class ServerThread implements Runnable
{
	ServerDriver drive;
	Socket cSocket;
	public ServerThread(Socket cSock, ServerDriver driver)
	{
		this.cSocket = cSock;
		this.drive = driver;
	}
	public void run()  {
    
		try{
                String ip=(((InetSocketAddress) cSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
                String requestTime = String.valueOf(System.currentTimeMillis());
                String requestType;
                String returnedValue;   
           		System.out.println("Just connected to " + ip);
                BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
           if(isValidUser(in, out))
            {
                out.writeUTF("Thank you for connecting to the server. \nOptions:\nTo add an ID-Name pair to the database, enter the following:\nadd ID name. Example: add 98 John Smith.\nTo get a name from an ID number, enter get ID. Example: get 98\n");
                 String line;
                 int id;
                 while((line = in.readLine())!=null)
                 {

                    String[] words = line.split(" ");
                    if(words.length < 2)
                    {
                      returnedValue = "Invalid input";
                      out.writeUTF(returnedValue+"\n");
                    }
                    id = checkID(words[1], out);
                    if(id > 0)
                    {
                      if(words[0].equals("add"))
                      {
                          requestType = "add";
                          if(words.length < 3)
                          {
                            returnedValue = "Invalid input";
                            out.writeUTF(returnedValue+"\n");
                          }
                          else
                          {
                            String name = words[2];
                            StringBuilder nameBuilder = new StringBuilder(name);
                            for(int index = 3; index < words.length; index++)
                            {
                                nameBuilder.append(" "+words[index]);
                            }
                            String finalName = nameBuilder.toString();
                            try
                            {
                              returnedValue = drive.addPerson(words[1], finalName);
                               out.writeUTF(returnedValue+"\n");
                            }
                            catch(Exception e)
                            {
                              returnedValue = "Something went wrong adding this name.\n"+e.getMessage();
                              out.writeUTF(returnedValue+"\n");
                            }
                          }
                      }
                      if(words[0].equals("get"))
                      {
                        requestType = "get";
                        returnedValue = drive.nameFromID(words[1]);
                           out.writeUTF(returnedValue+"\n");
                      }
                      else
                      {
                      out.writeUTF(words[0]+" is not a valid command.\n");
                      }
                    }
                 }
                 cSocket.close();
              }
              else
              {
                out.writeUTF("Incorrect username or password. Closing connection.");
                cSocket.close();
              }   
            }
             catch (Exception s) 
            {
                String msg = s.getMessage();
                System.out.println(msg);
                 
            }

	}

  private boolean isValidUser(BufferedReader in, DataOutputStream out) throws IOException, SQLException
  {
    String username, password;
    out.writeUTF("Enter username\n");
    username = in.readLine();
    out.writeUTF("Enter password\n");
    password = in.readLine();
    if(password.equals("not present"))
    {
      return false;
    }
    else
    {
      String result = drive.passwordFromUsername(username);
      if(result.equals(password))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  private int checkID(String idInput, DataOutputStream out) throws Exception
  {
      int id = -1;
      try
      {
        id = Integer.parseInt(idInput);
      }
      catch(NumberFormatException e)
      {
          out.writeUTF("ID provided is not an integer.\n");
          return -1;
      }
      if(id < 1)
      {
         out.writeUTF("ID cannot be less than 1.\n");
         return -1;
      }

      return id;
 }
  
}
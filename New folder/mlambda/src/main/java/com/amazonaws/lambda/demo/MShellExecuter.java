package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class MShellExecuter {

	private static String USERNAME ="ubuntu"; // username for remote host
	//private static String PASSWORD ="password"; // password of the remote host
	private static String host = "ec2-34-210-43-89.us-west-2.compute.amazonaws.com"; // remote host address
	private static int port=22;
	private static String AccessKey = "./Ubuntu-1.pem";

	// C:\\Users\\shadim\\Downloads\\
	/**
	 * This method will execute the script file on the server.
	 * This takes file name to be executed as an argument
	 * The result will be returned in the form of the list
	 * @param scriptFileName
	 * @return
	 */
	public List<String> executeFile(String scriptFileName) {

		List<String> result = new ArrayList<String>();
		try     {
			/**
			 * Create a new Jsch object
			 * This object will execute shell commands or scripts on server
			 */
			JSch jsch = new JSch();  // this is your problem. this jar is not in the packaged jar.. which is why it doesn't know which class this is.

			/*
			 * Open a new session, with your username, host and port
			 * Set the password and call connect.
			 * session.connect() opens a new connection to remote SSH server.
			 * Once the connection is established, you can initiate a new channel.
			 * this channel is needed to connect to remotely execution program
			 */
			jsch.addIdentity(AccessKey);
			Session session = jsch.getSession(USERNAME, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			//session.setPassword(PASSWORD);
			session.connect();
			//create the excution channel over the session
			ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
			// Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
			InputStream in = channelExec.getInputStream();
			// Set the command that you want to execute
			// In our case its the remote shell script
			channelExec.setCommand(scriptFileName);
			// Execute the command
			channelExec.connect();
			// Read the output from the input stream we set above
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			//Read each line from the buffered reader and add it to result list
			// You can also simple print the result here
			while ((line = reader.readLine()) != null)
			{
				result.add(line);
			}
			//retrieve the exit status of the remote command corresponding to this channel
			int exitStatus = channelExec.getExitStatus();
			//Safely disconnect channel and disconnect session. If not done then it may cause resource leak
			channelExec.disconnect();
			session.disconnect();
			if(exitStatus < 0){
				// System.out.println("Done, but exit status not set!");
			}
			else if(exitStatus > 0){
				// System.out.println("Done, but with error!");
			}
			else{
				// System.out.println("Done!");
			}
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e);
		}
		return result;
	}

	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MShellExecuter sh = new MShellExecuter();
		//sh.takeSnapshot("www.yahoo.com");

		System.out.println(sh.takeSnapshot("www.yahoo.com"));
	}
	*/


}

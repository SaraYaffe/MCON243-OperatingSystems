package mod4;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SplittableRandom;


public class Server
{
    /**
     * @param args
     * @author Sara Yaffe and Chaya Haor
     */
    public static void main(String[] args)
    {
        args = new String[]{"01203"};

        if (args.length != 1)
        {
            System.out.println("Oops, forgot to enter port");
            System.exit(1);
        }

        try
        {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket1 = serverSocket.accept();
            PrintWriter responseWriter1 = new PrintWriter(clientSocket1.getOutputStream(), true);
            BufferedReader requestReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

            //accept message and divide into packets
            String phrase = requestReader1.readLine();
            int len = phrase.length();
            String letter;
            ArrayList<String> list = new ArrayList<>();

            
            //number the packets
            for (int i = 0; i < len; i++)
            {
                letter = phrase.split("(?!^)")[i];
                if(i < 10)
                {
                	list.add("0" + i + "." + letter);
                }
                else {
                	list.add(i + "." + letter);
                }
                            }
            
            //send packets to client
            sendPackets(list, responseWriter1);

            
            
            
            //accepts dropped list from client and attempts to resend missing packets
            String s;
            while (!(phrase = requestReader1.readLine()).equals("finished"))
            {
	            ArrayList<String> missing = new ArrayList<>();
	            int numMissing = Integer.parseInt(phrase);
	            for (int i = 0; i < numMissing; i++)
	            {
	
	            	s = requestReader1.readLine();
	            	
	            	if(Integer.parseInt(s) < 10)
	            	{
	            		s = "0" + s;
	            	}
	            	
	            	for(int ix = 0; ix < list.size(); ix++) {
	            	String location;
	            	
	            	location = list.get(ix).split("(?!^)")[0] + list.get(ix).split("(?!^)")[1] ;
	            	if(location.equals(s)) 
	            	{
	            		
	            		missing.add(list.get(ix));
	                }
	            	}
	            }
	            
	            sendPackets(missing, responseWriter1);
            }
            
            
            
            
            
            System.out.println("All packets received by client");
           


        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    //send packets to client at 80% probability
    //if sent successfully, add to a shuffled list and print to response writer and print confirmation message
    private static void sendPackets(ArrayList<String> list, PrintWriter responseWriter1)
    {
        int len = list.size();
        ArrayList<String> shuffledList = new ArrayList<>();
        for (int i = 0; i < len; i++)
        {
            SplittableRandom random = new SplittableRandom();
            boolean dropPacket = random.nextInt(1, 100) <= 20;
            if (!dropPacket)
            {
                shuffledList.add(list.get(i));
            }
        }
        Collections.shuffle(shuffledList);

        for (String s : shuffledList)
        {
            responseWriter1.println(s);
            System.out.println(s + " --  packet received");
        }
        responseWriter1.println("DONE");
    }
}



          
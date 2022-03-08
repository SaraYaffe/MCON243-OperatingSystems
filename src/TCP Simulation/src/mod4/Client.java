package mod4;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Client
{
    /**
     * @param args ports
     * @throws IOException when needed
     * @author Sara Yaffe and Chaya Haor
     */
    public static void main(String[] args) throws IOException
    {
        args = new String[]{"127.0.0.1", "01203"};
        ArrayList<String> list = new ArrayList<>();


        if (args.length != 2)
        {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (Socket clientSocket = new Socket(hostName, portNumber);
             PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader responseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)))
        {
        	
        	//input message and send to Server
            String phrase = stdIn.readLine();
            requestWriter.println(phrase);

            //print received packets and add to a list of received packets
            //sort received list
            while (!(phrase = responseReader.readLine()).equals("DONE"))
            {
                list.add(phrase);
                System.out.println(phrase);
            }
            
            Collections.sort(list);

            
            //create missing list returned from missingMethod
            ArrayList<Integer> dropped = missingMethod(list);
            
            

            
            
            
            //sends dropped list item #s to server to resend
            while(!dropped.isEmpty())
            {
            	int numMissing = dropped.size();   
            	requestWriter.println(numMissing);   
                for (int i = 0; i < numMissing; i++)
                {
                	
                    requestWriter.println(dropped.get(i));
                }
                //requestWriter.println("Sent packets");
                while (!(phrase = responseReader.readLine()).equals("DONE"))
                {
                    list.add(phrase);
                    System.out.println(phrase);
                }
                
                Collections.sort(list);
                dropped = missingMethod(list);
            }
            requestWriter.println("finished");
            
            
            
            
            

            System.out.println("all packets received from server");
            System.out.print("original message: ");
            for(int i = 0; i < list.size(); i++)
            {
                System.out.print(list.get(i).substring(list.get(i).length() - 1));
            }

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    //determines which # packets were dropped and returns list of dropped #s
    public static ArrayList<Integer> missingMethod(ArrayList<String> list)
    {
        ArrayList<Integer> dropped = new ArrayList<>();
        int index;
        int upTo=0;
        for (int i = 0; i < list.size(); i++)
        {
            String location = list.get(i).split("(?!^)")[0] + list.get(i).split("(?!^)")[1];
            index = Integer.parseInt(location);
            while (index != upTo)
            {
                dropped.add(upTo);
                upTo++;
            }
            upTo++;

        }
        System.out.println("dropped list sent to server: " + dropped);
        return dropped;
    }

}




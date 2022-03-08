import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveB_fromMaster extends Thread {
	private ArrayList<Job> pendingJobs;
	private Object pendingJobs_LOCK;

	public SlaveB_fromMaster(ArrayList<Job> pendingJobs, Object pendingJobs_LOCK) {
		this.pendingJobs = pendingJobs;
		this.pendingJobs_LOCK = pendingJobs_LOCK;
	}

	public void run() {
		String hostName = "127.0.0.1";
		int portNumber = 34567;

		try (Socket socket = new Socket(hostName, portNumber);
				ObjectInputStream readNewJobs = new ObjectInputStream(socket.getInputStream())) {

			while (true) {
				Job uncompletedJob = (Job) readNewJobs.readObject();
				synchronized (pendingJobs_LOCK) {
					pendingJobs.add(uncompletedJob);
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}

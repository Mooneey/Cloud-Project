import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;



public class aws {

	/*
	 * Cloud Computing, Data Computing Laboratory Department of Computer Science
	 * Chungbuk National University
	 */

	static AmazonEC2 ec2;

	private static void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		}

		catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		/* check the region at AWS console */
		ec2 = AmazonEC2ClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-east-1").build();
	}

	public static void main(String[] args) throws Exception {
		init();
		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;
		while (true) {
			System.out.println("															");
			System.out.println("															");
			System.out.println("------------------------------------------------------------");
			System.out.println(" Amazon AWS Control Panel using SDK ");
			System.out.println("															");
			System.out.println(" Cloud Computing, Computer Science Department ");
			System.out.println("							at Chungbuk National University ");
			System.out.println("------------------------------------------------------------");
			System.out.println(" 1. list instance					2. available zones		");
			System.out.println(" 3. start instance					4. available regions	");
			System.out.println(" 5. stop instance					6. create instance		");
			System.out.println(" 7. reboot instance					8. list images			");
			System.out.println(" 									99. quit				");
			System.out.println("------------------------------------------------------------");

			System.out.print("Enter an integer: ");
			
			number = menu.nextInt();
			
			switch (number) {
			case 1:
				listInstances();
				break;
				
			case 2:
				//availableZone();
				break;
				
			case 3:
				//startInstance();
				break;
				
			case 4:
				//availableRegion();
				break;
				
			case 5:
				//stopInstance();
				break;
				
			case 6:
				//createInstance();
				break;
				
			case 7:
				//rebootInstance();
				break;
				
			case 8:
				//listImage();
				break;
				
			case 9:
				//terminateInstance();
				break;
				
			case 99:
				//quit();
				break;
			}
		}
	}

	public static void listInstances() {
		System.out.println("Listing instances....");
		
		boolean done = false;
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		
		while (!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);
			for (Reservation reservation : response.getReservations()) {
				for (Instance instance : reservation.getInstances()) {
					System.out.printf(
						"[id] %s, " + 
						"[AMI] %s, " + 
						"[type] %s, " +
						"[state] %10s, " + 
						"[monitoring state] %s",
						instance.getInstanceId(),
						instance.getImageId(), 
						instance.getInstanceType(),
						instance.getState().getName(),
						instance.getMonitoring().getState());
				}
				System.out.println();
			}
			request.setNextToken(response.getNextToken());
			if (response.getNextToken() == null) {
				done = true;
			}
		}		
	}
	
	public static void startInstance(String instanceId) {

	    StartInstancesRequest request = new StartInstancesRequest()
	            .withInstanceIds(instanceId);

	    ec2.startInstances(request);
	}

	
	public static void stopInstance(String instanceId) {
		
        StopInstancesRequest request = new StopInstancesRequest()
                .withInstanceIds(instanceId);
        
        ec2.stopInstances(request);

        // snippet-end:[ec2.java2.start_stop_instance.stop]
        System.out.printf("Successfully stop instance %s", instanceId);
    }
	
	public static void rebootInstance(String instanceId) {
		  try {

		        RebootInstancesRequest request = new RebootInstancesRequest()
		            .withInstanceIds(instanceId);

		        ec2.rebootInstances(request);
		        System.out.printf("Successfully rebooted instance %s", instanceId);
		  }
		  
		  catch(Exception e) {
		        System.out.printf("Successfully rebooted instance %s", instanceId);

		  }
	}
	
}

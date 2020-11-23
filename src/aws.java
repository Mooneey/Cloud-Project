import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

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

		String instanceId;
		String imageId;
		
		int number = 0;

		while (true) {
			System.out.println("															");
			System.out.println("															");
			System.out.println("------------------------------------------------------------");
			System.out.println(" Amazon AWS Control Panel using SDK ");
			System.out.println("															");
			System.out.println(" Cloud Computing, Computer Science Department ");
			System.out.println("			at Chungbuk National University ");
			System.out.println("------------------------------------------------------------");
			System.out.println(" 1. list instance					2. available zones		");
			System.out.println(" 3. start instance					4. available regions	");
			System.out.println(" 5. stop instance					6. create instance		");
			System.out.println(" 7. reboot instance					8. list images			");
			System.out.println(" 9. terminate instance				99. quit				");
			System.out.println("------------------------------------------------------------");

			System.out.print("Enter an integer: ");

			number = menu.nextInt();

			switch (number) {
			case 1:
				listInstances();
				break;

			case 2:
				availableZones();
				break;

			case 3:
				System.out.print("Enter instance ID : ");
				instanceId = id_string.next();
				startInstance(instanceId);
				break;

			case 4:
				availableRegions();
				break;

			case 5:
				System.out.print("Enter instance ID : ");
				instanceId = id_string.next();
				stopInstance(instanceId);
				break;

			case 6:
				System.out.print("Enter ami ID : ");
				imageId = id_string.next();
				createInstance(imageId);
				break;

			case 7:
				System.out.print("Enter instance ID : ");
				instanceId = id_string.next();
				rebootInstance(instanceId);
				break;

			case 8:
				listImages();
				break;

			case 9:
				System.out.print("Enter instance ID : ");
				instanceId = id_string.next();
				terminateInstance(instanceId);
				break;
				
			case 10:
				startAllInstance();
				break;

			case 11:
				listKeyPair();
				break;
				
			case 12:
				listSecurityGroup();
				break;
				
			case 99:
				System.out.println("bye!");
				System.exit(0);
				break;
			}
		}
	}

//	================= 1. list instance =================
	public static void listInstances() {
		System.out.println("Listing instances....");
		System.out.println();
		
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
							"[state] %10s, " 
							+ "[monitoring state] %s",
							instance.getInstanceId(), 
							instance.getImageId(), 
							instance.getInstanceType(),
							instance.getState().getName(), 
							instance.getMonitoring().getState()
					);
				}
				System.out.println(2);
			}
			request.setNextToken(response.getNextToken());
			if (response.getNextToken() == null) {
				done = true;
			}
		}
	}

	
//	================= 2. available zones =================
	public static void availableZones() {
		System.out.println("Listing available zones....\n");
		
		DescribeAvailabilityZonesResult zonesResult = 
				ec2.describeAvailabilityZones();

		for (AvailabilityZone zone : zonesResult.getAvailabilityZones()) {
			System.out.printf("[zone name] %s, " +
								"[state] %s, " + 
								"[region name] %s\n",
								zone.getZoneName(),
								zone.getState(), 
								zone.getRegionName()
			);
		}
		System.out.println();
	}

	
//	================= 3. start instance =================
	public static void startInstance(String instanceId) {
		System.out.printf("Starting.... %s\n", instanceId);

		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instanceId);

		ec2.startInstances(request);

		System.out.printf("Successfully started instance %s", instanceId);
		System.out.println();
	}

	
//	================= 4. available regions =================
	public static void availableRegions() {
		System.out.println("Listing available regions....\n");
		
		DescribeRegionsResult regions_response = ec2.describeRegions();

		for(Region region : regions_response.getRegions()) {
		    System.out.printf("[name] %s " + 
		    					"[endpoint] %s\n", 
		    					region.getRegionName(), 
		    					region.getEndpoint()
    		);
		}
	    System.out.println();
	}

	
//	================= 5. stop instance =================
	public static void stopInstance(String instanceId) {
		System.out.printf("Stopping.... %s\n", instanceId);

		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);

		ec2.stopInstances(request);

		System.out.printf("Successfully stop instance %s", instanceId);
		System.out.println();
	}

	
//	================= 6. create instance =================
	public static void createInstance(String amiId) {
		System.out.println("Creating Instance....\n");
	
		RunInstancesRequest request = new RunInstancesRequest()
				.withImageId(amiId)
				.withInstanceType(InstanceType.T2Micro)
				.withMaxCount(1)
				.withMinCount(1);
		
		RunInstancesResult result = ec2.runInstances(request);

		String instanceId = result.getReservation()
										.getInstances()
										.get(0)
										.getInstanceId();

		System.out.printf("Successfully started EC2 instance %s based on AMI %s\n", 
				instanceId, amiId);
	}

	
//	================= 7. reboot instance =================
	public static void rebootInstance(String instanceId) {
		try {
			System.out.printf("Rebooting.... %s\n", instanceId);

			RebootInstancesRequest request = new RebootInstancesRequest()
					.withInstanceIds(instanceId);

			ec2.rebootInstances(request);

			System.out.printf("Successfully rebooted instance %s\n", instanceId);
		}

		catch (Exception e) {

		}
	}
	
	
//	================= 8. list images =================
	public static void listImages() {
		System.out.println("Loading images....\n");

		DescribeImagesRequest request = new DescribeImagesRequest().withOwners("self");
		DescribeImagesResult Images = ec2.describeImages(request);
		for (Image image : Images.getImages()) {
			System.out.printf("[ImageID] %s,  " + 
								"[Name] %s,  " + 
								"[Owner] %s  " +
								"[state] %10s", 
								image.getImageId(), 
								image.getName(),
								image.getOwnerId(),
								image.getState()
			);
		}
		System.out.println();
	}
	
	
//	================= 9. terminate instance =================
	public static void terminateInstance(String instanceId) {
		System.out.printf("Terminating.... %s\n", instanceId);

		TerminateInstancesRequest request = new TerminateInstancesRequest()
				.withInstanceIds(instanceId);

		ec2.terminateInstances(request);

		System.out.printf("Successfully terminated instance %s\n", instanceId);
	}
	
	
//	================= 10. start all instance =================
	public static void startAllInstance() {
			
//		System.out.println("Start all instance....\n");
//		
//		boolean done = false;
		
//		InstanceStates instanceState = new InstanceStates();
//		DescribeInstancesRequest request = new DescribeInstancesRequest();
//
//		while (!done) {
//			DescribeInstancesResult response = ec2.describeInstances(request);
//			for (Reservation reservation : response.getReservations()) {
//				for (Instance instance : reservation.getInstances()) {
//					
//					if(instance.getState().getCode() == InstanceStates.STOOPED) {
//						startInstance(instance.getInstanceId());
//					}					
//				}
//			}
//			
//			request.setNextToken(response.getNextToken());
//			if (response.getNextToken() == null) {
//				done = true;
//			}
//		}
	}
	
	
//	================= 11. list key pair =================
	public static void listKeyPair() {
		DescribeKeyPairsResult response = ec2.describeKeyPairs();

		for(KeyPairInfo key_pair : response.getKeyPairs()) {
		    System.out.printf(
		        "[key name] %15s " +
		        "[fingerprint] %s\n",
		        key_pair.getKeyName(),
		        key_pair.getKeyFingerprint());
		}	
		
		System.out.println();
	}
	
	
//	================= 12. list security group =================	
	public static void listSecurityGroup() {

        DescribeSecurityGroupsRequest request =
            new DescribeSecurityGroupsRequest();

        DescribeSecurityGroupsResult response =
            ec2.describeSecurityGroups(request);

        for(SecurityGroup group : response.getSecurityGroups()) {
            System.out.printf(
                "[id] %22s, " +
                "[name] %20s " +
                "[vpc id] %s " +
                "[description] %s\n",
                group.getGroupId(),
                group.getGroupName(),
                group.getVpcId(),
                group.getDescription()
            );
        }		
		System.out.println();
	}
}


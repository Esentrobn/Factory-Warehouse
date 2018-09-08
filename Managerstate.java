

import java.util.*;

import java.text.*;
import java.io.*;
public class Managerstate extends WareHouseUIState {
	private static Managerstate managerstate;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;


	private static final int EXIT = 0;
	private static final int addManufacturer = 1;
	private static final int associateProdandMan = 2;
	private static final int disassociateProdandMan = 3;
	private static final int changePrice = 4;
	private static final int makeClerk = 5;
	private static final int HELP = 6;


	private Managerstate() {
		warehouse = Warehouse.instance();
	}

	public static Managerstate instance() {
		if (managerstate == null) {
			return managerstate = new Managerstate();
		} else {
			return managerstate;
		}
	}
	public String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
				if (tokenizer.hasMoreTokens()) {
					return tokenizer.nextToken();
				}
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);
	}
	private boolean yesOrNo(String prompt) {
		String more = getToken(prompt + " (Y|y)[es] or anything else for no");
		if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
			return false;
		}
		return true;
	}
	public double getNumber(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				double num = Double.parseDouble(item);
				return num;
			} catch (NumberFormatException nfe) {
				System.out.println("Please input a number ");
			}
		} while (true);
	}

	public int getCommand() {
	
		do {
			try {

				int value = Integer.parseInt(getToken("Enter command:"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number");
			}


		} while (true);
	}
	
	private void SecurityCheck() {
		 int trycount = 0;
		 while (!Securitystate.instance().isLocked() ){
			 WarehouseUIContext.instance().changeState(4);
			 trycount++;

			 if (trycount > 5) {
				System.out.println("Too many tries, logging out");
				logout();
			}
		}
	}

	public void help() {
		System.out.println("Enter a number between 0 and 12 as explained below:\n");
		System.out.println(EXIT + " to Exit");
		System.out.println(addManufacturer + " to add a Manufacturer"); 
		System.out.println(associateProdandMan + " to associate a product and manufacturer");  
		System.out.println(disassociateProdandMan + " to dissassociate a product and manufacturer");  
		System.out.println(changePrice + " modify a price of a product");  
		System.out.println(makeClerk + " to change to clerk mode");  
		System.out.println(HELP + " for help\n");
	}




	public void process() {
		int command;
		System.out.println("Please login before making choice");
			help();
			while ((command = getCommand())  != EXIT) {

				switch (command) {

				case addManufacturer: addManufacturer(); break;
				case associateProdandMan: associateProdandMan(); break;
				case disassociateProdandMan: disassociateProdandMan(); break;
				case changePrice : changePrice(); break;
				case makeClerk : makeClerk(); break;

				case HELP:              help();
				break;
				}
				Securitystate.instance().reLock();


			}
				logout();
			}

			private void makeClerk() {
				WarehouseUIContext.instance().changeState(0);
			}

			private void changePrice() {

				String productName = getToken("Enter product id to change price for");
				double newPrice = getNumber("Enter new price for the item");

				if (warehouse.changePrice(productName, newPrice)) {
					System.out.println("Change successful");

				}
				else{
					System.out.println("Failed to change price");

				}


			}

			private void disassociateProdandMan() {
				String manufacturerId;
				Manufacturer manufacturer;
				String productId;
				Product product;

				do {
					manufacturerId = getToken("Enter manufacturer id");
					manufacturer = warehouse.searchManufacturer(manufacturerId);

					if (manufacturer == null) {
						System.out.println("Invalid manufacturer id, please try again.");
					}
				} while (manufacturer == null);

				do {
					productId = getToken("Enter product id");
					product = warehouse.searchProduct(productId);

					if (product == null) {
						System.out.println("Invalid product id, please try again.");
					}
				} while (product == null);

				product = warehouse.unAssignProduct(productId, manufacturerId);
				System.out.println("Disassociate complete between product:" + product + " and manufacturer:" + manufacturer);
			}

			private void associateProdandMan() {
				String manuId;
				Manufacturer manufacturer;
				String productId;
				Product product;

				do {
					manuId = getToken("Enter manufacturer id");
					manufacturer = warehouse.searchManufacturer(manuId);

					if (manufacturer == null) {
						System.out.println("Invalid manufacturer id, enter new id.");
					}
				} while (manufacturer == null);

				do {
					productId = getToken("Enter product id");
					product = warehouse.searchProduct(productId);

					if (product == null) {
						System.out.println("Invalid product id, please try again.");
					}
				} while (product == null);

				product = warehouse.assignProduct(productId, manuId);
				System.out.println("Associated: " + product + " and manufacturer:" + manufacturer);
			}

			private void addManufacturer() {
				String name = getToken("Enter manufacturer name");
				Manufacturer result;
				result = warehouse.addManufacturer(name,"","");
				if (result == null) {
					System.out.println("Could not add manufacturer");
				}
				System.out.println("Added manufacturer [" + result + "]");
			}

			public void run() {
				SecurityCheck();
				process();
			}

			public void logout()
			{
				WarehouseUIContext.instance().changeState(0); // exit with a code 0
			}

		}

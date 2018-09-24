
// Name = "Matthew Fitzgerald";
import java.io.*;
import java.util.Scanner;

public class BankTeller {

	public static void main(String[] args) throws IOException {
		// constant definitions
		final int MAX_NUM = 50;

		// variable declarations
		BankAccount[] bankAcctArray = new BankAccount[MAX_NUM]; // Array of bank accounts
		int numAccts; // number of accounts
		char choice; // menu item selected
		boolean not_done = true; // loop control flag

		// open input test cases file
		// File testFile = new File("mytestcases.txt");

		// create Scanner object
		// Scanner kybd = new Scanner(testFile);
		Scanner kybd = new Scanner(System.in);

		// open the output file
		PrintWriter outFile = new PrintWriter("myoutput.txt");

		/* first part */
		/* fill and print initial database */
		numAccts = readAccts(bankAcctArray, MAX_NUM);
		printAccts(bankAcctArray, numAccts, outFile);

		/* second part */
		/* prompts for a transaction and then */
		/* call functions to process the requested transaction */
		do {
			menu();
			choice = kybd.next().charAt(0);
			switch (choice) {
			case 'q':
			case 'Q':
				not_done = false;
				printAccts(bankAcctArray, numAccts, outFile);
				break;
			case 'b':
			case 'B':
				balance(bankAcctArray, numAccts, outFile, kybd);
				break;
			case 'd':
			case 'D':
				deposit(bankAcctArray, numAccts, outFile, kybd);
				break;
			case 'w':
			case 'W':
				withdrawal(bankAcctArray, numAccts, outFile, kybd);
				break;
			case 'n':
			case 'N':
				numAccts = newAcct(bankAcctArray, numAccts, outFile, kybd);
				break;
			case 'x':
			case 'X':
				numAccts = deleteAcct(bankAcctArray, numAccts, outFile, kybd);
				break;
			case 'i':
			case 'I':
				accountInfo(bankAcctArray, numAccts);
			default:
				outFile.println("Error: " + choice + " is an invalid selection -  try again");
				outFile.println();
				outFile.flush();
				break;
			}

		} while (not_done);

		// close the output file
		outFile.close();

		// close the test cases input file
		kybd.close();

		System.out.println();
		System.out.println("The program is terminating");
	}

	private static void accountInfo(BankAccount[] accounts, int numAccts) {
		// TODO Add error processing for SSN lookup and add notes
		String requestedSsn;
		int index;
		Scanner kybd = new Scanner(System.in);
		Depositor tempDep = new Depositor();
		BankAccount tempAcc = new BankAccount();

		System.out.println();
		System.out.println("Please enter your Social Security Number" + "\n as one whole number");
		requestedSsn = kybd.nextLine();
		for (index = 0; index < numAccts;) {
			tempAcc = accounts[index];
			tempDep = accounts[index].getDepositor();
			int tempSsn = Integer.parseInt(tempDep.getSsn());
			if (Integer.parseInt(requestedSsn) == tempSsn) {
				System.out.println("Social Security Number: " + requestedSsn + "\n");
				System.out.println("Name: " + tempDep.getName());
				System.out.println("Account number: " + tempAcc.getAccountNumber());
				System.out.println("Account Balance: " + tempAcc.getBalance());
				System.out.println("Account Type: " + tempAcc.getAccountType());
				System.out.println();
				kybd.close();
				return;
			} else {// If no SSN is found
				System.out.println("No SSN found.");
				kybd.close();
				return;
			}
		}

	}

	/*
	 * Method readAccts() Input: acctNumArray - reference to array of account
	 * numbers balanceArray - reference to array of account balances maxAccts -
	 * maximum number of active accounts allowed Process: Reads the initial database
	 * of accounts and balances Output: Fills in the initial BankAccount array and
	 * returns the number of active accounts
	 */
	public static int readAccts(BankAccount[] accounts, int maxAccts) throws IOException {
		// open database input file
		// create File object
		File dbFile = new File("myinput.txt");
		// create Scanner object
		Scanner sc = new Scanner(dbFile);
		int count = 0; // account number counter
		String line;

		while (sc.hasNext() && count < maxAccts) {
			line = sc.nextLine();
			String[] tokens = line.split(" ");
			Depositor tempDep = new Depositor(tokens[2] + " " + tokens[3], tokens[4]);
			BankAccount newAcc = new BankAccount(Integer.parseInt(tokens[0]), tokens[5], Double.parseDouble(tokens[1]),
					tempDep);
			accounts[count] = newAcc;
			count++;
		}

		// close the input file
		sc.close();

		// return the account number count
		return count;
	}

	/*
	 * Method printAccts: Input: accounts - array of BankAccount objects numAccts -
	 * number of active accounts outFile - reference to the output file Process:
	 * Prints the database of accounts and balances Output: Prints the database of
	 * accounts and balances
	 */
	public static void printAccts(BankAccount[] accounts, int numAccts, PrintWriter outFile) {
		Depositor tempDep = new Depositor();
		outFile.println();
		outFile.println("\t\tDatabase of Bank Accounts");
		outFile.printf("");
		outFile.println();
		for (int index = 0; index < numAccts; index++) {
			tempDep = accounts[index].getDepositor();
			outFile.printf("%7d  $%7.2f	%22s %7s %7s", accounts[index].getAccountNumber(), accounts[index].getBalance(),
					tempDep.getName(), tempDep.getSsn(), accounts[index].getAccountType());
			outFile.println();
		}
		outFile.println();

		// flush the output file
		outFile.flush();
	}

	/*
	 * Method menu() Input: none Process: Prints the menu of transaction choices
	 * Output: Prints the menu of transaction choices
	 */
	public static void menu() {
		System.out.println();
		System.out.println("Select one of the following transactions:");
		System.out.println("\t****************************");
		System.out.println("\t    List of Choices         ");
		System.out.println("\t****************************");
		System.out.println("\t     W -- Withdrawal");
		System.out.println("\t     D -- Deposit");
		System.out.println("\t     N -- New Account");
		System.out.println("\t     B -- Balance Inquiry");
		System.out.println("\t     I -- Account Info");
		System.out.println("\t     X -- Delete Account");
		System.out.println("\t     Q -- Quit");
		System.out.println();
		System.out.print("\tEnter your selection: ");
	}

	/*
	 * Method findAcct: Input: accounts - array of account numbers numAccts - number
	 * of active accounts requestedAccount - requested account requested_number
	 * Process: Performs a linear search on the acctNunArray for the requested
	 * account Output: If found, the index of the requested account is returned
	 * Otherwise, returns -1
	 */
	public static int findAcct(BankAccount[] accounts, int numAccts, int requestedAccount) {
		for (int index = 0; index < numAccts; index++) {
			if (accounts[index].getAccountNumber() == requestedAccount)
				return index;
		}
		return -1;
	}

	/*
	 * TODO Fix discription add error checking
	 * 
	 * Method balance: Input: acctNumArray - array of account numbers balanceArray -
	 * array of account balances numAccts - number of active accounts outFile -
	 * reference to output file kybd - reference to the "test cases" input file
	 * Process: Prompts for the requested account Calls findAcct() to see if the
	 * account exists If the account exists, the balance is printed Otherwise, an
	 * error message is printed Output: If the account exists, the balance is
	 * printed Otherwise, an error message is printed
	 */
	public static void balance(BankAccount[] accounts, int numAccts, PrintWriter outFile, Scanner kybd) {
		int requestedAccount;
		int index;

		System.out.println();
		System.out.print("Enter the account number: "); // prompt for account number
		requestedAccount = kybd.nextInt(); // read-in the account number
		System.out.println(requestedAccount);

		// call findAcct to search if requestedAccount exists
		index = findAcct(accounts, numAccts, requestedAccount);

		if (index == -1) // invalid account
		{
			outFile.println("Transaction Requested: Balance Inquiry");
			outFile.println("Error: Account number " + requestedAccount + " does not exist");
		} else // valid account
		{
			outFile.println("Transaction Requested: Balance Inquiry");
			outFile.println("Account Number: " + requestedAccount);
			outFile.printf("Current Balance: $%.2f", accounts[index].getBalance());
			outFile.println();
		}
		outFile.println();

		outFile.flush(); // flush the output buffer
	}

	/*TODO EDIT THIS
	 * Method deposit: Input: account - array of BankAccount objects numAccts -
	 * number of active accounts outFile - reference to the output file kybd -
	 * reference to the "test cases" input file Process: Prompts for the requested
	 * account Calls findacct() to see if the account exists If the account exists,
	 * prompts for the amount to deposit If the amount is valid, it makes the
	 * deposit and prints the new balance Otherwise, an error message is printed
	 * Output: For a valid deposit, the deposit transaction is printed Otherwise, an
	 * error message is printed
	 */
	public static void deposit(BankAccount[] account, int numAccts, PrintWriter outFile, Scanner kybd) {
		int requestedAccount;
		int index;
		double amountToDeposit;

		System.out.println();
		System.out.print("Enter the account number: "); // prompt for account number
		requestedAccount = kybd.nextInt(); // read-in the account number

		// call findAcct to search if requestedAccount exists
		index = findAcct(account, numAccts, requestedAccount);

		if (index == -1) // invalid account
		{
			outFile.println("Transaction Requested: Deposit");
			outFile.println("Error: Account number " + requestedAccount + " does not exist");
		} else // valid account
		{
			System.out.print("Enter amount to deposit: "); // prompt for amount to deposit
			amountToDeposit = kybd.nextDouble(); // read-in the amount to deposit

			if (amountToDeposit <= 0.00) {
				// invalid amount to deposit
				outFile.println("Transaction Requested: Deposit");
				outFile.println("Account Number: " + requestedAccount);
				outFile.printf("Error: $%.2f is an invalid amount", amountToDeposit);
				outFile.println();
			} else {
				outFile.println("Transaction Requested: Deposit");
				outFile.println("Account Number: " + requestedAccount);
				outFile.printf("Old Balance: $%.2f", account[index].getBalance());
				outFile.println();
				outFile.println("Amount to Deposit: $" + amountToDeposit);
				account[index].setBalance(account[index].getBalance() + amountToDeposit); // make the deposit
				outFile.printf("New Balance: $%.2f", account[index].getBalance());
				outFile.println();
			}
		}
		outFile.println();

		outFile.flush(); // flush the output buffer
	}
/*
 * TODO add method desc
 * 
 */
	public static void withdrawal(BankAccount[] accounts, int numAccts, PrintWriter outFile, Scanner kybd) {
		int requestedAccount;
		int index;
		double amountToWithdraw;

		System.out.println();
		System.out.print("Enter the account number: "); // prompt for account number
		requestedAccount = kybd.nextInt(); // read-in the account number

		// call findAcct to search if requestedAccount exists
		index = findAcct(accounts, numAccts, requestedAccount);

		if (index == -1) // invalid account
		{
			outFile.println("Transaction Requested: Withdrawl");
			outFile.println("Error: Account number " + requestedAccount + " does not exist");
		} else // valid account
		{
			System.out.print("Enter amount to withdraw: "); // prompt for amount to withdraw
			amountToWithdraw = kybd.nextDouble(); // read-in the amount to withdraw

			if (amountToWithdraw <= 0.00 || amountToWithdraw > accounts[index].getBalance()) { // Checks for invalid
																								// amount to
				// withdraw
				outFile.println("Transaction Requested: Withdrawl");
				outFile.println("Account Number: " + requestedAccount);
				if (amountToWithdraw > accounts[index].getBalance()) {
					outFile.printf("You have $%.2f in your account.", accounts[index].getBalance());
					outFile.println("Insufficent Funds.");
					System.out.printf("You have $%.2f in your account.", accounts[index].getBalance());
					System.out.println("Insufficent Funds.");
					System.out.println("");
					outFile.println("");
				}
				outFile.printf("Error: $%.2f is an invalid amount", amountToWithdraw);
				outFile.println();
			} else {
				outFile.println("Transaction Requested: Withdrawl");
				outFile.println("Account Number: " + requestedAccount);
				outFile.printf("Old Balance: $%.2f", accounts[index].getBalance());
				outFile.println();
				outFile.println("Amount to withdraw: $" + amountToWithdraw);
				accounts[index].setBalance(accounts[index].getBalance() - amountToWithdraw);
				; // make the deposit
				outFile.printf("New Balance: $%.2f", accounts[index].getBalance());
				outFile.println();
			}
		}
		outFile.println();

		outFile.flush(); // flush the output buffer

	}
/*
 * TODO add method desc
 * 
 * 
 * 
 */
	public static int newAcct(BankAccount[] accounts, int numAccts, PrintWriter outFile, Scanner kybd) {
		int desiredAccount;
		int index = 0;
		int choice;
		boolean not_done = true;

		System.out.println();
		System.out.println("Enter your desired account number. Must have exactly 6 digits."); // Checks to make sure
																								// account is formatted
																								// properly
		desiredAccount = kybd.nextInt();
		if (desiredAccount <= 99999 || desiredAccount > 999999) {
			outFile.println("Transaction Requested: New Account");
			outFile.println("Attempted account number " + desiredAccount + " is incorrectly formatted.");
			outFile.flush();
			return numAccts;
		} else {
			index = findAcct(accounts, numAccts, desiredAccount);

		}
		// Error if account already exists else create new account
		if (index != -1) {
			System.out.println("Attempted account number " + desiredAccount + " already exsits.");
			outFile.println("Transaction Requested: New Account");
			outFile.println("Attempted account number " + desiredAccount + " already exsits.");
			outFile.flush();
			return numAccts;
		}
		System.out.println("Please enter your first name");
		String newFirstName = kybd.next();
		System.out.println("Please enter Your last name.");
		String newLastName = kybd.next();
		System.out.println("Please enter your Social Security Number as one number."); // TODO Add social processing
		String newSsn;
		newSsn = kybd.next();
		Depositor newDep = new Depositor(newFirstName + " " + newLastName, newSsn);
		do {
			showAcctMenu();
			choice = kybd.next().charAt(0);
			switch (choice) {
			case '1':
				System.out.println("Account Sucessfully created!");
				System.out.println();
				BankAccount newAccountSavings = new BankAccount(desiredAccount, "Savings", 0.0, newDep);
				outFile.println("New account created:");
				outFile.println("Social Security Number: " + newDep.getSsn() + "\n");
				outFile.println("Name: " + newDep.getName());
				outFile.println("Account number: " + newAccountSavings.getAccountNumber());
				outFile.println("Account Balance: " + newAccountSavings.getBalance());
				outFile.println("Account Type: " + newAccountSavings.getAccountType());
				outFile.println();
				accounts[numAccts] = newAccountSavings;
				not_done = false;
				break;
			case '2':
				System.out.println("Account Sucessfully created!");
				System.out.println();
				BankAccount newAccountChecking = new BankAccount(desiredAccount, "Checking", 0.0, newDep);
				outFile.println("New account created:");
				outFile.println();
				outFile.println("Social Security Number: " + newDep.getSsn() + "\n");
				outFile.println("Name: " + newDep.getName());
				outFile.println("Account number: " + newAccountChecking.getAccountNumber());
				outFile.println("Account Balance: " + newAccountChecking.getBalance());
				outFile.println("Account Type: " + newAccountChecking.getAccountType());
				outFile.println();
				accounts[numAccts] = newAccountChecking;
				not_done = false;
				break;
			case '3':
				System.out.println("Account Sucessfully created!");
				System.out.println();
				BankAccount newAccountCD = new BankAccount(desiredAccount, "CD", 0.0, newDep);
				accounts[numAccts] = newAccountCD;
				outFile.println("New account created:");
				outFile.println();
				outFile.println("Social Security Number: " + newDep.getSsn() + "\n");
				outFile.println("Name: " + newDep.getName());
				outFile.println("Account number: " + newAccountCD.getAccountNumber());
				outFile.println("Account Balance: " + newAccountCD.getBalance());
				outFile.println("Account Type: " + newAccountCD.getAccountType());
				outFile.println();
				not_done = false;
				break;
			default:
				System.out.println("Invalid selction.");
				break;
			}
		} while (not_done);
		outFile.flush();
		return numAccts + 1;
	}
/*
 * 
 * TODO Add method description
 * 
 * 
 */
	private static void showAcctMenu() {
		System.out.println();
		System.out.println("Select one of the following transactions:");
		System.out.println("\t****************************");
		System.out.println("\t    List of Choices         ");
		System.out.println("\t****************************");
		System.out.println("\t     1 -- Savings");
		System.out.println("\t     2 -- Checking");
		System.out.println("\t     3 -- CD");
		System.out.println();
		System.out.print("\tEnter your selection: ");

	}
/*
 * TODO Add method desc
 * 
 * 
 */
	public static int deleteAcct(BankAccount[] accounts, int numAccts, PrintWriter outFile, Scanner kybd) {
		int accountToDelete;
		int index = 0;
		System.out.println();
		System.out.print("Please enter the account number you would like to close.");
		accountToDelete = kybd.nextInt();
		index = findAcct(accounts, numAccts, accountToDelete);
		if (index == -1) { // Checks if account exists
			outFile.println("Transaction Requested: Delete Account");
			outFile.println("Account number " + accountToDelete + "does not exist.");
			outFile.flush();
		} else {
			if (accounts[index].getBalance() != 0.00) { // Ensures balance is 0.00 in the array
				outFile.println("Transaction Requested: Delete Account");
				System.out.println("Account number " + accountToDelete + " must have a zero balance.");
				outFile.println("Account number " + accountToDelete + " must have a zero balance.");
				outFile.println("");
				outFile.flush();
			} else {
				if (accounts[index].getBalance() == 0.00) { // Balance IS 0; account deletion
					accounts[index] = accounts[numAccts];
					outFile.println("Account number " + accountToDelete + " has been deleted.");
					return numAccts - 1;
				}
			}
		}
		return numAccts;
	}

	/* Method pause() */
	public static void pause(Scanner keyboard) {
		String tempstr;
		System.out.println();
		System.out.print("Press ENTER to continue");
		tempstr = keyboard.nextLine(); // flush previous ENTER
		tempstr = keyboard.nextLine(); // wait for ENTER
	}

}
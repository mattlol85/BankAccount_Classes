/**
 * 
 */

/**
 * @author Matthew
 *
 */

public class BankAccount {	
private int accountNumber;
private String accountType;
private double balance;
private Depositor depositor;
//No argument
public BankAccount() {
	
accountNumber = 0;
accountType = "";
balance = 0.0;
depositor = new Depositor();

}
//With parameter
public BankAccount(int a,String s,double b,Depositor depo) {
	accountNumber = a;
	accountType = s;
	balance = b;
	depositor = depo;
}
//setters
public void setAccountNumber(int a) {
accountNumber = a;
}
public void setAccountType(String at) {
accountType = at;
}
public void setBalance(double b) {
balance = b;
}
public void setDepositor(Depositor d) {
depositor = d;
}
//getters
public int getAccountNumber() {
	return accountNumber;
}
public String getAccountType() {
	return accountType;
}
public double getBalance() {
	return balance;
}
public Depositor getDepositor() {
return depositor;
}
}

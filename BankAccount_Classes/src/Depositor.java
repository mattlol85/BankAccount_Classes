/**
 * This class contains the template for a depositor which has
 * two strings, a SSN and a name.
 */

/**
 * @author Matthew
 *
 */
public class Depositor {
private String name;
private String ssn;
//No arg
public Depositor() {
name = "";
ssn = "";
	
}
//With Parameters
public Depositor(String n,String s) {
	name = n;
	ssn = s;
}
//setters
public void setName(String n) {
	name = n;
}
public void setSsn(String s) {
	ssn = s;
}
//getters
public String getName() {
	return name;
}
public String getSsn() {
	return ssn;
}
}

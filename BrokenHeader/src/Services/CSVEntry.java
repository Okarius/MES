package Services;
/**
 * Every in the Handler used CSVEntry (PhoneBookEntry usw.) 
 * Hast du implement this interface. 
 * Thus it is possible to write back every List of CSVEntry
 * using the CSVWorker.
 * @author root
 *
 */
public interface CSVEntry {
	public abstract String objectToLine();
}
	

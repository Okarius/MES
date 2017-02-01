package Services;
/**
 * Every handler that uses CSV files 
 * Has to use this interface for its "CSVEntries"(eg. Phonebookentry). 
 * Thus it is possible to write back every List of CSVEntry
 * using the CSVWorker.
 * @author Niki
 *
 */
public interface CSVEntry {
	/**
	 * Used to write Object back to CSV-File
	 * @return String with every attributes value divided by ";" 
	 */
	public abstract String objectToLine();
}
	

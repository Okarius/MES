package Services;
/**
 * Every Handler who uses CSV files 
 * Has to use this interface for its "CSV"Attributes (eg. Phonebookentry). 
 * Thus it is possible to write back every List of CSVEntry
 * using the CSVWorker.
 * @author Niki
 *
 */
public interface CSVEntry {
	public abstract String objectToLine();
}
	

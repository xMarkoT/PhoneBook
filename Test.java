
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		String rawFilePath = ""; // enter source file path
		String parsedFilePath = ""; // enter target file path
		PhoneBook phoneBook = new PhoneBook(rawFilePath, parsedFilePath);

		phoneBook.parseRawFile();

		// phoneBook.addPhoneRecordToFile(name, number);
		// phoneBook.removePhoneRecord(name);
		// phoneBook.printSortedMapByName();
		// System.out.println(phoneBook.getPhoneNumberByName(name));

		// generateCalls(phoneBook.getPhoneToCall(), phoneBook);

		// phoneBook.printCallStatistic();
	}

	private static void generateCalls(Map<String, Integer> phoneToCall, PhoneBook phoneBook) {
		int mapSize = phoneToCall.size();
		int createCalls = 100;

		Random random = new Random();
		List<String> keySet = new ArrayList<String>(phoneToCall.keySet());

		for (int i = 0; i < createCalls; i++) {
			phoneBook.outgoingCall(keySet.get(random.nextInt(mapSize)));
		}
	}
}

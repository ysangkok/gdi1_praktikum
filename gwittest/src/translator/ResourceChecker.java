package translator;

import java.util.Arrays;
import java.util.Vector;

public class ResourceChecker {
	public ResourceChecker() {
		// do nothing
	}

	public ResourceChecker(String[] args) {
		ExtendedResourceBundle[] bundles = new ExtendedResourceBundle[args.length - 1];
		String[] resourceNames = new String[args.length - 1];
		String currentResourceName = null;
		ExtendedResourceBundle currentBundle = null;
		System.out.println("Requested to check resource base name " + args[0]);
		int i, pos = 0;
		for (i = 1; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("none"))
				currentResourceName = args[0];
			else
				currentResourceName = args[0] + '.' + args[i];
			currentBundle = new ExtendedResourceBundle(currentResourceName);
			if (currentBundle != null && currentBundle.getKeys() != null
					&& currentBundle.getKeys().length > 0) {
				resourceNames[pos] = currentResourceName;
				System.out.println("Successfully loaded bundle " + resourceNames[pos]
						+ " for language code " + args[i]);
				bundles[pos++] = currentBundle;
			} else
				System.out.println("Error in loading resource '" + currentResourceName
						+ "'");
		}
		int errors;
		// compare keys crosswise -- may take a while!
		for (i = 0; i < pos; i++) {
			String[] masterResourceKeys = bundles[i].getKeys();
			for (int j = 0; j < pos; j++)
				if (i != j && bundles[j] != null) {
					Vector<String> errorEntries = new Vector<String>(50, 25);
					errors = 0;
					System.out
							.println("***********************************************************\nComparing resource '"
									+ resourceNames[i] + "' with '" + resourceNames[j] + "'");
					// String[] secondResourceKeys = bundles[j].getKeys();
					for (int keyIndex = 0; keyIndex < masterResourceKeys.length; keyIndex++)
						if (bundles[j].getMessage(masterResourceKeys[keyIndex], false) == null) {
							errors++;
							errorEntries.add(masterResourceKeys[keyIndex]);
							// System.out.println("key '" + masterResourceKeys[keyIndex] +"'
							// missing in resource '" +resourceNames[j] +"'");
						}
					if (errors > 0) {
						Object[] entries = new String[errors];
						errorEntries.copyInto(entries);
						Arrays.sort(entries);
						for (int p = 0; p < errors; p++)
							System.out.println("key '" + entries[p]
									+ "' missing in resource '" + resourceNames[j] + "'");

					}
					System.out.println(errors + " errors found.");
				}
		}
	}

	public static void main(String[] args) {
		if (args.length < 3)
			System.out.println("Usage: java translator.ResourceChecker baseFileName ext1 ext2 ...");
		else
			new ResourceChecker(args);
	}
}

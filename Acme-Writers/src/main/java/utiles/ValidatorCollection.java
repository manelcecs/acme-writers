
package utiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidatorCollection {

	public static boolean validateURLCollection(final Collection<String> urls) {
		boolean res = true;
		try {
			for (final String url : urls)
				if (!url.isEmpty())
					new URL(url);
		} catch (final MalformedURLException e) {
			res = false;
		}

		return res;
	}

	public static List<String> deleteStringsBlanksInCollection(final List<String> collectionString) {
		final List<String> strings = collectionString;
		final List<String> stringsBlanks = new ArrayList<>();

		for (final String s : strings)
			if (s.isEmpty())
				stringsBlanks.add(s);

		strings.removeAll(stringsBlanks);
		return strings;
	}

	public static Collection<String> deleteStringsBlanksInCollection(final Collection<String> collectionString) {
		final Collection<String> strings = collectionString;
		final Collection<String> stringsBlanks = new ArrayList<>();

		for (final String s : strings)
			if (s.isEmpty())
				stringsBlanks.add(s);

		strings.removeAll(stringsBlanks);
		return strings;
	}

}


package utiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class ValidateCollectionURL {

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

	public static Collection<String> deleteURLBlanksInCollection(final Collection<String> pictureURLs) {
		final Collection<String> urls = pictureURLs;
		final Collection<String> urlsBlanks = new ArrayList<>();

		for (final String url : urls)
			if (url.isEmpty())
				urlsBlanks.add(url);

		urls.removeAll(urlsBlanks);
		return urls;
	}

}

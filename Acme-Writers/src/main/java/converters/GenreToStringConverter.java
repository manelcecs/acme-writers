
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Genre;

@Component
@Transactional
public class GenreToStringConverter implements Converter<Genre, String> {

	@Override
	public String convert(final Genre genre) {
		final String result;
		if (genre == null)
			result = null;
		else
			result = String.valueOf(genre.getId());
		return result;
	}
}

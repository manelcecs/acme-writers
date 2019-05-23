
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Writer;

@Component
@Transactional
public class WriterToStringConverter implements Converter<Writer, String> {

	@Override
	public String convert(final Writer actor) {
		final String result;
		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}
}


package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Publisher;

@Component
@Transactional
public class PublisherToStringConverter implements Converter<Publisher, String> {

	@Override
	public String convert(final Publisher publisher) {
		final String result;
		if (publisher == null)
			result = null;
		else
			result = String.valueOf(publisher.getId());
		return result;
	}
}

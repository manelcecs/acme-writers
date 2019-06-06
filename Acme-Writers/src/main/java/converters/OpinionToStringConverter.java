
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Opinion;

@Component
@Transactional
public class OpinionToStringConverter implements Converter<Opinion, String> {

	@Override
	public String convert(final Opinion opinion) {
		final String result;
		if (opinion == null)
			result = null;
		else
			result = String.valueOf(opinion.getId());
		return result;
	}
}

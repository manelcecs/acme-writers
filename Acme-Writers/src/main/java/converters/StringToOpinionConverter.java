
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import services.OpinionService;
import domain.Opinion;

@Component
@Transactional
public class StringToOpinionConverter implements Converter<String, Opinion> {

	@Autowired
	private OpinionService	opinionService;


	@Override
	public Opinion convert(final String text) {
		final Opinion result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.opinionService.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}

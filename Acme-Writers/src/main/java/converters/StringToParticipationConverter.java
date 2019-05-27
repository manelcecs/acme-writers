
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import services.ParticipationService;
import domain.Participation;

@Component
@Transactional
public class StringToParticipationConverter implements Converter<String, Participation> {

	@Autowired
	private ParticipationService	participationService;


	@Override
	public Participation convert(final String text) {
		final Participation result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.participationService.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}

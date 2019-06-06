
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import services.WriterService;
import domain.Writer;

@Component
@Transactional
public class StringToWriterConverter implements Converter<String, Writer> {

	@Autowired
	private WriterService	writerService;


	@Override
	public Writer convert(final String text) {
		Writer result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.writerService.findOne(id);

			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}

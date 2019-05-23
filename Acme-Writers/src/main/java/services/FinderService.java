
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import security.LoginService;
import utiles.AuthorityMethods;
import domain.Book;
import domain.Finder;
import domain.Genre;

@Service
@Transactional
public class FinderService {

	@Autowired
	private FinderRepository		finderRepository;

	@Autowired
	private BookService				bookService;

	@Autowired
	private ReaderService			readerService;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private Validator				validator;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	public Finder save(final Finder finder) throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		final LocalDateTime DATETIMENOW = LocalDateTime.now();

		Assert.notNull(finder);
		final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
			+ DATETIMENOW.getSecondOfMinute());

		Assert.isTrue(this.readerService.findByPrincipal(LoginService.getPrincipal()).getFinder().getId() == finder.getId());

		String keyWord = finder.getKeyWord();
		Integer minNumWords = finder.getMinNumWords();
		Integer maxNumWords = finder.getMaxNumWords();
		String language = finder.getLanguage();
		int minGenre;
		int maxGenre;
		final Genre genre = finder.getGenre();

		if (keyWord == null)
			keyWord = "";
		if (minNumWords == null)
			minNumWords = 0;
		if (maxNumWords == null)
			maxNumWords = 2147483647;
		if (language == null)
			language = "";
		if (genre == null) {
			minGenre = 0;
			maxGenre = 2147483647;
		} else {
			minGenre = genre.getId();
			maxGenre = genre.getId();
		}

		final Collection<Book> results = this.bookService.getFilterBooksByFinder(keyWord, minNumWords, maxNumWords, language, minGenre, maxGenre);

		List<Book> returnResults = new ArrayList<Book>();
		returnResults.addAll(results);
		final Integer maxFinderResults = this.adminConfigService.getAdminConfig().getFinderResults();
		if (returnResults.size() > maxFinderResults)
			returnResults = returnResults.subList(0, maxFinderResults);
		finder.setBooks(returnResults);
		finder.setLastUpdate(actual);
		return this.finderRepository.save(finder);
	}

	public Finder clear(final Finder finder) throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		Assert.notNull(finder);

		finder.setKeyWord(null);
		finder.setMinNumWords(null);
		finder.setMaxNumWords(null);
		finder.setLanguage(null);
		finder.setGenre(null);

		return this.save(finder);
	}
	public Finder findOne(final int finderId) {
		Assert.notNull(finderId);
		return this.finderRepository.findOne(finderId);
	}

	public Boolean cacheFinder(final Finder finderA, final Finder finderB) throws ParseException {
		Boolean result;
		final LocalDateTime DATETIMENOW = LocalDateTime.now();

		final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + DATETIMENOW.getMinuteOfHour() + ":" + DATETIMENOW.getSecondOfMinute());
		final Calendar cal = Calendar.getInstance();
		cal.setTime(finderA.getLastUpdate());
		cal.set(Calendar.MILLISECOND, (cal.get(Calendar.MILLISECOND) + (this.adminConfigService.getAdminConfig().getFinderCacheTime() * 3600000)));
		final Date expirationDate = cal.getTime();

		Boolean keyWord = true;
		if (finderA.getKeyWord() != null && finderB.getKeyWord() == null)
			keyWord = finderA.getKeyWord().isEmpty();
		if (finderA.getKeyWord() == null && finderB.getKeyWord() != null)
			keyWord = finderB.getKeyWord().isEmpty();
		if (finderA.getKeyWord() != null && finderB.getKeyWord() != null)
			keyWord = finderA.getKeyWord().equals(finderB.getKeyWord());

		Boolean minNumWords = false;
		if (finderA.getMinNumWords() == null && finderB.getMinNumWords() == null)
			minNumWords = true;
		if (finderA.getMinNumWords() != null && finderB.getMinNumWords() != null)
			minNumWords = finderA.getMinNumWords() - finderB.getMinNumWords() == 0;

		Boolean maxNumWords = false;
		if (finderA.getMaxNumWords() == null && finderB.getMaxNumWords() == null)
			maxNumWords = true;
		if (finderA.getMaxNumWords() != null && finderB.getMaxNumWords() != null)
			maxNumWords = finderA.getMaxNumWords() - finderB.getMaxNumWords() == 0;

		Boolean language = true;
		if (finderA.getLanguage() != null && finderB.getLanguage() == null)
			language = finderA.getLanguage().isEmpty();
		if (finderA.getLanguage() == null && finderB.getLanguage() != null)
			language = finderB.getLanguage().isEmpty();
		if (finderA.getLanguage() != null && finderB.getLanguage() != null)
			language = finderA.getLanguage().equals(finderB.getLanguage());

		Boolean genre = false;
		if (finderA.getGenre() == null && finderB.getGenre() == null)
			genre = true;
		if (finderA.getGenre() != null && finderB.getGenre() != null)
			genre = finderB.getGenre().getId() == finderA.getGenre().getId();

		result = keyWord && minNumWords && maxNumWords && actual.before(expirationDate) && language && genre;
		return result;
	}
	public Finder reconstruct(final Finder finder, final BindingResult binding) {

		final Finder result = this.finderRepository.findOne(this.readerService.findByPrincipal(LoginService.getPrincipal()).getFinder().getId());

		finder.setId(result.getId());
		finder.setVersion(result.getVersion());
		finder.setBooks(result.getBooks());
		finder.setLastUpdate(result.getLastUpdate());
		this.validator.validate(finder, binding);

		return finder;
	}

	public Finder create() throws ParseException {
		final Finder finder = new Finder();

		final Date actual = this.FORMAT.parse("0001/01/01 01:00:00");
		finder.setLastUpdate(actual);
		return finder;
	}
	public Finder generateNewFinder() throws ParseException {

		final Finder finder = this.create();
		Finder res;
		res = this.finderRepository.save(finder);
		return res;
	}

	public void flush() {
		this.finderRepository.flush();
	}

	public Collection<Finder> getFindersByGenre(final int idGenre) {
		return this.finderRepository.getFindersByGenre(idGenre);
	}

	public Finder updateGenre(final Finder finder) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.finderRepository.save(finder);
	}

}

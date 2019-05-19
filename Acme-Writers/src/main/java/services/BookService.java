
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BookRepository;
import utiles.AuthorityMethods;
import domain.Book;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository	bookRepository;


	public Book findOne(final int idBook) {
		return this.bookRepository.findOne(idBook);
	}

	public Collection<Book> getFilterBooksByKeyword(final String keyword) {
		return this.bookRepository.getFilterBooksByKeyword(keyword);
	}

	public Collection<Book> getFilterBooksByFinder(final String keyWord, final Integer minNumWords, final Integer maxNumWords, final String language, final int minGenre, final int maxGenre) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		return this.bookRepository.getFilterBooksByFinder(keyWord, minNumWords, maxNumWords, language, minGenre, maxGenre);
	}

	public Collection<Book> getBooksByFinder(final int idFinder) {
		return this.bookRepository.getBooksByFinder(idFinder);
	}
}

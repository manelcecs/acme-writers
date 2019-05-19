
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.BookRepository;
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
}

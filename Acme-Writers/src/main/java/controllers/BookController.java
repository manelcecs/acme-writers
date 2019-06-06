
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.BookService;
import services.ChapterService;
import services.OpinionService;
import services.ReaderService;
import utiles.AuthorityMethods;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Actor;
import domain.Book;
import domain.Chapter;
import domain.Reader;
import domain.Writer;

@Controller
@RequestMapping("/book")
public class BookController extends AbstractController {

	@Autowired
	private BookService		bookService;

	@Autowired
	private ChapterService	chapterService;

	@Autowired
	private ReaderService	readerService;

	@Autowired
	private OpinionService	opinionService;

	@Autowired
	private ActorService	actorService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer idBook) {
		ModelAndView result;

		final Book book = this.bookService.findOne(idBook);

		if (!book.getDraft() && !book.getCancelled() && (book.getStatus().equals("INDEPENDENT") || book.getStatus().equals("ACCEPTED"))) {
			result = new ModelAndView("book/display");
			result.addObject("book", book);
			result.addObject("chapters", this.chapterService.getChaptersOfABook(book.getId()));

			result.addObject("opinions", this.opinionService.getOpinionsOfBook(idBook));
			result.addObject("requestURIChapters", "book/display.do?idBook=" + idBook);
			result.addObject("requestURIOpinions", "book/display.do?idBook=" + idBook);

			this.configValues(result);
		} else
			result = new ModelAndView("redirect:listAll.do");

		return result;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {

		final Collection<Book> books = this.bookService.getAllVisibleBooks();
		return this.listModelAndView(books, "book/listAll.do", "book.title.listAll");
	}

	@RequestMapping(value = "/listByWriter", method = RequestMethod.GET)
	public ModelAndView listByWriter(@RequestParam final int idWriter) {
		final Collection<Book> books = this.bookService.getAllVisibleBooksOfWriter(idWriter);
		return this.listModelAndView(books, "book/listByWriter.do", "book.title.listByWriters");
	}

	@RequestMapping(value = "/listByPublisher", method = RequestMethod.GET)
	public ModelAndView listByPublisher(@RequestParam final int idPublisher) {
		final Collection<Book> books = this.bookService.getAllVisibleBooksOfPublisher(idPublisher);
		return this.listModelAndView(books, "book/listByPublisher.do", "book.title.listByPublishers");
	}

	@RequestMapping(value = "/listRecommended", method = RequestMethod.GET)
	public ModelAndView listByScore() {
		final Collection<Book> books = this.bookService.getBooksOrderedByScore();
		return this.listModelAndView(books, "book/listRecommended.do", "book.title.listRecommended");
	}

	protected ModelAndView listModelAndView(final Collection<Book> books, final String requestURI, final String title) {
		final ModelAndView result = new ModelAndView("book/list");
		result.addObject("books", books);
		result.addObject("requestURI", requestURI);
		result.addObject("title", title);

		try {
			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			final Boolean favourites = true;
			final Collection<Book> favouritesBooks = reader.getBooks();
			result.addObject("favouritesBooks", favouritesBooks);
			result.addObject("favourites", favourites);
			result.addObject("targetURL", "/book/listAll.do");

		} catch (final Throwable socorro) {

		}

		this.configValues(result);
		return result;

	}

	/**
	 * @param idBook
	 * @param response
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadBook", method = RequestMethod.GET)
	public void downloadBook(@RequestParam final int idBook, final HttpServletResponse response) throws DocumentException, IOException {
		final Book book = this.bookService.findOne(idBook);

		final Actor actorLogged = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final String authority = AuthorityMethods.getLoggedAuthority().getAuthority();
		if ((book.getWriter().equals(actorLogged)) || (authority.equals("READER") && !book.getDraft() && !book.getCancelled() && (book.getStatus().equals("INDEPENDENT") || book.getStatus().equals("ACCEPTED")))) {

			boolean existCover = true;
			if (book.getCover() != null)
				try {
					final URL url = new URL(book.getCover());
					final Image image = Image.getInstance(url);

				} catch (final Throwable oops) {
					existCover = false;
				}
			response.setContentType("application/pdf");

			final Document doc = new Document();

			final PdfWriter pdfWriter = PdfWriter.getInstance(doc, response.getOutputStream());
			doc.open();
			final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(idBook);
			final BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

			//Portada
			if (existCover) {
				final Image image = Image.getInstance(new URL(book.getCover()));
				image.setAlignment(Element.ALIGN_MIDDLE | Image.TEXTWRAP);
				//Revisar el tamaño de la portada
				doc.add(image);
				doc.add(Chunk.NEWLINE);
				doc.add(Chunk.NEWLINE);
			}

			final Font fontTitle = new Font(helvetica, 24, Font.BOLD);
			final Paragraph paragraphTitle = new Paragraph(book.getTitle(), fontTitle);
			paragraphTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(paragraphTitle);

			final Font fontAuthor = new Font(helvetica, 20, Font.BOLD);
			final Writer writer = book.getWriter();
			final String subtitle = writer.getName() + " " + writer.getSurname();
			final Paragraph paragraphAuthor = new Paragraph(subtitle, fontAuthor);
			paragraphAuthor.setAlignment(Element.ALIGN_CENTER);
			doc.add(paragraphAuthor);

			final Font fontRef = new Font(helvetica, 14, Font.NORMAL);
			final Paragraph paragraphRef = new Paragraph("ref: " + book.getTicker().getIdentifier(), fontRef);
			paragraphRef.setAlignment(Element.ALIGN_CENTER);
			doc.add(paragraphRef);
			doc.add(Chunk.NEWLINE);

			doc.newPage();

			//Capitulos
			final Font fontChapter = new Font(helvetica, 12, Font.NORMAL);
			final Font fontChapterTitle = new Font(helvetica, 14, Font.ITALIC);
			for (final Chapter chapter : chapters) {
				doc.add(Chunk.NEWLINE);
				final Paragraph paragraphChapterTitle = new Paragraph(chapter.getNumber() + " - " + chapter.getTitle(), fontChapterTitle);
				paragraphChapterTitle.setAlignment(Element.ALIGN_CENTER);
				doc.add(paragraphChapterTitle);
				doc.add(Chunk.NEWLINE);
				final Paragraph paragraph = new Paragraph(chapter.getText(), fontChapter);
				paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
				doc.add(paragraph);
				doc.newPage();
			}

			doc.close();
			pdfWriter.close();
		}

	}
}

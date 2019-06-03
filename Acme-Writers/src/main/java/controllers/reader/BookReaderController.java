
package controllers.reader;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BookService;
import services.ChapterService;
import services.OpinionService;
import services.ReaderService;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import controllers.AbstractController;
import domain.Book;
import domain.Chapter;
import domain.Reader;
import domain.Writer;

@Controller
@RequestMapping("/book/reader")
public class BookReaderController extends AbstractController {

	@Autowired
	private BookService		bookService;

	@Autowired
	private ReaderService	readerService;

	@Autowired
	ChapterService			chapterService;

	@Autowired
	OpinionService			opinionService;


	@RequestMapping(value = "/listFavourites", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("book/list");
		final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
		result.addObject("books", reader.getBooks());
		result.addObject("favourites", true);
		result.addObject("favouritesBooks", reader.getBooks());
		result.addObject("requestURI", "book/reader/listFavourites.do");
		result.addObject("title", "book.title.favourites");

		this.configValues(result);

		return result;
	}

	@RequestMapping(value = "/addToList", method = RequestMethod.GET)
	public ModelAndView addToList(@RequestParam final int idBook) {
		ModelAndView result;
		try {
			this.bookService.addBookToFavouriteList(idBook);
			result = new ModelAndView("redirect:listFavourites.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("cannot.add.book");
		}

		return result;
	}

	@RequestMapping(value = "/removeFromFavourites", method = RequestMethod.GET)
	public ModelAndView removeFromFavourites(@RequestParam final int idBook) {
		ModelAndView result;
		try {
			this.bookService.deleteBookFromFavouriteList(idBook);
			result = new ModelAndView("redirect:listFavourites.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("cannot.remove.book");
		}

		return result;
	}

	protected ModelAndView listModelAndView() {

		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("book/list");
		final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
		result.addObject("books", reader.getBooks());
		result.addObject("favourites", true);
		result.addObject("message", message);
		result.addObject("requestURI", "book/reader/listFavourites.do");
		result.addObject("title", "book.title.favourites");

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
		response.setContentType("application/pdf");

		final Document doc = new Document();

		final PdfWriter pdfWriter = PdfWriter.getInstance(doc, response.getOutputStream());
		doc.open();
		final Book book = this.bookService.findOne(idBook);
		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(idBook);
		final BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

		//		Portada
		//		if (book.getCover() != null) {
		//			final URL url = new URL(book.getCover());
		//			final Image image = Image.getInstance(url);
		//			image.setAlignment(Element.ALIGN_MIDDLE | Image.TEXTWRAP);
		//			//Revisar el tamaño de la portada
		//			//image.scaleAbsolute(5f, 10f);
		//			doc.add(image);
		//			doc.add(Chunk.NEWLINE);
		//			doc.add(Chunk.NEWLINE);
		//		}

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

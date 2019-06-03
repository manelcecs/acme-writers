
package utiles;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import services.BookService;
import services.ChapterService;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Book;
import domain.Chapter;
import domain.Writer;

public class PDFtest {

	@Autowired
	private BookService		bookService;

	@Autowired
	private ChapterService	chapterService;


	public void main(final String[] args) throws DocumentException, IOException {
		final Document doc = new Document();
		final PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream("prueba.pdf"));

		doc.open();
		final Book book = this.bookService.findOne(282);
		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(282);
		final BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		//		final Font font = new Font(helvetica, 12, Font.NORMAL);
		//		final Chunk chunk = new Chunk("Esto es una prueba cojones", font);

		//Portada
		final URL url = new URL("https://images-na.ssl-images-amazon.com/images/I/41wbyRiUbvL.jpg");
		final Image image = Image.getInstance(url);
		image.setAlignment(Element.ALIGN_MIDDLE);
		//Revisar el tamaño de la portada
		//image.scaleAbsolute(5f, 10f);
		doc.add(image);

		//		final Font fontTitle = new Font(helvetica, 24, Font.BOLD);
		//		final Chunk chunkTitle = new Chunk(book.getTitle(), fontTitle);
		//		doc.add(chunkTitle);

		final Font fontAuthor = new Font(helvetica, 20, Font.BOLD);
		//FIXME: Arreglar que aprezca la editorial solo cuando existe
		final Writer writer = book.getWriter();
		final String subtitle = writer.getName() + " " + writer.getSurname() + "\n" + "\n";
		System.out.println(subtitle);
		final Chunk chunkAuthor = new Chunk(subtitle, fontAuthor);
		doc.add(chunkAuthor);
		//		final Publisher publisher = book.getPublisher();
		//		final String subtitlePublisher = publisher.getCommercialName();
		//		System.out.println(subtitlePublisher);
		//		final Chunk chunkPublisher = new Chunk(subtitlePublisher, fontAuthor);
		//		doc.add(chunkPublisher);

		final Font fontRef = new Font(helvetica, 20, Font.NORMAL);
		final Chunk chunkRef = new Chunk("ref: " + book.getTicker().getIdentifier() + "\n" + "\n", fontRef);
		doc.add(chunkRef);

		//Datos del libro

		//Capitulos

		doc.close();
		pdfWriter.close();

	}

}


package services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Book;
import domain.Chapter;
import domain.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PdfTest {

	@Autowired
	private BookService		bookService;

	@Autowired
	private ChapterService	chapterService;


	@Test
	public void generatePDF() throws DocumentException, IOException {
		final Document doc = new Document();

		final PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream("C:/Documents and Settings/Student/Desktop/prueba" + DateTime.now().getMillis() + ".pdf"));

		doc.open();
		final Book book = this.bookService.findOne(32769);
		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(32769);
		final BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

		//		Portada
		final URL url = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCDKWDc7brZFoPzbDw4kMqH4kdU6xk7GJfyV3OwqrWPxF9KYrd");
		final Image image = Image.getInstance(url);
		image.setAlignment(Element.ALIGN_MIDDLE | Image.TEXTWRAP);
		//Revisar el tamaño de la portada
		//image.scaleAbsolute(5f, 10f);
		doc.add(image);
		doc.add(Chunk.NEWLINE);
		doc.add(Chunk.NEWLINE);

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

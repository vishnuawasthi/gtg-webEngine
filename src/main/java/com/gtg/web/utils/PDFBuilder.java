package com.gtg.web.utils;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gtg.lib.dto.UserDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilder extends AbstractITextPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container
		List<UserDTO> listBooks = (List<UserDTO>) model.get("users");
		document.add(new Paragraph("GTG USERS LIST"));
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] { 3.0f, 2.0f, 2.0f, 2.0f, 1.0f, 2.0f, 2.0f, 2.0f });
		table.setSpacingBefore(10);
		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(5);
		// write table header
		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("FIRST NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("LAST NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("EMAIL", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("USER NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("ROLE NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("ROLE ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("CREATED", font));
		table.addCell(cell);

		/*
		 * // write table row data for (Book aBook : listBooks) {
		 * table.addCell(aBook.getTitle()); table.addCell(aBook.getAuthor());
		 * table.addCell(aBook.getIsbn());
		 * table.addCell(aBook.getPublishedDate());
		 * table.addCell(String.valueOf(aBook.getPrice())); }
		 */
		document.add(table);
		System.out.println("buildPdfDocument()- end");
	}

}

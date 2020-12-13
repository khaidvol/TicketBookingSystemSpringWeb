package com.epam.jgmp.pdf;

import com.epam.jgmp.model.Ticket;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class TicketsPdfBuilder extends AbstractITextPdfView {

  @Override
  protected void buildPdfDocument(
      Map<String, Object> model,
      Document document,
      PdfWriter writer,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

    // get data model which is passed by the Spring container
    List<Ticket> tickets = (List<Ticket>) model.get("tickets");

    document.add(new Paragraph("BOOKED TICKETS"));

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100.0f);
    table.setWidths(new float[] {1.0f, 2.0f, 2.0f, 2.0f, 3.0f});
    table.setSpacingBefore(10);

    // define font for table header row
    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(BaseColor.WHITE);

    // define table header cell
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(BaseColor.BLUE);
    cell.setPadding(5);

    // write table header
    cell.setPhrase(new Phrase("Ticket ID", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Event ID", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("User ID", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Place", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Category", font));
    table.addCell(cell);

    // write table row data
    for (Ticket ticket : tickets) {
      table.addCell("" + ticket.getId());
      table.addCell("" + ticket.getEventId());
      table.addCell("" + ticket.getUserId());
      table.addCell("" + ticket.getPlace());
      table.addCell("" + ticket.getCategory());
    }

    document.add(table);
  }
}

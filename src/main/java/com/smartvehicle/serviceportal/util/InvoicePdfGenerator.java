package com.smartvehicle.serviceportal.util;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.smartvehicle.serviceportal.entity.Invoice;

public class InvoicePdfGenerator {

	// ============================================================
	// GENERATE PDF FROM INVOICE
	// ============================================================
	public static byte[] generateInvoicePdf(Invoice invoice) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, outputStream);

			document.open();

			// -------- TITLE --------
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Paragraph title = new Paragraph("Vehicle Service Invoice", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(new Paragraph(" ")); // empty line

			// -------- INVOICE DETAILS --------
			document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()));
			document.add(new Paragraph("Payment Status: " + invoice.getPaymentStatus()));
			document.add(new Paragraph(" "));

			// -------- CUSTOMER DETAILS --------
			document.add(new Paragraph("Customer Name: " + invoice.getBooking().getVehicle().getUser().getName()));
			document.add(new Paragraph("Email: " + invoice.getBooking().getVehicle().getUser().getEmail()));
			document.add(new Paragraph(" "));

			// -------- VEHICLE DETAILS --------
			document.add(new Paragraph("Vehicle Number: " + invoice.getBooking().getVehicle().getVehicleNumber()));
			document.add(new Paragraph("Model: " + invoice.getBooking().getVehicle().getModel()));
			document.add(new Paragraph(" "));

			// -------- SERVICE DETAILS --------
			document.add(new Paragraph("Service: " + invoice.getBooking().getService().getServiceName()));
			document.add(new Paragraph("Duration (hours): " + invoice.getBooking().getService().getDurationHours()));
			document.add(new Paragraph("Price: ₹ " + invoice.getAmount()));

			document.close();

		} catch (Exception e) {
			throw new RuntimeException("Error generating invoice PDF");
		}

		return outputStream.toByteArray();
	}
}

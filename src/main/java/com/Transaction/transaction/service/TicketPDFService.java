package com.Transaction.transaction.service;


import com.Transaction.transaction.payloads.TicketDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class TicketPDFService {

    public byte[] generateTicketPDF(TicketDto ticket) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        document.add(new Paragraph("Ticket Information"));
        document.add(new Paragraph("Ticket ID: " + ticket.getTicketNo()));
        document.add(new Paragraph("Passenger: " + ticket.getPassengerName()));
        document.add(new Paragraph("Departure Date: " + ticket.getDepartureDate()));
        document.add(new Paragraph("Seat Number: " + ticket.getSeatNo()));
        document.add(new Paragraph("Full Name: " + ticket.getBookingTicket().getFullName()));
        document.add(new Paragraph("Email: " + ticket.getBookingTicket().getEmail()));
        document.add(new Paragraph("Booking Date: " + ticket.getSeat().getBusInfo().getRoute12().getDate()));
        document.close();

        return outputStream.toByteArray();
    }
}


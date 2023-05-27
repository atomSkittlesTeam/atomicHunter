package net.vniia.skittles.configs;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.time.LocalDateTime;



public class PdfGenerator {

    public void createPdf() throws IOException {
        String path = "offer.pdf";
        String imageFile = "E:/Projects/atomicHunter/atomichunter-rest/src/main/resources/photo_2023-05-24_22-29-53.jpg";
        ImageData data = ImageDataFactory.create(imageFile);
        String FONT = "E:/Projects/atomicHunter/atomichunter-rest/src/main/resources/OpenSans.ttf";
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        PdfFont font = PdfFontFactory.createFont(FONT);

        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        Image img = new Image(data);

        document.add(img);
        document.add(new Paragraph("ЛИЧНО И КОНФИДЕНЦИАЛЬНО").setBold().setFont(font));
        document.add(new Paragraph("Наноеву Алексею").setFont(font));
        document.add(new Paragraph("Дата: " + LocalDateTime.now()).setFont(font));
        document.add(new Paragraph("Компания: ООО Sidor-Pi...").setFont(font));
        document.add(new Paragraph("ЗП: 45.000").setFont(font));

        document.close();
    }

}

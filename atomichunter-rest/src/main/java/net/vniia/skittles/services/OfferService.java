package net.vniia.skittles.services;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class OfferService {

    public static final String OFFER_PATH = "offers/";

    @Value("classpath:/photo_2023-05-24_22-29-53.jpg")
    private Resource photo;
    @Value("classpath:/OpenSans.ttf")
    private Resource font;

    public void createPdf() throws IOException {
        this.createFolder();
        String path = OFFER_PATH + "offer1.pdf";

        FontProgram fontProgram = FontProgramFactory.createFont(font.getContentAsByteArray());
        PdfFont font = PdfFontFactory.createFont(fontProgram);

        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        ImageData data = ImageDataFactory.create(photo.getContentAsByteArray());
        Image img = new Image(data);


        document.add(img);
        document.add(new Paragraph("ЛИЧНО И КОНФИДЕНЦИАЛЬНО").setBold().setFont(font));
        document.add(new Paragraph("Наноеву Алексею").setFont(font));
        document.add(new Paragraph("Дата: " + LocalDateTime.now()).setFont(font));
        document.add(new Paragraph("Компания: ООО Sidor-Pi...").setFont(font));
        document.add(new Paragraph("ЗП: 45.000").setFont(font));

        document.close();
    }

    private void createFolder() {
        File directory = new File(OFFER_PATH);
        if (! directory.exists()){
            directory.mkdir();
        }
    }
}

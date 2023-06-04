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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class OfferService {

    public static final String OFFER_PATH = "offers/";

    @Value("classpath:/logo2.png")
    private Resource photo;
    @Value("classpath:/OpenSans.ttf")
    private Resource font;

    @Autowired
    private EmailService emailService;

    public String createPdf() throws IOException {
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
        img.scaleToFit(200f, 200f);
        float twoCol = 360f;
        float twoColumnWidth[] = {twoCol, twoCol};
        img.setRelativePosition(170f, 0, 200f, 0);


        document.add(img).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(new Paragraph("ЛИЧНО И КОНФИДЕНЦИАЛЬНО").setBold().setFont(font));
        document.add(new Paragraph("Наноеву Алексею").setFont(font));
        document.add(new Paragraph("Дата: " + LocalDate.now()).setFont(font));
        document.add(new Paragraph("Компания «AtomicHunter» хотела бы предложить Вам занять должность ... разработчика на следующих условиях:\n" +
                "Ваш ежемесячный оклад будет составлять ... рублей. Дата начала действия трудового договора – открытая дата.").setFont(font));
        document.add(new Paragraph("Вам будет установлен испытательный срок 3 (три) месяца, " +
                "по истечении которого Компания проведет Вашу аттестацию. " +
                "В дальнейшем аттестация будет проводиться регулярно и, по ее результатам, " +
                "может изменяться уровень компенсации и занимаемая Вами в Компании должность.").setFont(font));
        document.add(new Paragraph("Ваш вклад в развитие Компании будет оцениваться руководством по следующим критериям:").setFont(font));
        document.add(new Paragraph("1) Решение задач, входящих в непосредственную зону Вашей ответственности;").setFont(font));
        document.add(new Paragraph("2) Вклад в развитие Компании в целом.").setFont(font));

        Table twoColTable = new Table(twoColumnWidth);
        twoColTable.addCell(getBoldTitleCell("Директор").setFont(font));
        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell("Воппер Леха").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell("Сидоров Артемка").setFont(font));
        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return path;
    }

    static Cell getBoldTitleCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getNotBoldTitleCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    public void createPdfAndSendByEmail(String email) throws IOException, MessagingException {
        String path = this.createPdf();
        this.emailService.sendEmailWithAttachment(
                        email,
                "Оффер",
                "Здравствуйте! Компания Atomic Hunter высылает вам оффер",
                path
        );
    }

    private void createFolder() {
        File directory = new File(OFFER_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}

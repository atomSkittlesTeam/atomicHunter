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
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
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

        document.add(img);
        document.add(new Paragraph("ЛИЧНО И КОНФИДЕНЦИАЛЬНО").setBold().setFont(font));
        document.add(new Paragraph("Наноеву Алексею").setFont(font));
        document.add(new Paragraph("Дата: " + LocalDateTime.now()).setFont(font));
        document.add(new Paragraph("Компания «AtomicHunter» хотела бы предложить Вам занять должность ... разработчика на следующих условиях:\n" +
                "Ваш ежемесячный оклад будет составлять ... рублей. Дата начала действия трудового договора – открытая дата.").setFont(font));
        document.add(new Paragraph("Мы хотим предложить Вам работать с нами," +
                "на позиции Инженер-программист").setFont(font));
        document.add(new Paragraph("Вам будет установлен испытательный срок 3 (три) месяца, " +
                "по истечении которого Компания проведет Вашу аттестацию. " +
                "В дальнейшем аттестация будет проводиться регулярно и, по ее результатам, " +
                "может изменяться уровень компенсации и занимаемая Вами в Компании должность.").setFont(font));
        document.add(new Paragraph("Ваш вклад в развитие Компании будет оцениваться руководством по следующим критериям:").setFont(font));
        document.add(new Paragraph("1) Решение задач, входящих в непосредственную зону Вашей ответственности;").setFont(font));
        document.add(new Paragraph("2) Вклад в развитие Компании в целом.").setFont(font));

        document.close();

        return path;
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
        if (! directory.exists()) {
            directory.mkdir();
        }
    }
}

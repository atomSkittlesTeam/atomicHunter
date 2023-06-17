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
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.dto.VacancyWithVacancyRespondDto;
import net.vniia.skittles.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class ReportService {

    public static final String OFFER_PATH = "offers/";
    public static final String VACANCY_REPORT_PATH = "vacancyReports/";

    @Value("classpath:/logo2.png")
    private Resource photo;
    @Value("classpath:/OpenSans.ttf")
    private Resource font;

    @Autowired
    private EmailService emailService;

    public String createOfferReport(VacancyWithVacancyRespondDto vacancyWithRespondDto, User currentUser, UserDto currentHR)
            throws IOException {
        this.createFolder(OFFER_PATH);
        String path = OFFER_PATH + "offer_to_" + vacancyWithRespondDto.getVacancyRespond().getId() + "_respond.pdf";
        VacancyDto vacancy = vacancyWithRespondDto.getVacancy();

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
        document.add(new Paragraph(vacancyWithRespondDto.getVacancyRespond().getFullName()).setFont(font));
        document.add(new Paragraph("Дата: " + LocalDate.now()).setFont(font));

        document.add(new Paragraph(
                "Компания «Атомпродукт» хотела бы предложить Вам занять должность " + "ПОПРАВИТЬ"
                + " на следующих условиях: " +
                        "ВСТАВИТЬ УСЛОВИЯ"
                + "Дата начала действия трудового договора – открытая дата.").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph("Вам будет установлен испытательный срок 3 (три) месяца, " +
                "по истечении которого компания проведет Вашу аттестацию. " +
                "В дальнейшем аттестация будет проводиться регулярно, и по ее результатам " +
                "может изменяться уровень компенсации и занимаемая Вами в компании должность.").setFont(font));
        document.add(new Paragraph("Ваш вклад в развитие Компании будет оцениваться руководством по следующим критериям:").setFont(font));
        document.add(new Paragraph("1) Решение задач, входящих в непосредственную зону Вашей ответственности;").setFont(font));
        document.add(new Paragraph("2) Вклад в развитие Компании в целом.").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));
        document.add(new Paragraph("").setFont(font));


        Table twoColTable = new Table(twoColumnWidth);
        twoColTable.addCell(getBoldTitleCell("Директор").setFont(font));
        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell(currentUser.getFullName()).setFont(font));
        twoColTable.addCell(getNotBoldTitleCell(currentHR.getFullName()).setFont(font));
        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return path;
    }

    public String createVacancyReport(VacancyDto vacancyDto)
            throws IOException {
        this.createFolder(VACANCY_REPORT_PATH);
        String path = VACANCY_REPORT_PATH + "report_of_" + vacancyDto.getId() + "_vacancy.pdf";

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
        document.add(new Paragraph("Вакансия").setBold().setFont(font));
//        document.add(new Paragraph(vacancyWithRespondDto.getVacancyRespond().getFullName()).setFont(font));
//        document.add(new Paragraph("Дата: " + LocalDate.now()).setFont(font));
//
//        document.add(new Paragraph(
//                "Компания «Атомпродукт» хотела бы предложить Вам занять должность " + "ПОПРАВИТЬ"
//                        + " на следующих условиях: " +
//                        "ВСТАВИТЬ УСЛОВИЯ"
//                        + "Дата начала действия трудового договора – открытая дата.").setFont(font)
//                .setTextAlignment(TextAlignment.JUSTIFIED));
//        document.add(new Paragraph("Вам будет установлен испытательный срок 3 (три) месяца, " +
//                "по истечении которого компания проведет Вашу аттестацию. " +
//                "В дальнейшем аттестация будет проводиться регулярно, и по ее результатам " +
//                "может изменяться уровень компенсации и занимаемая Вами в компании должность.").setFont(font));
//        document.add(new Paragraph("Ваш вклад в развитие Компании будет оцениваться руководством по следующим критериям:").setFont(font));
//        document.add(new Paragraph("1) Решение задач, входящих в непосредственную зону Вашей ответственности;").setFont(font));
//        document.add(new Paragraph("2) Вклад в развитие Компании в целом.").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//        document.add(new Paragraph("").setFont(font));
//
//
//        Table twoColTable = new Table(twoColumnWidth);
//        twoColTable.addCell(getBoldTitleCell("Директор").setFont(font));
//        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
//        twoColTable.addCell(getNotBoldTitleCell(currentUser.getFullName()).setFont(font));
//        twoColTable.addCell(getNotBoldTitleCell(currentHR.getFullName()).setFont(font));
//        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return "report_of_" + vacancyDto.getId() + "_vacancy.pdf";
    }

    static Cell getBoldTitleCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBold().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getNotBoldTitleCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    public void createPdfAndSendByEmail(VacancyWithVacancyRespondDto vacancyWithVacancyRespondDto,
                                        User currentUser, UserDto currentHR)
            throws IOException, MessagingException {
        String path = this.createOfferReport(vacancyWithVacancyRespondDto, currentUser, currentHR);
        this.emailService.sendEmailWithAttachment(
                        vacancyWithVacancyRespondDto.getVacancyRespond().getEmail(),
                "Оффер",
                "Здравствуйте! Компания Atomic Hunter высылает вам оффер",
                path
        );
    }

    private void createFolder(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}

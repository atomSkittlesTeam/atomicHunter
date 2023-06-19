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
import net.vniia.skittles.dto.CompetenceWeightScoreFullDto;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        document.add(new Paragraph("Кому: " + vacancyWithRespondDto.getVacancyRespond().getFullName()).setFont(font));
        document.add(new Paragraph("Дата: " + LocalDate.now()).setFont(font));

        document.add(new Paragraph(
                "Компания «Атомпродукт» хотела бы предложить Вам занять должность (" +
                        vacancyWithRespondDto.getVacancy().getPosition().getName()
                        + ") на следующих условиях: " +
                        vacancyWithRespondDto.getVacancy().getResponsibilities()).setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph("Дата начала действия трудового договора – открытая дата.").setFont(font).setTextAlignment(TextAlignment.JUSTIFIED));

        document.add(new Paragraph("Вам будет установлен испытательный срок 3 (три) месяца, " +
                "по истечении которого компания проведет Вашу аттестацию. " +
                "В дальнейшем аттестация будет проводиться регулярно, и по ее результатам " +
                "может изменяться уровень компенсации и занимаемая Вами в компании должность.").setFont(font));
        document.add(new Paragraph("Ваш вклад в развитие Компании будет оцениваться руководством по следующим критериям:").setFont(font));
        document.add(new Paragraph("1) Решение задач, входящих в непосредственную зону Вашей ответственности;").setFont(font));
        document.add(new Paragraph("2) Вклад в развитие Компании в целом.").setFont(font));
        document.add(new Paragraph("Для заключения трудового договора свяжитес с нашей службой HR." +
                "При заключении договора при себе иметь мед. обследование и паспорт.").setFont(font));
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
        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell(currentHR.getFullName()).setFont(font));
        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return path;
    }

    public String createDeclineOfferReport(VacancyWithVacancyRespondDto vacancyWithRespondDto, User currentUser, UserDto currentHR, List<CompetenceWeightScoreFullDto> competenceWeightScoreFullDtos)
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
                "Спасибо что выбрали нашу компанию!").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph(
                "К сожалению мы вынуждены Вам отказать, на неопределнный срок. Возможно мы вам еще перезвоним!").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));

        document.add(new Paragraph("").setFont(font));

        document.add(new Paragraph(
                "Вы не прошли по следующим позициям: ").setFont(font).setBold()
                .setTextAlignment(TextAlignment.JUSTIFIED));

        competenceWeightScoreFullDtos.forEach( comp -> {
            document.add(new Paragraph(
                    comp.getCompetence().getName() + " " + "Оценка: " + comp.getScore() ).setFont(font)
                    .setTextAlignment(TextAlignment.JUSTIFIED));
        } );

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
        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell(currentHR.getFullName()).setFont(font));
        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return path;
    }

    public String createAlternativeOfferReport(VacancyWithVacancyRespondDto vacancyWithRespondDto,
                                               User currentUser,
                                               UserDto currentHR, String goodVacancy)
            throws IOException {
        this.createFolder(OFFER_PATH);
        String path = OFFER_PATH + "offer_to_" + vacancyWithRespondDto.getVacancyRespond().getId() + "_respond.pdf";


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
        document.add(new Paragraph("Кому: " + vacancyWithRespondDto.getVacancyRespond().getFullName()).setFont(font));
        document.add(new Paragraph("Дата: " + LocalDate.now()).setFont(font));

        document.add(new Paragraph(
                "Спасибо что выбрали нашу компанию!").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph(
                "К сожалению мы не может предложить Вам вакансию на которую вы проходили собеседование").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph(
                "Однако, по результам собеседования, мы можем вам перезвонить и предложить что-то новое ").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));

        if (goodVacancy != null) {
            document.add(new Paragraph(
                    "Сейчас мы готовы педложить вам следующие вакансии: " + goodVacancy).setFont(font)
                    .setTextAlignment(TextAlignment.JUSTIFIED));
        }

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
        twoColTable.addCell(getBoldTitleCell("Отдел кадров").setFont(font));
        twoColTable.addCell(getNotBoldTitleCell(currentHR.getFullName()).setFont(font));
        document.add(twoColTable.setMarginBottom(12f));

        document.close();

        return path;
    }

    public String createVacancyReport(VacancyDto vacancyDto, String additionalInformation)
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
        document.add(new Paragraph("Требуется " + vacancyDto.getName())
                .setBold().setFont(font).setTextAlignment(TextAlignment.CENTER)
                .setFontSize(22));

        document.add(new Paragraph("Компания: ").setBold().setFont(font));
        document.add(new Paragraph("Крупное производственное предприятие «Атомпродукт» - " +
                "одна из лидирующих компаний в корпорации РосАтом.").setFont(font)
                .setTextAlignment(TextAlignment.JUSTIFIED));
        document.add(new Paragraph("Обязанности: ").setBold().setFont(font));
        document.add(new Paragraph(vacancyDto.getResponsibilities()).setFont(font));
        document.add(new Paragraph("Требования:").setBold().setFont(font));
        document.add(new Paragraph(vacancyDto.getRequirements() +
                " Оценка ваших навыков будет проводиться по следующему набору" +
                " компетенций: " +
                vacancyDto.getCompetenceWeight().stream().map(e ->
                        e.getCompetence().getName()).collect(Collectors.joining(", ")))
                .setFont(font));
        document.add(new Paragraph("Условия:").setBold().setFont(font));
        document.add(new Paragraph(vacancyDto.getConditions()).setFont(font));
        if (additionalInformation != null && !additionalInformation.equals("")) {
            document.add(new Paragraph("Дополнительная информация:").setBold().setFont(font));
            document.add(new Paragraph(additionalInformation).setFont(font));
        }

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
                                        User currentUser, UserDto currentHR, String mode, List<CompetenceWeightScoreFullDto> competenceWeightScoreFullDtos, String goodVacancy)
            throws IOException, MessagingException {
        String path = "";
        switch (mode) {
            case "approve" -> {
                path = this.createOfferReport(vacancyWithVacancyRespondDto, currentUser, currentHR);

            }
            case "decline" -> {
                path = this.createDeclineOfferReport(vacancyWithVacancyRespondDto, currentUser, currentHR, competenceWeightScoreFullDtos);
            }
            case "alternative" -> {
                path = this.createAlternativeOfferReport(vacancyWithVacancyRespondDto, currentUser, currentHR, goodVacancy);
            }

        }
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

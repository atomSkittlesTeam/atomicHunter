import { Component, EventEmitter, Input, Output } from "@angular/core";
import { VacancyService } from "../../services/vacancy.service";
import { CompetenceService } from "../../services/competence.service";
import { PositionService } from "../../services/position.service";
import { MessageService } from "primeng/api";

@Component({
  selector: 'app-vacancy-respond-resume',
  templateUrl: './vacancy-respond-resume.component.html',
  styleUrls: ['./vacancy-respond-resume.component.scss']
})
export class VacancyRespondResumeComponent {
  constructor(private vacancyService: VacancyService,
              private competenceService: CompetenceService,
              private positionService: PositionService,
              public messageService: MessageService) {
  }

  @Input("pdfResume") pdfResume: string = "";
  @Input("showPdfResume") showPdfResume: boolean = false;
  @Output("hidePdfResume") hidePdfResume = new EventEmitter<boolean>();

  header: string = "Резюме";

  async ngOnInit() {
    // window.open(this.pdfResume, 'help');
  }
}

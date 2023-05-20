import {Component} from '@angular/core';
import {RequestService} from "../../services/request.service";
import {formatDate} from "@angular/common";
import { AnalyticsService } from "../../services/analytics.service";

@Component({
    selector: 'app-analytics',
    templateUrl: './analytics.component.html',
    styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent {

    display: boolean = false;
    displayStat: boolean = false;
    displayStats: boolean = false;
    dialogHeader: string;
    data: any;
    dataAll: any;
    analyticsMap: Map<string, number>;
    analyticsAllMap: Map<string, number>;

    options = {
        scales: {
            y: {
                title: {
                    display: true,
                    text: 'Количество позиций заказов, ШТ'
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Номер заказа'
                }
            }
        }
    }

    constructor(public analyticsService: AnalyticsService) {
    }

    async ngOnInit() {
        await this.showDialogStat();
    }

    async showDialogStat() {
            const almostMap = await this.analyticsService.getAnalyticsPositionCountByNumber();
            this.analyticsMap = new Map(Object.entries(almostMap));
            this.data = {
                labels: Array.from(this.analyticsMap.keys()),
                datasets: [
                    {
                        data: Array.from(this.analyticsMap.values()),
                        backgroundColor: [
                            "#42A5F5",
                            "#66BB6A",
                            "#FFA726",
                            "#ff2672",
                        ],
                        hoverBackgroundColor: [
                            "#64B5F6",
                            "#81C784",
                            "#FFB74D",
                            "#ff2672"
                        ]
                    }
                ]
            };
            this.displayStat = true;
            this.dialogHeader = "code";
        }

}

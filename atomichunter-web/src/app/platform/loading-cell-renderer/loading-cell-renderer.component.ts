import { Component } from '@angular/core';
import {ILoadingCellRendererParams} from "ag-grid-community";
import {ILoadingCellRendererAngularComp} from "ag-grid-angular";

@Component({
  selector: 'app-loading-cell-renderer',
  templateUrl: './loading-cell-renderer.component.html',
  styleUrls: ['./loading-cell-renderer.component.scss']
})
export class LoadingCellRendererComponent implements ILoadingCellRendererAngularComp {


  public params!: ILoadingCellRendererParams & { loadingMessage: string };

  agInit(
      params: ILoadingCellRendererParams & { loadingMessage: string }
  ): void {
    this.params = params;
  }
}

import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';

@Injectable()
export class HttpHelperInterceptor implements HttpInterceptor {

  constructor(private messageService: MessageService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (localStorage.getItem("AUTH")) {
      request = request.clone({
        setHeaders: {
          Authorization: `Basic ${localStorage.getItem("AUTH")}`
        }
      });
    }
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          let errorMsg = '';
          if (error.error instanceof ErrorEvent) {
              // 'This is client side error';
              errorMsg = `Error: ${error.error.message}`;
          } else {
              // 'This is server side error'
              if (error instanceof HttpErrorResponse) {
                  this.messageService.add({
                    severity: 'error',
                    summary: `Ошибка код HTTP: ${error.status}`,
                    detail:  `Произошла ошибка на сервере: ${error.message}`
                  });
              }
              errorMsg = `Error Code: ${error.status},  Message: ${error.message}`;
          }
          console.log(errorMsg);
          return throwError(() => error);
        })
      );
  }
}

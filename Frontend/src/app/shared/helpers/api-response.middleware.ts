import { HttpErrorResponse } from '@angular/common/http';
import { catchError, map, OperatorFunction, throwError } from 'rxjs';
import { ApiResponse } from '../models/types';

export function extractApiData<T>(): OperatorFunction<ApiResponse<T>, T /* input, output */> {
  return source =>
    source.pipe(
      map(response => response.data),
      catchError(handleError)
    );
}

export function handleError(error: HttpErrorResponse) {
  let errorMessage = 'An error occurred';

  if (error.error instanceof ErrorEvent) {
    errorMessage = error.error.message;
  } else {
    errorMessage = error.error?.message || error.statusText || errorMessage;
  }

  return throwError(() => new Error(errorMessage));
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import {
  UserRequest,
  LoginRequest,
  ForgetPasswordRequest,
  ResetPasswordRequest,
  AuthResponse,
  ApiResponse
} from '../models/auth.model';

import { environment } from '../../../../environments/environment';
import { StorageService } from '../../../core/services/storage.service';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private apiUrl = environment.apiUrl + "/auth";

  constructor(private http: HttpClient, private storageService: StorageService) { }

  signup(userRequest: UserRequest): Observable<AuthResponse> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/signup`, userRequest)
      .pipe(
        map(response => {
          if (response.data.token) {
            this.storageService.set('token', response.data.token);
          }
          return response.data;
        }),
        catchError(this.handleError)
      );
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        map(response => {
          if (response.data.token) {
            this.storageService.set('token', response.data.token);
          }
          return response.data;
        }),
        catchError(this.handleError)
      );
  }

  forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(`${this.apiUrl}/forget-password`, forgetPasswordRequest)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  resetPassword(resetPasswordRequest: ResetPasswordRequest): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(`${this.apiUrl}/reset-password`, resetPasswordRequest)
      .pipe(
        map(response => response.data),
        catchError(this.handleError)
      );
  }

  logout(): void {
    // TODO: API Call
    this.storageService.remove('token');
  }

  isAuthenticated(): boolean {
    // TODO: API Call
    return this.storageService.get('token') !== null;
  }

  getToken(): string | null {
    return this.storageService.get('token');
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error?.message || error.statusText || errorMessage;
    }
    return throwError(() => new Error(errorMessage));
  }
}


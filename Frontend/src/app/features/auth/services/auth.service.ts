import { Injectable } from '@angular/core';
import { AuthResponse, ForgetPasswordRequest, LoginRequest, ResetPasswordRequest, UserRequest } from '../models/auth.model';
import { catchError, map, Observable, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ApiResponse } from '../../../shared/models/types';
import { StorageService } from '../../../shared/services/storage.service';
import { environment } from '../../../environments/environment.prod';
import { extractApiData } from '../../../shared/helpers/api-response.middleware';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(private http: HttpClient, private storageService: StorageService) { }

  apiUrl: string = environment.apiUrl + "/auth";

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, loginRequest)
      .pipe(extractApiData<AuthResponse>());
  }


  signup(userRequest: UserRequest): Observable<AuthResponse> {
    return this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/signup`, userRequest)
      .pipe(extractApiData<AuthResponse>());
  }


  forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(`${this.apiUrl}/forget-password`, forgetPasswordRequest)
      .pipe(extractApiData<string>());
  }

  resetPassword(resetPasswordRequest: ResetPasswordRequest): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(`${this.apiUrl}/reset-password`, resetPasswordRequest)
      .pipe(extractApiData<string>());
  }

}

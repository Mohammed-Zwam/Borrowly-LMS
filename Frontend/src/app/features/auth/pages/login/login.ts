import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';
import { HeroSection } from '../../components/hero-section/hero-section';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    ButtonModule,
    InputTextModule,
    CheckboxModule,
    ToastModule,
    HeroSection
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  showPassword = false;
  rememberMe = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.loadRememberedEmail();
  }

  initializeForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      rememberMe: [false]
    });
  }

  loadRememberedEmail(): void {
    const rememberedEmail = localStorage.getItem('rememberedEmail');
    if (rememberedEmail) {
      this.loginForm.patchValue({
        email: rememberedEmail,
        rememberMe: true
      });
      this.rememberMe = true;
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  getEmailErrors(): string[] {
    const errors: string[] = [];
    const control = this.loginForm.get('email');
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push('Email is required');
      if (control.errors['email']) errors.push('Please enter a valid email address');
    }
    return errors;
  }

  getPasswordErrors(): string[] {
    const errors: string[] = [];
    const control = this.loginForm.get('password');
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push('Password is required');
      if (control.errors['minlength']) errors.push('Password must be at least 8 characters');
    }
    return errors;
  }


  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Please fill all required fields correctly'
      });
      return;
    }

    this.loading = true;

    const loginRequest: LoginRequest = {
      email: this.loginForm.get('email')?.value,
      password: this.loginForm.get('password')?.value
    };

    // Handle remember me
    if (this.loginForm.get('rememberMe')?.value) {
      localStorage.setItem('rememberedEmail', loginRequest.email);
    } else {
      localStorage.removeItem('rememberedEmail');
    }

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.loading = false;
        console.log(response)
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Login successful!'
        });
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      },

      error: (error) => {
        this.loading = false;
        console.log(error)

        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: error.message || 'Invalid credentials'
        });
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.loginForm.get(fieldName);
    if (!field || !field.errors || !field.touched) {
      return '';
    }

    if (field.errors['required']) return `${fieldName} is required`;
    if (field.errors['email']) return 'Please enter a valid email';
    if (field.errors['minlength']) return `${fieldName} is too short`;

    return 'Invalid field';
  }
}


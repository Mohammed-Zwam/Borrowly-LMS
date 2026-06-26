import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { ForgetPasswordRequest } from '../../models/auth.model';
import { HeroSection } from '../../components/hero-section/hero-section';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    ButtonModule,
    InputTextModule,
    HeroSection
  ],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css'
})
export class ForgotPasswordComponent {
  forgotForm!: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.initializeForm();
  }

  initializeForm(): void {
    this.forgotForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  private showAlert(icon: 'success' | 'error', title: string, text: string): void {
    void Swal.fire({
      icon,
      title,
      text,
      confirmButtonColor: '#2563eb',
      confirmButtonText: 'Okay'
    });
  }

  getEmailErrors(): string[] {
    const errors: string[] = [];
    const control = this.forgotForm.get('email');
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push('Email is required');
      if (control.errors['email']) errors.push('Please enter a valid email address');
    }
    return errors;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.forgotForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  onSubmit(): void {
    if (this.forgotForm.invalid) {
      this.showAlert('error', 'Error', 'Please enter a valid email address');
      return;
    }

    this.loading = true;

    const forgetPasswordRequest: ForgetPasswordRequest = {
      email: this.forgotForm.get('email')?.value
    };

    this.authService.forgetPassword(forgetPasswordRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.submitted = true;
        this.showAlert('success', 'Success', 'Password reset link has been sent to your email!');
      },
      error: (error) => {
        this.loading = false;
        this.showAlert('error', 'Error', error?.message || 'Failed to send reset link');
      }
    });
  }


  getFieldError(fieldName: string): string {
    const field = this.forgotForm.get(fieldName);
    if (!field || !field.errors || !field.touched) {
      return '';
    }

    if (field.errors['required']) return `${fieldName} is required`;
    if (field.errors['email']) return 'Please enter a valid email';

    return 'Invalid field';
  }
}


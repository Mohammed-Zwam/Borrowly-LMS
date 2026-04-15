import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../services/auth.service';
import { UserRequest } from '../../models/auth.model';
import { HeroSection } from '../../components/hero-section/hero-section';

// Custom validator for password confirmation
function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');

  if (!password || !confirmPassword) {
    return null;
  }

  return password.value === confirmPassword.value ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    ButtonModule,
    InputTextModule,
    PasswordModule,
    CheckboxModule,
    ToastModule,
    HeroSection
  ],
  templateUrl: './signup.html',
  styleUrls: ['./signup.css']
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;
  loading = false;
  showPassword = false;
  showConfirmPassword = false;
  passwordStrength = 0;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.signupForm.get('password')?.valueChanges.subscribe(() => {
      this.calculatePasswordStrength();
    });
  }

  initializeForm(): void {
    this.signupForm = this.formBuilder.group(
      {
        name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
        email: ['', [Validators.required, Validators.email]],
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
        phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{7,15}$/)]],
        password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
        confirmPassword: ['', [Validators.required]],
        agreeToTerms: [false, [Validators.requiredTrue]]
      },
      { validators: passwordMatchValidator }
    );
  }

  calculatePasswordStrength(): void {
    const password = this.signupForm.get('password')?.value || '';
    let strength = 0;

    if (password.length >= 8) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++;

    this.passwordStrength = strength;
  }

  getPasswordStrengthLabel(): string {
    const labels = ['', 'Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
    return labels[this.passwordStrength] || '';
  }

  getPasswordStrengthColor(): string {
    const colors = ['#767586', '#ba1a1a', '#f57c00', '#fbc02d', '#7cb342', '#2196f3'];
    return colors[this.passwordStrength] || '#767586';
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  getFieldErrors(fieldName: string): string[] {
    const errors: string[] = [];
    const control = this.signupForm.get(fieldName);
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push(`${this.formatFieldName(fieldName)} is required`);
      if (control.errors['email']) errors.push('Please enter a valid email address');
      if (control.errors['minlength']) errors.push(`${this.formatFieldName(fieldName)} must be at least ${control.errors['minlength'].requiredLength} characters`);
      if (control.errors['maxlength']) errors.push(`${this.formatFieldName(fieldName)} cannot exceed ${control.errors['maxlength'].requiredLength} characters`);
      if (control.errors['pattern']) {
        errors.push(`${this.formatFieldName(fieldName)} format is invalid`);
      }
    }
    return errors;
  }

  getPasswordMismatchError(): string | null {
    const formErrors = this.signupForm.errors;
    if (formErrors && formErrors['passwordMismatch'] && (this.signupForm.get('confirmPassword')?.dirty || this.signupForm.get('confirmPassword')?.touched)) {
      return 'Passwords do not match';
    }
    return null;
  }

  getConfirmPasswordErrors(): string[] {
    const errors: string[] = [];
    const control = this.signupForm.get('confirmPassword');
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push('Please confirm your password');
    }
    const mismatchError = this.getPasswordMismatchError();
    if (mismatchError) errors.push(mismatchError);
    return errors;
  }

  getTermsErrors(): string[] {
    const errors: string[] = [];
    const control = this.signupForm.get('agreeToTerms');
    if (control && control.errors && (control.dirty || control.touched)) {
      if (control.errors['required']) errors.push('You must agree to the terms and conditions');
    }
    return errors;
  }

  formatFieldName(fieldName: string): string {
    return fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
  }


  onSubmit(): void {
    if (this.signupForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Please fill all required fields correctly'
      });
      return;
    }

    this.loading = true;

    const userRequest: UserRequest = {
      name: this.signupForm.get('name')?.value,
      email: this.signupForm.get('email')?.value,
      username: this.signupForm.get('username')?.value,
      phone: this.signupForm.get('phone')?.value,
      password: this.signupForm.get('password')?.value
    };

    this.authService.signup(userRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Account created successfully!'
        });
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      },
      error: (error) => {
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: error.message || 'Failed to create account'
        });
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.signupForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.signupForm.get(fieldName);
    if (!field || !field.errors || !field.touched) {
      return '';
    }

    if (field.errors['required']) return `${fieldName} is required`;
    if (field.errors['email']) return 'Please enter a valid email';
    if (field.errors['minlength']) return `${fieldName} is too short`;
    if (field.errors['maxlength']) return `${fieldName} is too long`;
    if (field.errors['pattern']) return `${fieldName} is invalid`;

    return 'Invalid field';
  }

  getFormError(): string {
    if (this.signupForm.errors?.['passwordMismatch']) {
      return 'Passwords do not match';
    }
    return '';
  }
}


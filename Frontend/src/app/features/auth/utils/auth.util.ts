export class PasswordValidator {

  static calculateStrength(password: string): number {
    if (!password) return 0;

    let strength = 0;

    // Length check
    if (password.length >= 8) strength++;

    // Lowercase check
    if (/[a-z]/.test(password)) strength++;

    // Uppercase check
    if (/[A-Z]/.test(password)) strength++;

    // Number check
    if (/[0-9]/.test(password)) strength++;

    // Special character check
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++;

    return strength;
  }

  static getStrengthLabel(strength: number): string {
    const labels = ['', 'Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
    return labels[strength] || '';
  }


  static getStrengthColor(strength: number): string {
    const colors = ['#767586', '#ba1a1a', '#f57c00', '#fbc02d', '#7cb342', '#2196f3'];
    return colors[strength] || '#767586';
  }


  static validatePassword(password: string): string[] {
    const errors: string[] = [];

    if (!password) {
      errors.push('Password is required');
      return errors;
    }

    if (password.length < 8) {
      errors.push('Password must be at least 8 characters');
    }

    if (password.length > 20) {
      errors.push('Password must not exceed 20 characters');
    }

    if (!/[a-z]/.test(password)) {
      errors.push('Password must contain lowercase letters');
    }

    if (!/[A-Z]/.test(password)) {
      errors.push('Password must contain uppercase letters');
    }

    if (!/[0-9]/.test(password)) {
      errors.push('Password must contain numbers');
    }

    return errors;
  }
}


export class EmailValidator {
  private static emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  static isValid(email: string): boolean {
    return this.emailRegex.test(email);
  }
}


export class PhoneValidator {
  private static phoneRegex = /^\+?[0-9]{7,15}$/;

  static isValid(phone: string): boolean {
    // Remove spaces and hyphens
    const cleaned = phone.replace(/[\s-]/g, '');
    return this.phoneRegex.test(cleaned);
  }

  static format(phone: string): string {
    const cleaned = phone.replace(/\D/g, '');
    if (cleaned.length === 10) {
      return `(${cleaned.slice(0, 3)}) ${cleaned.slice(3, 6)}-${cleaned.slice(6)}`;
    }
    return phone;
  }
}
export class ErrorMessageUtil {

  static getMessage(error: any): string {
    if (typeof error === 'string') {
      return error;
    }

    if (error.error?.message) {
      return error.error.message;
    }

    if (error.message) {
      return error.message;
    }

    if (error.status === 401) {
      return 'Invalid credentials';
    }

    if (error.status === 404) {
      return 'Resource not found';
    }

    if (error.status === 500) {
      return 'Server error. Please try again later';
    }

    return 'An error occurred. Please try again';
  }
}


export class TokenUtil {
  private static readonly TOKEN_KEY = 'token';
  private static readonly USER_KEY = 'user';

  static saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  static hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  static saveUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }


  static getUser(): any {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }


  static removeUser(): void {
    localStorage.removeItem(this.USER_KEY);
  }


  static decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      return null;
    }
  }

  static isTokenExpired(token: string): boolean {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) {
      return true;
    }

    return decoded.exp * 1000 < Date.now();
  }
}
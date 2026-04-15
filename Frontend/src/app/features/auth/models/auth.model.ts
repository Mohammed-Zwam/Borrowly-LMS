export interface UserRequest {
  email: string;
  password: string;
  phone: string;
  name: string;
  username: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface ForgetPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  password: string;
}

export interface UserResponse {
  id: number;
  email: string;
  name: string;
  username: string;
  phone: string;
  role: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  title: string;
  user: UserResponse;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}


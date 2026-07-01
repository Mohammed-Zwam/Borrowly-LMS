
import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Signup } from './pages/signup/signup';
import { ForgotPassword } from './pages/forget-password/forgot-password';
import { ResetPassword } from './pages/reset-password/reset-password';


export const authRoutes: Routes = [
  {
    path: 'signup',
    component: Signup,
    data: { title: 'Signup' }
  },
  {
    path: 'login',
    component: Login,
    data: { title: 'Login' }
  },
  {
    path: 'forgot-password',
    component: ForgotPassword,
    data: { title: 'Forgot Password' }
  },
  {
    path: 'reset-password',
    component: ResetPassword,
    data: { title: 'Reset Password' }
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];


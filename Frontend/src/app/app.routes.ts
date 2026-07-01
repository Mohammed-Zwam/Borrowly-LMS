import { Routes } from '@angular/router';
import { authRoutes } from './features/auth/auth.routes';
import { AdminLayout } from './features/admin/layouts/admin-layout/admin-layout';
import { adminRoutes } from './features/admin/admin.routes';



export const routes: Routes = [
  {
    path: '',
    children: authRoutes
  },
  {
    path: '',
    component: AdminLayout,
    children: adminRoutes
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];

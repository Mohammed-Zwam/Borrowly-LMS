import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Books } from './pages/books/books';


export const adminRoutes: Routes = [
  {
    path: 'dashboard',
    component: Dashboard,
    data: { title: 'Dashboard' }
  },
  {
    path: 'books',
    component: Books,
    data: { title: 'Manage Books' }
  }
];


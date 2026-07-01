import { RouterLink, RouterLinkActive, ɵEmptyOutletComponent } from '@angular/router';
import { Component } from '@angular/core';
import { Logo } from '../../../../shared/components/logo/logo';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, Logo],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar {

}

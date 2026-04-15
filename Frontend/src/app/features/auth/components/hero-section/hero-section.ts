import { Component, Input } from '@angular/core';
import { Logo } from '../../../shared/components/logo/logo';

@Component({
  selector: 'app-hero-section',
  templateUrl: './hero-section.html',
  styleUrl: './hero-section.css',
  imports: [Logo]
})
export class HeroSection {
  @Input() imgSrc: string = '';
  @Input() title: string = '';
  @Input() description: string = '';
}

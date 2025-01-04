import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { MsalModule, MsalService, MsalBroadcastService } from '@azure/msal-angular';
import { PublicClientApplication } from '@azure/msal-browser';
import { msalConfig } from './auth/msal.config';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, MsalModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'TiendaWeb';
  private msalInstance: PublicClientApplication;

  constructor(
    private msalService: MsalService,
    private msalBroadcastService: MsalBroadcastService
  ) {
    this.msalInstance = new PublicClientApplication(msalConfig);
  }

  async ngOnInit() {
    try {
      // Inicializar MSAL
      await this.msalInstance.initialize();
      
      // Manejar la redirección después de la inicialización
      const response = await this.msalInstance.handleRedirectPromise();
      if (response) {
        this.msalInstance.setActiveAccount(response.account);
      }
    } catch (error) {
      console.error('Error initializing MSAL:', error);
    }
  }
}
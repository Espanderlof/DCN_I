import { Component, OnInit, OnDestroy, inject, PLATFORM_ID } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { MsalService, MsalBroadcastService } from '@azure/msal-angular';
import { InteractionStatus } from '@azure/msal-browser';
import { Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'TiendaWeb';
  isIframe = false;
  private readonly _destroying$ = new Subject<void>();
  private readonly platformId = inject(PLATFORM_ID);

  constructor(
    private msalService: MsalService,
    private msalBroadcastService: MsalBroadcastService
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.isIframe = window !== window.parent && !window.opener;

      this.msalService.initialize().subscribe({
        next: () => {
          console.log('MSAL initialization successful');
          this.msalService.handleRedirectObservable().subscribe();
        },
        error: (error) => {
          console.error('MSAL initialization failed', error);
        }
      });

      this.msalBroadcastService.inProgress$
        .pipe(
          filter((status: InteractionStatus) => status === InteractionStatus.None),
          takeUntil(this._destroying$)
        )
        .subscribe(() => {
          this.checkAndSetActiveAccount();
        });
    }
  }

  checkAndSetActiveAccount() {
    if (isPlatformBrowser(this.platformId)) {
      let activeAccount = this.msalService.instance.getActiveAccount();

      if (!activeAccount && this.msalService.instance.getAllAccounts().length > 0) {
        let accounts = this.msalService.instance.getAllAccounts();
        this.msalService.instance.setActiveAccount(accounts[0]);
      }
    }
  }

  ngOnDestroy(): void {
    this._destroying$.next(undefined);
    this._destroying$.complete();
  }
}
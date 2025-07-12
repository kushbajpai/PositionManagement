import { Component } from '@angular/core';
import { TransactionFormComponent } from './transaction-form/transaction-form';
import { PositionGridComponent } from './position-grid/position-grid';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    MatToolbarModule,
    // TransactionFormComponent,
    // PositionGridComponent,
    RouterModule
  ],
  template: `
    <mat-toolbar color="primary">Position Management App</mat-toolbar>
    <nav>
      <a routerLink="/form">Transaction Form</a> |
      <a routerLink="/grid">Position Grid</a>
    </nav>
    <router-outlet></router-outlet>
    <div style="display: flex; flex-direction: column; gap: 24px; padding: 24px;">
      <!-- <app-transaction-form></app-transaction-form>
      <app-position-grid></app-position-grid> -->
      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {}


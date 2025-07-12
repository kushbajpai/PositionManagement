import { Routes } from '@angular/router';
import { TransactionFormComponent } from './transaction-form/transaction-form';
import { PositionGridComponent } from './position-grid/position-grid';

export const routes: Routes = [
  { path: '', redirectTo: 'form', pathMatch: 'full' },
  { path: 'form', component: TransactionFormComponent },
  { path: 'grid', component: PositionGridComponent }
];

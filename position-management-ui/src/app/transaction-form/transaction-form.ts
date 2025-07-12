import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  template: `
    <mat-card>
      <h2>Submit Transaction</h2>
      <form #txnForm="ngForm" (ngSubmit)="submitTransaction(txnForm)">
        <mat-form-field class="full-width">
          <input matInput name="tradeId" [(ngModel)]="transaction.tradeId" placeholder="Trade ID" required type="number" min="1" />
        </mat-form-field>

        <mat-form-field class="full-width">
          <input matInput name="version" [(ngModel)]="transaction.version" placeholder="Version" required type="number" min="1" />
        </mat-form-field>

        <mat-form-field class="full-width">
          <input matInput name="securityCode" [(ngModel)]="transaction.securityCode" placeholder="Security Code" required />
        </mat-form-field>

        <mat-form-field class="full-width">
          <input matInput name="quantity" [(ngModel)]="transaction.quantity" placeholder="Quantity" required type="number" min="1" />
        </mat-form-field>

        <mat-form-field class="full-width">
          <mat-select name="action" [(ngModel)]="transaction.action" placeholder="Action" required>
            <mat-option value="INSERT">INSERT</mat-option>
            <mat-option value="UPDATE">UPDATE</mat-option>
            <mat-option value="CANCEL">CANCEL</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field class="full-width">
          <mat-select name="buySell" [(ngModel)]="transaction.buySell" placeholder="Buy/Sell" required>
            <mat-option value="BUY">BUY</mat-option>
            <mat-option value="SELL">SELL</mat-option>
          </mat-select>
        </mat-form-field>

        <button mat-raised-button color="primary" type="submit" [disabled]="!txnForm.valid">Submit</button>
      </form>
    </mat-card>
  `,
  styles: [`
    .full-width {
      width: 100%;
      margin-bottom: 12px;
    }
  `]
})
export class TransactionFormComponent {
  transaction: any = {};

  constructor(private http: HttpClient) {}

  submitTransaction(form: NgForm) {
    const params = new HttpParams({ fromObject: this.transaction });
    console.log('Submitting transaction:', this.transaction);
    this.http.post('http://localhost:8080/api/transaction', {}, { params })
      .subscribe(() => {
        alert('Transaction submitted');
        form.resetForm();
      });
  }
}

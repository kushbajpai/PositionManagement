import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-position-grid',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule],
  template: `
    <mat-card>
      <h2>Current Positions</h2>
      <table mat-table [dataSource]="rows" class="mat-elevation-z1" style="width: 100%;">
        <ng-container matColumnDef="security">
          <th mat-header-cell *matHeaderCellDef>Security Code</th>
          <td mat-cell *matCellDef="let row">{{ row.security }}</td>
        </ng-container>

        <ng-container matColumnDef="quantity">
          <th mat-header-cell *matHeaderCellDef>Quantity</th>
          <td mat-cell *matCellDef="let row">{{ row.quantity }}</td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="['security', 'quantity']"></tr>
        <tr mat-row *matRowDef="let row; columns: ['security', 'quantity'];"></tr>
      </table>
    </mat-card>
  `
})
export class PositionGridComponent implements OnInit {
  rows: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<{ [key: string]: number }>('http://localhost:8080/api/positions')
      .subscribe(data => {
        this.rows = Object.entries(data).map(([security, quantity]) => ({ security, quantity }));
      });
  }
}

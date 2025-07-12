import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PositionGrid } from './position-grid';

describe('PositionGrid', () => {
  let component: PositionGrid;
  let fixture: ComponentFixture<PositionGrid>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PositionGrid]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PositionGrid);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

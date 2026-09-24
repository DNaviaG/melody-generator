import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Melody } from './melody';

describe('Melody', () => {
  let component: Melody;
  let fixture: ComponentFixture<Melody>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Melody],
    }).compileComponents();

    fixture = TestBed.createComponent(Melody);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

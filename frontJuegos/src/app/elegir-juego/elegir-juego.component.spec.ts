import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ElegirJuegoComponent } from './elegir-juego.component';

describe('ElegirJuegoComponent', () => {
  let component: ElegirJuegoComponent;
  let fixture: ComponentFixture<ElegirJuegoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ElegirJuegoComponent]
    });
    fixture = TestBed.createComponent(ElegirJuegoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

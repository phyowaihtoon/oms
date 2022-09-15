jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LovSetupDialogComponent } from './lov-setup-dialog.component';

describe('Component Tests', () => {
  describe('LovSetupDialogComponent Component', () => {
    let comp: LovSetupDialogComponent;
    let fixture: ComponentFixture<LovSetupDialogComponent>;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LovSetupDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LovSetupDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LovSetupDialogComponent);
      comp = fixture.componentInstance;
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});

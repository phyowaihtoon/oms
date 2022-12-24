import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentTrashbinDetailComponent } from './document-trashbin-detail.component';

describe('DocumentTrashbinDetailComponent', () => {
  let component: DocumentTrashbinDetailComponent;
  let fixture: ComponentFixture<DocumentTrashbinDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentTrashbinDetailComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentTrashbinDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

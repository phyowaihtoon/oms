import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentTrashbinComponent } from '../document-trashbin.component';

describe('DocumentTrashbinComponent', () => {
  let component: DocumentTrashbinComponent;
  let fixture: ComponentFixture<DocumentTrashbinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentTrashbinComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentTrashbinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

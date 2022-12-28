import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeDefinitionPopupComponent } from './code-definition-popup.component';

describe('CodeDefinitionPopupComponent', () => {
  let component: CodeDefinitionPopupComponent;
  let fixture: ComponentFixture<CodeDefinitionPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CodeDefinitionPopupComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeDefinitionPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

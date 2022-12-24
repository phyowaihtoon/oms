import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteMetadataComponent } from './delete-metadata.component';

describe('DeleteMetadataComponent', () => {
  let component: DeleteMetadataComponent;
  let fixture: ComponentFixture<DeleteMetadataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeleteMetadataComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteMetadataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

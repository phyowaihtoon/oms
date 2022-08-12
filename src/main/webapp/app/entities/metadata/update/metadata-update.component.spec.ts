import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetadataUpdateComponent } from './metadata-update.component';

describe('MetadataUpdateComponent', () => {
  let component: MetadataUpdateComponent;
  let fixture: ComponentFixture<MetadataUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MetadataUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetadataUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetadataTrashbinComponent } from './metadata-trashbin.component';

describe('MetadataTrashbinComponent', () => {
  let component: MetadataTrashbinComponent;
  let fixture: ComponentFixture<MetadataTrashbinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MetadataTrashbinComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetadataTrashbinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

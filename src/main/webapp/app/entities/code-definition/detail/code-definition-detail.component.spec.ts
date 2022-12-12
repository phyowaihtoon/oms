import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeDefinitionDetailComponent } from './code-definition-detail.component';

describe('CodeDefinition Management Detail Component', () => {
  let comp: CodeDefinitionDetailComponent;
  let fixture: ComponentFixture<CodeDefinitionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CodeDefinitionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ codeDefinition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CodeDefinitionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CodeDefinitionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load codeDefinition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.codeDefinition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

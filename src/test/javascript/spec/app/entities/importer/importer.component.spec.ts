import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FoodJournalTestModule } from '../../../test.module';
import { ImporterComponent } from 'app/entities/importer/importer.component';
import { ImporterService } from 'app/entities/importer/importer.service';
import { Importer } from 'app/shared/model/importer.model';

describe('Component Tests', () => {
  describe('Importer Management Component', () => {
    let comp: ImporterComponent;
    let fixture: ComponentFixture<ImporterComponent>;
    let service: ImporterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FoodJournalTestModule],
        declarations: [ImporterComponent],
      })
        .overrideTemplate(ImporterComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImporterComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ImporterService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Importer(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.importers && comp.importers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

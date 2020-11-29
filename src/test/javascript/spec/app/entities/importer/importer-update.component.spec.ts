import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FoodJournalTestModule } from '../../../test.module';
import { ImporterUpdateComponent } from 'app/entities/importer/importer-update.component';
import { ImporterService } from 'app/entities/importer/importer.service';
import { Importer } from 'app/shared/model/importer.model';

describe('Component Tests', () => {
  describe('Importer Management Update Component', () => {
    let comp: ImporterUpdateComponent;
    let fixture: ComponentFixture<ImporterUpdateComponent>;
    let service: ImporterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FoodJournalTestModule],
        declarations: [ImporterUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ImporterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImporterUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ImporterService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Importer(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Importer();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { FoodJournalTestModule } from '../../../test.module';
import { ImporterDetailComponent } from 'app/entities/importer/importer-detail.component';
import { Importer } from 'app/shared/model/importer.model';

describe('Component Tests', () => {
  describe('Importer Management Detail Component', () => {
    let comp: ImporterDetailComponent;
    let fixture: ComponentFixture<ImporterDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ importer: new Importer(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FoodJournalTestModule],
        declarations: [ImporterDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ImporterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ImporterDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load importer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.importer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});

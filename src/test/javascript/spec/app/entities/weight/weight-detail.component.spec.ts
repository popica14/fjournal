import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { FoodJournalTestModule } from '../../../test.module';
import { WeightDetailComponent } from 'app/entities/weight/weight-detail.component';
import { Weight } from 'app/shared/model/weight.model';

describe('Component Tests', () => {
  describe('Weight Management Detail Component', () => {
    let comp: WeightDetailComponent;
    let fixture: ComponentFixture<WeightDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ weight: new Weight(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FoodJournalTestModule],
        declarations: [WeightDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(WeightDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WeightDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load weight on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.weight).toEqual(jasmine.objectContaining({ id: 123 }));
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

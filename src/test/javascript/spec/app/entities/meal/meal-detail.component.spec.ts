import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { FoodJournalTestModule } from '../../../test.module';
import { MealDetailComponent } from 'app/entities/meal/meal-detail.component';
import { Meal } from 'app/shared/model/meal.model';

describe('Component Tests', () => {
  describe('Meal Management Detail Component', () => {
    let comp: MealDetailComponent;
    let fixture: ComponentFixture<MealDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ meal: new Meal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FoodJournalTestModule],
        declarations: [MealDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MealDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MealDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load meal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.meal).toEqual(jasmine.objectContaining({ id: 123 }));
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

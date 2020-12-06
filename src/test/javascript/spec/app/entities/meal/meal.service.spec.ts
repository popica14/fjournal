import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { MealService } from 'app/entities/meal/meal.service';
import { IMeal, Meal } from 'app/shared/model/meal.model';
import { MealType } from 'app/shared/model/enumerations/meal-type.model';

describe('Service Tests', () => {
  describe('Meal Service', () => {
    let injector: TestBed;
    let service: MealService;
    let httpMock: HttpTestingController;
    let elemDefault: IMeal;
    let expectedResult: IMeal | IMeal[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MealService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Meal(0, 'AAAAAAA', 0, 'AAAAAAA', MealType.SNACK, currentDate, 'image/png', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Meal', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Meal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Meal', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            quantity: 1,
            portionSize: 'BBBBBB',
            type: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            photo: 'BBBBBB',
            calories: 1,
            comment: 'BBBBBB',
            recipe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Meal', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            quantity: 1,
            portionSize: 'BBBBBB',
            type: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            photo: 'BBBBBB',
            calories: 1,
            comment: 'BBBBBB',
            recipe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Meal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

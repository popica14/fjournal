import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ImporterService } from 'app/entities/importer/importer.service';
import { IImporter, Importer } from 'app/shared/model/importer.model';

describe('Service Tests', () => {
  describe('Importer Service', () => {
    let injector: TestBed;
    let service: ImporterService;
    let httpMock: HttpTestingController;
    let elemDefault: IImporter;
    let expectedResult: IImporter | IImporter[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ImporterService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Importer(0, 'image/png', 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            importDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Importer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            importDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            importDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Importer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Importer', () => {
        const returnedFromService = Object.assign(
          {
            file: 'BBBBBB',
            importDate: currentDate.format(DATE_TIME_FORMAT),
            separator: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            importDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Importer', () => {
        const returnedFromService = Object.assign(
          {
            file: 'BBBBBB',
            importDate: currentDate.format(DATE_TIME_FORMAT),
            separator: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            importDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Importer', () => {
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

import { Moment } from 'moment';

export interface IWeight {
  id?: number;
  value?: number;
  date?: Moment;
  photoContentType?: string;
  photo?: any;
  observation?: string;
  myWeigthLogin?: string;
  myWeigthId?: number;
}

export class Weight implements IWeight {
  constructor(
    public id?: number,
    public value?: number,
    public date?: Moment,
    public photoContentType?: string,
    public photo?: any,
    public observation?: string,
    public myWeigthLogin?: string,
    public myWeigthId?: number
  ) {}
}

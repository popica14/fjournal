import { Moment } from 'moment';
import { MealType } from 'app/shared/model/enumerations/meal-type.model';

export interface IMeal {
  id?: number;
  description?: string;
  quantity?: number;
  portionSize?: string;
  type?: MealType;
  date?: Moment;
  photoContentType?: string;
  photo?: any;
  calories?: number;
  comment?: string;
  recipe?: string;
  myMealLogin?: string;
  myMealId?: number;
}

export class Meal implements IMeal {
  constructor(
    public id?: number,
    public description?: string,
    public quantity?: number,
    public portionSize?: string,
    public type?: MealType,
    public date?: Moment,
    public photoContentType?: string,
    public photo?: any,
    public calories?: number,
    public comment?: string,
    public recipe?: string,
    public myMealLogin?: string,
    public myMealId?: number
  ) {}
}

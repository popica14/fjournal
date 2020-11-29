import { Moment } from 'moment';

export interface IImporter {
  id?: number;
  fileContentType?: string;
  file?: any;
  importDate?: Moment;
  separator?: string;
  ownerLogin?: string;
  ownerId?: number;
  uploaderLogin?: string;
  uploaderId?: number;
}

export class Importer implements IImporter {
  constructor(
    public id?: number,
    public fileContentType?: string,
    public file?: any,
    public importDate?: Moment,
    public separator?: string,
    public ownerLogin?: string,
    public ownerId?: number,
    public uploaderLogin?: string,
    public uploaderId?: number
  ) {}
}

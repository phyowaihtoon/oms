import * as dayjs from 'dayjs';

export interface IRepositoryHeader {
  id?: number;
  repositoryName?: string;
  delFlag?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  repositoryDetails?: IRepository[];
}

export class RepositoryHeader implements IRepositoryHeader {
  constructor(
    public id?: number,
    public repositoryName?: string,
    public delFlag?: string,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public repositoryDetails?: IRepository[]
  ) {}
}

export interface IRepository {
  id?: number;
  headerId?: number;
  folderName?: string;
  folderOrder?: number;
  delFlag?: string;
}

export class Repository implements IRepository {
  constructor(
    public id?: number,
    public headerId?: number,
    public folderName?: string,
    public folderOrder?: number,
    public delFlag?: string
  ) {}
}

export function getRepositoryIdentifier(repositoryHeader: IRepository): number | undefined {
  return repositoryHeader.id;
}

export interface IRepositoryInquiry {
  repositoryName?: string;
  createdDate?: string;
}

export class RepositoryInquiry implements IRepositoryInquiry {
  constructor(public repositoryName?: string, public createdDate?: string) {}
}

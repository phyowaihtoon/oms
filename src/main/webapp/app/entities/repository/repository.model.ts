export interface IRepositoryHeader {
  id?: number;
  repositoryName?: string;
  repositoryDetails?: IRepository[];
}

export class RepositoryHeader implements IRepositoryHeader {
  constructor(public id?: number, public repositoryName?: string, public repositoryDetails?: IRepository[]) {}
}

export interface IRepository {
  id?: number;
  headerId?: number;
  folderName?: string;
  folderOrder?: number;
}

export class Repository implements IRepository {
  constructor(public id?: number, public headerId?: number, public folderName?: string, public folderOrder?: number) {}
}

export function getRepositoryIdentifier(repositoryHeader: IRepository): number | undefined {
  return repositoryHeader.id;
}

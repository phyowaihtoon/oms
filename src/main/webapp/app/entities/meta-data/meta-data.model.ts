export interface IMetaData {
  id?: number;
  headerId?: number;
  fieldName?: string;
  fieldType?: string;
  isRequired?: string;
  fieldOrder?: number | null;
  fieldValue?: string | null;
}

export class MetaData implements IMetaData {
  constructor(
    public id?: number,
    public headerId?: number,
    public fieldName?: string,
    public fieldType?: string,
    public isRequired?: string,
    public fieldOrder?: number | null,
    public fieldValue?: string | null
  ) {}
}

export function getMetaDataIdentifier(metaData: IMetaData): number | undefined {
  return metaData.id;
}

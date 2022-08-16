export interface IMetaDataHeader {
  id?: number;
  docTitle?: string;
}

export class MetaDataHeader implements IMetaDataHeader {
  constructor(public id?: number, public docTitle?: string) {}
}

export interface IMetaData {
  id?: number;
  headerId?: number;
  fieldName?: string;
  fieldType?: string;
  fieldValue?: string;
  isRequired?: string;
  fieldOrder?: number;
}

export class MetaData implements IMetaData {
  constructor(
    public id?: number,
    public headerId?: number,
    public fieldName?: string,
    public fieldType?: string,
    public fieldValue?: string,
    public isRequired?: string,
    public fieldOrder?: number
  ) {}
}

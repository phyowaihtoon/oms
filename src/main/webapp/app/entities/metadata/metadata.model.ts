import * as dayjs from 'dayjs';

export interface IMetaDataHeader {
  id?: number;
  docTitle?: string;
  delFlag?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  metaDataDetails?: IMetaData[];
}

export class MetaDataHeader implements IMetaDataHeader {
  constructor(
    public id?: number,
    public docTitle?: string,
    public delFlag?: string,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public metaDataDetails?: IMetaData[]
  ) {}
}

export interface IMetaData {
  id?: number;
  headerId?: number;
  fieldName?: string;
  fieldType?: string;
  fieldValue?: string;
  isRequired?: string;
  fieldOrder?: number;
  showDashboard?: string;
  delFlag?: string;
  isDisplayed?: boolean;
}

export class MetaData implements IMetaData {
  constructor(
    public id?: number,
    public headerId?: number,
    public fieldName?: string,
    public fieldType?: string,
    public fieldValue?: string,
    public isRequired?: string,
    public fieldOrder?: number,
    public showDashboard?: string,
    public delFlag?: string,
    public isDisplayed?: boolean
  ) {}
}

export function getMetadataIdentifier(metaDataHeader: IMetaDataHeader): number | undefined {
  return metaDataHeader.id;
}

export interface IMetaDataInquiry {
  docTitle?: string;
  createdDate?: string;
}

export class MetaDataInquiry implements IMetaDataInquiry {
  constructor(public docTitle?: string, public createdDate?: string) {}
}

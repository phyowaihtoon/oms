import * as dayjs from 'dayjs';

export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: number;
  fieldNames?: string;
  fieldValues?: string;
  repositoryURL?: string;
  message?: string;
  delFlag?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  docList?: IDocument[];
}

export class DocumentHeader implements IDocumentHeader {
  constructor(
    public id?: number,
    public metaDataHeaderId?: number,
    public fieldNames?: string,
    public fieldValues?: string,
    public repositoryURL?: string,
    public message?: string,
    public delFlag?: string,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public docList?: IDocument[]
  ) {}
}

export interface IDocument {
  id?: number;
  headerId?: number;
  filePath?: string;
  fileSize?: number;
  version?: number;
  remark?: string;
  delFlag?: string;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public headerId?: number,
    public filePath?: string,
    public fileSize?: number,
    public version?: number,
    public delFlag?: string,
    public remark?: string
  ) {}
}

export interface IDocumentInquiry {
  metaDataHeaderId?: number;
  repositoryURL?: string;
  createdDate?: string;
  fieldValues?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(public metaDataHeaderId?: number, public repositoryURL?: string, public createdDate?: string, public fieldValues?: string) {}
}

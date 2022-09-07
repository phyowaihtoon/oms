import * as dayjs from 'dayjs';

export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: number;
  fieldNames?: string;
  fieldValues?: string;
  repositoryURL?: string;
  message?: string;
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
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public headerId?: number,
    public filePath?: string,
    public fileSize?: number,
    public version?: number,
    public remark?: string
  ) {}
}

export interface IDocumentInquiry {
  id?: number;
  metaDataHeaderId?: number;
  repositoryURL?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(public id?: number, public metaDataHeaderId?: number, public docTitle?: string, public repositoryURL?: string) {}
}

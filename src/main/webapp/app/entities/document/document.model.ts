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
  fileName?: string;
  fileSize?: number;
  version?: number;
  remark?: string;
  delFlag?: string;
  fileData?: File; // This filed data is not needed to pass to server side, only used in client side
}

export class DMSDocument implements IDocument {
  constructor(
    public id?: number,
    public headerId?: number,
    public filePath?: string,
    public fileName?: string,
    public fileSize?: number,
    public version?: number,
    public delFlag?: string,
    public remark?: string,
    public fileData?: File
  ) {}
}

export interface IDocumentInquiry {
  metaDataHeaderId?: number;
  repositoryURL?: string;
  createdDate?: string;
  fieldValues?: string;
  fieldIndex?: number;
  generalValue?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(
    public metaDataHeaderId?: number,
    public repositoryURL?: string,
    public createdDate?: string,
    public fieldValues?: string,
    public fieldIndex?: number,
    public generalValue?: string
  ) {}
}

export function getDocIdentifier(docHeader: IDocumentHeader): number | undefined {
  return docHeader.id;
}

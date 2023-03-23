import * as dayjs from 'dayjs';

export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: number;
  fieldNames?: string;
  fieldValues?: string;
  repositoryURL?: string;
  priority?: number;
  status?: number;
  approvedbY?: string;
  reasonForAmend?: string;
  reasonForReject?: string;
  message?: string;
  delFlag?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  approvedDate?: dayjs.Dayjs;
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
    public priority?: number,
    public status?: number,
    public approvedbY?: string,
    public reasonForAmend?: string,
    public reasonForReject?: string,
    public delFlag?: string,
    public createdBy?: string,
    public createdDate?: dayjs.Dayjs,
    public approvedDate?: dayjs.Dayjs,
    public docList?: IDocument[]
  ) {}
}

export interface IDocument {
  id?: number;
  headerId?: number;
  filePath?: string;
  fileName?: string;
  fileNameVersion?: string;
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
    public fileNameVersion?: string,
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
  metaDataID1?: number;
  fieldValue1?: string;
  fieldIndex1?: number;
  metaDataID2?: number;
  fieldValue2?: string;
  fieldIndex2?: number;
  fieldValue3?: string;
  fieldIndex3?: number;
  fieldValue4?: string;
  fieldIndex4?: number;
  fieldSearchType1?: string;
  fieldSearchType2?: string;
  fieldSearchType3?: string;
  fieldSearchType4?: string;
  fieldSortBy1?: number;
  fieldSortBy2?: number;
  fieldSortBy3?: number;
  fieldSortBy4?: number;
  status?: number;
  reason?: string;
  generalValue?: string;
  approvedBy?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(
    public metaDataHeaderId?: number,
    public repositoryURL?: string,
    public createdDate?: string,
    public metaDataID1?: number,
    public fieldValue1?: string,
    public fieldIndex1?: number,
    public metaDataID2?: number,
    public fieldValue2?: string,
    public fieldIndex2?: number,
    public fieldValue3?: string,
    public fieldIndex3?: number,
    public fieldValue4?: string,
    public fieldIndex4?: number,
    public fieldSearchType1?: string,
    public fieldSearchType2?: string,
    public fieldSearchType3?: string,
    public fieldSearchType4?: string,
    public fieldSortBy1?: number,
    public fieldSortBy2?: number,
    public fieldSortBy3?: number,
    public fieldSortBy4?: number,
    public status?: number,
    public reason?: string,
    public generalValue?: string,
    public approvedBy?: string
  ) {}
}

export function getDocIdentifier(docHeader: IDocumentHeader): number | undefined {
  return docHeader.id;
}

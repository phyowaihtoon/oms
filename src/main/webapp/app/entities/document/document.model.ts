export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: number;
  fieldNames?: string;
  fieldValues?: string;
  repositoryURL?: string;
}

export class DocumentHeader implements IDocumentHeader {
  constructor(
    public id?: number,
    public metaDataHeaderId?: number,
    public fieldNames?: string,
    public fieldValues?: string,
    public repositoryURL?: string
  ) {}
}

export interface IDocument {
  id?: number;
  headerId?: number;
  filePath?: string;
}

export class Document implements IDocument {
  constructor(public id?: number, public headerId?: number, public filePath?: string) {}
}

export interface IDocumentInquiry {
  id?: number;
  metaDataHeaderId?: number;
  repositoryURL?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(public id?: number, public metaDataHeaderId?: number, public docTitle?: string, public repositoryURL?: string) {}
}

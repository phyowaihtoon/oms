export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: string;
  fieldNames?: string;
  fieldValues?: string;
  repositoryURL?: string;
}

export class DocumentHeader implements IDocumentHeader {
  constructor(
    public id?: number,
    public metaDataHeaderId?: string,
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
  docTitle?: string;
  repositoryURL?: string;
}

export class DocumentInquiry implements IDocumentInquiry {
  constructor(public id?: number, public docTitle?: string, public repositoryURL?: string) {}
}

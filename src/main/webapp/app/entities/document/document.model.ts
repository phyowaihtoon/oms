export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: string;
  fieldNames?: string;
  fieldValues?: string;
}

export class DocumentHeader implements IDocumentHeader {
  constructor(public id?: number, public metaDataHeaderId?: string, public fieldNames?: string, public fieldValues?: string) {}
}

export interface IDocument {
  id?: number;
  headerId?: number;
  filePath?: string;
}

export class Document implements IDocument {
  constructor(public id?: number, public headerId?: number, public filePath?: string) {}
}

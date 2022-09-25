export interface IDocumentHeader {
  id?: number;
  metaDataHeaderId?: number | null;
  fieldNames?: string | null;
  fieldValues?: string | null;
  repositoryURL?: string | null;
}

export class DocumentHeader implements IDocumentHeader {
  constructor(
    public id?: number,
    public metaDataHeaderId?: number | null,
    public fieldNames?: string | null,
    public fieldValues?: string | null,
    public repositoryURL?: string | null
  ) {}
}

export function getDocumentHeaderIdentifier(documentHeader: IDocumentHeader): number | undefined {
  return documentHeader.id;
}

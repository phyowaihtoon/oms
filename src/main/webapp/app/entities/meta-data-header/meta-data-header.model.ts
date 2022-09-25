export interface IMetaDataHeader {
  id?: number;
  docTitle?: string;
}

export class MetaDataHeader implements IMetaDataHeader {
  constructor(public id?: number, public docTitle?: string) {}
}

export function getMetaDataHeaderIdentifier(metaDataHeader: IMetaDataHeader): number | undefined {
  return metaDataHeader.id;
}

import { MetaDataHeader } from '../metadata/metadata.model';

export interface ICodeDefinition {
  id?: number;
  metaDataHeader?: MetaDataHeader | null;
  code?: string | null;
  definition?: string | null;
}

export class CodeDefinition implements ICodeDefinition {
  constructor(
    public id?: number,
    public metaDataHeader?: MetaDataHeader | null,
    public code?: string | null,
    public definition?: string | null
  ) {}
}

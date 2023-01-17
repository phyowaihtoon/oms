export interface ICodeDefinition {
  id?: number;
  type?: string | null;
  code?: string | null;
  definition?: string | null;
  typeDescription?: string | null;
}

export class CodeDefinition implements ICodeDefinition {
  constructor(
    public id?: number,
    public type?: string | null,
    public code?: string | null,
    public definition?: string | null,
    public typeDescription?: string | null
  ) {}
}

export interface ICodeDefinition {
  id?: number;
  code?: string | null;
  definition?: string | null;
}

export class CodeDefinition implements ICodeDefinition {
  constructor(public id?: number, public code?: string | null, public definition?: string | null) {}
}

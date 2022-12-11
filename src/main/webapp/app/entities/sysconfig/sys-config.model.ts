export interface ISysConfig {
  id?: number;
  code?: string;
  definition?: string;
  value?: string;
  enabled?: string;
}

export class SysConfig implements ISysConfig {
  constructor(public id?: number, public code?: string, public definition?: string, public value?: string, public enabled?: string) {}
}

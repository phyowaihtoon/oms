export interface IInputParam {
  templateId?: number;
  fromDate?: string;
  toDate?: string;
}

export class InputParam implements IInputParam {
  constructor(public templateId?: number, public fromDate?: string, public toDate?: string) {}
}

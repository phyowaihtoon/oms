export interface ISearchCriteria {
  requestFrom?: number;
  dateOn?: string;
  dateFrom?: string;
  dateTo?: string;
  referenceNo?: string;
  status?: number;
  senderId?: number;
  receiverId?: number;
  subject?: string;
}

export class SearchCriteria implements ISearchCriteria {
  constructor(
    public requestFrom?: number,
    public dateOn?: string,
    public dateFrom?: string,
    public dateTo?: string,
    public referenceNo?: string,
    public status?: number,
    public senderId?: number,
    public receiverId?: number,
    public subject?: string
  ) {}
}

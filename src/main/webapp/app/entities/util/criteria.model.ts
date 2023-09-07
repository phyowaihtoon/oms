export interface ISearchCriteria {
  dateOn?: string;
  dateFrom?: string;
  dateTo?: string;
  status?: number;
}

export class SearchCriteria implements ISearchCriteria {
  constructor(public dateOn?: string, public dateFrom?: string, public dateTo?: string, public status?: number) {}
}

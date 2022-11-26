export interface IDashboardTemplate {
  id?: number;
  cardId?: string;
  cardName?: string;
}

export class DashboardTemplate implements IDashboardTemplate {
  constructor(public id?: number, public cardId?: string, public cardName?: string) {}
}

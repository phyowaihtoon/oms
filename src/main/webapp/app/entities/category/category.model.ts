export interface ICategory {
  id?: number;
  code?: string;
  description?: string;
}

export class Category implements ICategory {
  constructor(public id?: number, public code?: string, public description?: string) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}

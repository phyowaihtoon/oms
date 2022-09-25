export interface IDepartment {
  id?: number;
  departmentName?: string;
  delFlag?: string;
}

export class Department implements IDepartment {
  constructor(public id?: number, public departmentName?: string, public delFlag?: string) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}

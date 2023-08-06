export interface IDepartment {
  id?: number;
  departmentName?: string;
  delFlag?: string;
  headDepartment?: IHeadDepartment;
}

export class Department implements IDepartment {
  constructor(public id?: number, public departmentName?: string, public delFlag?: string, public headDepartment?: IHeadDepartment) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}

export interface IHeadDepartment {
  id?: number;
  description?: string;
}

export class HeadDepartment implements IHeadDepartment {
  constructor(public id?: number, public description?: string) {}
}

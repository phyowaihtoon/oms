export interface IRoleMenuMap {
  id?: number;
  roleId?: string;
  menuId?: number;
  allowed?: string;
}

export class RoleMenuMap implements IRoleMenuMap {
  constructor(public id?: number, public roleId?: string, public menuId?: number, public allowed?: string) {}
}

export function getRoleMenuMapIdentifier(roleMenuMap: IRoleMenuMap): number | undefined {
  return roleMenuMap.id;
}

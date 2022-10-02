export interface IUserRole {
  id?: number;
  roleName?: string;
}

export class UserRole implements IUserRole {
  constructor(public id?: number, public roleName?: string) {}
}

export function getUserRoleIdentifier(userRole: IUserRole): number | undefined {
  return userRole.id;
}

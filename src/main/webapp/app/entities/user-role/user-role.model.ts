import { IMenuItem } from '../util/setup.model';

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

export interface IRoleMenuAccess {
  id?: number;
  isAllow?: number;
  isRead?: number;
  isWrite?: number;
  isDelete?: number;
  menuItem?: IMenuItem;
  userRole?: IUserRole;
}

export class RoleMenuAccess implements IRoleMenuAccess {
  constructor(
    public id?: number,
    public isAllow?: number,
    public isRead?: number,
    public isWrite?: number,
    public isDelete?: number,
    public menuItem?: IMenuItem,
    public userRole?: IUserRole
  ) {}
}

export interface IMenuGroupMessage {
  id?: number;
  name?: string;
  translateKey?: string;
  orderNo?: number;
  subMenuItems?: IRoleMenuAccess[];
}

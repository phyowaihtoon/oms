import { IDashboardTemplate } from 'app/services/dashboard-template.model';
import { IMetaDataHeader } from '../metadata/metadata.model';
import { IMenuItem } from '../util/setup.model';

export interface IUserRole {
  id?: number;
  roleName?: string;
  roleType?: number;
}

export class UserRole implements IUserRole {
  constructor(public id?: number, public roleName?: string, public roleType?: number) {}
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

export interface IRoleTemplateAccess {
  id?: number;
  metaDataHeader?: IMetaDataHeader;
  userRole?: IUserRole;
}

export class RoleTemplateAccess implements IRoleTemplateAccess {
  constructor(public id?: number, public metaDataHeader?: IMetaDataHeader, public userRole?: IUserRole) {}
}

export interface IRoleDashboardAccess {
  id?: number;
  dashboardTemplate?: IDashboardTemplate;
  userRole?: IUserRole;
}

export class RoleDashboardAccess implements IRoleDashboardAccess {
  constructor(public id?: number, public dashboardTemplate?: IDashboardTemplate, public userRole?: IUserRole) {}
}

export interface IMenuGroupMessage {
  id?: number;
  groupCode?: string;
  name?: string;
  translateKey?: string;
  faIcon?: string;
  orderNo?: number;
  routerLink?: string;
  subMenuItems?: IRoleMenuAccess[];
}

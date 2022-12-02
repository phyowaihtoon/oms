import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { IMenuGroupMessage, IRoleMenuAccess } from 'app/entities/user-role/user-role.model';

export interface IUserAuthority {
  userID: string;
  userName: string;
  departmentName?: string;
  roleName: string;
  workflowAuthority: number;
  sysConfigMessage: ISysConfigMessage;
  menuGroups: IMenuGroupMessage[];
  activeMenu: IRoleMenuAccess;
  templateList: IMetaDataHeader[];
}

export interface ISysConfigMessage {
  workflowEnabled: string;
}

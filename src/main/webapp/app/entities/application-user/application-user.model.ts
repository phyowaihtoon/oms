import { IUser } from 'app/entities/user/user.model';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { MetaDataHeader } from '../metadata/metadata.model';

export interface IApplicationUser {
  id?: number;
  workflowAuthority?: number;
  user?: IUser;
  userRole?: IUserRole;
  department?: MetaDataHeader | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public workflowAuthority?: number,
    public user?: IUser,
    public userRole?: IUserRole,
    public department?: MetaDataHeader | null
  ) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}

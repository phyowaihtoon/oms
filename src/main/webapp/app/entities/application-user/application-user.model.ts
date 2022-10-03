import { IUser } from 'app/entities/user/user.model';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IApplicationUser {
  id?: number;
  workflowAuthority?: number;
  user?: IUser;
  userRole?: IUserRole;
  department?: IDepartment | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public workflowAuthority?: number,
    public user?: IUser,
    public userRole?: IUserRole,
    public department?: IDepartment | null
  ) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}

import { IUserRole, NewUserRole } from './user-role.model';

export const sampleWithRequiredData: IUserRole = {
  id: 43156,
  roleName: 'enable invoice',
};

export const sampleWithPartialData: IUserRole = {
  id: 95729,
  roleName: 'protocol',
};

export const sampleWithFullData: IUserRole = {
  id: 42054,
  roleName: 'Borders bluetooth',
};

export const sampleWithNewData: NewUserRole = {
  roleName: 'paradigm compress',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

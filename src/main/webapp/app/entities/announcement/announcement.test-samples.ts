import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 39095,
  departmentName: 'neutral markets',
  delFlag: 'b',
};

export const sampleWithPartialData: IDepartment = {
  id: 30654,
  departmentName: 'Tuna',
  delFlag: 'F',
};

export const sampleWithFullData: IDepartment = {
  id: 23664,
  departmentName: 'compressing cross-media digital',
  delFlag: 'X',
};

export const sampleWithNewData: NewDepartment = {
  departmentName: 'Metal',
  delFlag: 'A',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

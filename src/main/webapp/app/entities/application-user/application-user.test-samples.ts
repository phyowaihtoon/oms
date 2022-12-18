import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 60827,
  workflowAuthority: 14397,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 95279,
  workflowAuthority: 82117,
};

export const sampleWithFullData: IApplicationUser = {
  id: 82798,
  workflowAuthority: 55912,
};

export const sampleWithNewData: NewApplicationUser = {
  workflowAuthority: 8058,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

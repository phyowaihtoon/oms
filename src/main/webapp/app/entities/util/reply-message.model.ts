export interface IReplyMessage {
  code: string;
  message: string;
  data?: any;
}

export class ResponseCode {
  static readonly SUCCESS: string = '000';
  static readonly SUCCESS_MSG: string = 'SUCCESS';

  static readonly ERROR_E00: string = 'E00';
  static readonly ERROR_E01: string = 'E01';
  static readonly ERROR_MSG: string = 'ERROR';

  static readonly WARNING: string = 'W00';
  static readonly WARNING_MSG: string = 'WARNING';

  static readonly RESPONSE_FAILED_CODE: string = 'R00';
  static readonly RESPONSE_FAILED_MSG: string = 'CONNECTION TIMEOUT';
}

export interface IHeaderDetailsMessage {
  code?: string;
  message?: string;
  header?: any;
  details1?: any;
  details2?: any;
}

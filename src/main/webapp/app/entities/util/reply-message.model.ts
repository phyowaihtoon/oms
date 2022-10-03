export interface IReplyMessage {
  code: string;
  message: string;
  data?: any;
}

export class ResponseCode {
  static readonly SUCCESS_CODE: string = '000';
  static readonly SUCCESS_MSG: string = 'SUCCESS';

  static readonly ERROR_CODE: string = 'E00';
  static readonly ERROR_MSG: string = 'ERROR';

  static readonly WARNING_CODE: string = 'W00';
  static readonly WARNING_MSG: string = 'WARNING';

  static readonly RESPONSE_FAILED_CODE: string = 'R00';
  static readonly RESPONSE_FAILED_MSG: string = 'CONNECTION TIMEOUT';
}

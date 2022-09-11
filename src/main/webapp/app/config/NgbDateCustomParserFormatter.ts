import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Injectable } from '@angular/core';
import * as dayjs from 'dayjs';

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  /*
  parse(value: string): NgbDateStruct | null{
    if (value) {
      const dateParts = value.trim().split('-');
      if (dateParts.length === 1 ) {
        return {day: Number(dateParts[0]), month:0, year: 0};
      } else if (dateParts.length === 2 ) {
        return {day: Number(dateParts[0]), month: Number(dateParts[1]), year: 0};
      } else if (dateParts.length === 3) {
        return {day: Number(dateParts[0]), month:Number(dateParts[1]), year: Number(dateParts[2])};
      }
    }
    return null;
  }

  format(date: NgbDateStruct): string {
    console.warn("Printing NgbDateStruct inside NgbDateCustomParserFormatter");
    console.warn(date);
    return `${this.customPadNumber(date.day)}-${this.customPadNumber(date.month)}-${date.year}`;
  }

  customPadNumber(numDM:number):string{
    if(numDM<10){
      return "0"+numDM.toString();
    }
    return numDM.toString();
  }

  */

  parse(value: string | null): NgbDateStruct | null {
    const dateFormat = 'DD-MM-YYYY';
    if (value) {
      const otherthing = dayjs(value).format(dateFormat);
      const tests = dayjs(otherthing);
      const something = {
        day: tests.date(),
        month: tests.month() + 1,
        year: tests.year(),
      };
      return something;
    }
    return null;
  }

  format(date: NgbDateStruct | null): string {
    if (date) {
      const something = dayjs(new Date(date.year, date.month - 1, date.day));
      return something.format('DD-MM-YYYY');
    }
    return '';
  }
}

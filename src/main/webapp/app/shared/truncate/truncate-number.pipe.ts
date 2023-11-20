import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncateNumber',
})
export class TruncateNumberPipe implements PipeTransform {
  transform(value: number, maxLength: number): string {
    const convertedValue = String(value);
    if (convertedValue.length > maxLength) {
      return convertedValue.substring(0, maxLength) + '+';
    }
    return convertedValue;
  }
}

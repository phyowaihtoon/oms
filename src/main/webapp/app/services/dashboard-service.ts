import { Injectable } from '@angular/core';
import * as Highcharts from 'highcharts';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  generatePieChart(id: any, d: any): void {
    const options: Highcharts.Options = {
      chart: { type: 'pie' },
      title: { text: '' },
      tooltip: { pointFormat: '</b>{point.percentage:.2f}%</b>' },

      plotOptions: {
        series: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: true,
            format: '{point.percentage:.2f}%',
          },
          /* events: {

              'click': (event:any) => {
                  alert(
                    JSON.stringify(this) +'\n'+
                      'Alt: ' + event.altKey + '\n' +
                      'Control: ' + event.ctrlKey + '\n' +
                      'Meta: ' + event.metaKey + '\n' +
                      'Shift: ' + event.shiftKey
                  );
              }
           }, */
          /* point: {
            events: {
              click: this.onPointClick,
            },
          }, */
          showInLegend: true,
        },
      },
      credits: { enabled: false },
      series: d,
    };
    Highcharts.chart(id, options);
  }

  generateLineChart(id: string, data: any, col: any, ytitle: any): void {
    const options: any = {
      chart: { type: 'line' },
      title: { text: '' },
      subtitle: { text: '' },
      xAxis: {
        categories: col,
      },
      yAxis: {
        min: 0,
        /*  labels: {
           // tslint:disable-next-line: typedef
            formatter: function() {
              return this.value / 1000000 + 'M' ;
            }
        }, */
        title: {
          text: ytitle,
        },
      },
      credits: { enabled: false },
      plotOptions: {
        line: {
          dataLabels: {
            enabled: true,
          },
          enableMouseTracking: false,
        },
      },
      series: data,
    };
    Highcharts.chart(id, options);
  }

  generateBarChart(id: any, d: any, c: any, yt: any): void {
    Highcharts.chart(id, {
      chart: { type: 'column' },
      title: { text: '' },
      subtitle: { text: '' },
      xAxis: {
        categories: c,
        crosshair: true,
      },
      yAxis: {
        min: 0,
        title: { text: yt },
      },
      credits: { enabled: false },
      tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat:
          '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true,
      },
      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0,
        },
      },
      series: d,
    });
  }

  pieClick(): any {
    window.alert('Click Event...');
  }

  onPointClick = (event: any): any => {
    window.alert('event/....');
  };
}

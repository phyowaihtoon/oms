import { Injectable } from '@angular/core';
import * as Highcharts from 'highcharts';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  generatePieChart(id: any, d: any, title: any): void {
    const options: Highcharts.Options = {
      chart: { type: 'pie' },
      title: {
        text: title,
        style: {
          color: '#000000',
          fontSize: '22px',
          fontWeight: 'bold',
          fontFamily: '"Arbutus Slab", serif',
        },
      },
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

  generateLineChart(id: string, data: any, col: any, ytitle: any, title: any): void {
    const options: any = {
      chart: { type: 'line' },
      title: {
        text: title,
        style: {
          color: '#000000',
          fontSize: '22px',
          fontWeight: 'bold',
          fontFamily: '"Arbutus Slab", serif',
        },
      },
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

  generateBarChart(id: any, d: any, c: any, ytitle: any, title: any): void {
    Highcharts.chart(id, {
      chart: { type: 'column' },
      title: {
        text: title,
        style: {
          color: '#000000',
          fontSize: '22px',
          fontWeight: 'bold',
          fontFamily: '"Arbutus Slab", serif',
        },
      },
      subtitle: { text: '' },
      xAxis: {
        categories: c,
        crosshair: true,
      },
      yAxis: {
        min: 0,
        title: { text: ytitle },
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
          dataLabels: {
            enabled: true,
            color: '#000000',
            crop: false,
          },
        },
      },
      series: d,
    });
  }

  generateSingleBarChart(id: any, dataseries: any, col: any, ytitle: any, title: any): void {
    const options: any = {
      chart: { type: 'column' },
      title: {
        text: title,
        style: {
          color: '#000000',
          fontSize: '22px',
          fontWeight: 'bold',
          fontFamily: '"Arbutus Slab", serif',
        },
      },

      xAxis: {
        categories: col,
        crosshair: true,
      },

      yAxis: {
        min: 0,
        title: { text: ytitle },
      },
      tooltip: {
        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
        pointFormat:
          '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
        footerFormat: '</table>',
        shared: true,
        useHTML: true,
        enabled: false,
      },
      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0,
          dataLabels: {
            enabled: true,
            color: '#000000',
            crop: false,
          },
        },
      },
      series: [
        {
          name: '',
          colorByPoint: true,
          data: dataseries,
          colors: ['#c97530'],
        },
      ],
      legend: false,
      credits: { enabled: false },
    };
    Highcharts.chart(id, options);
  }

  pieClick(): any {
    window.alert('Click Event...');
  }

  onPointClick = (event: any): any => {
    window.alert('event/....');
  };
}

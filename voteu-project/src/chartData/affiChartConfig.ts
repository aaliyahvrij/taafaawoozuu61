/**
 * example of chart data
 */
import type { ChartData } from 'chart.js'

export const affiData: ChartData<'pie', number[], string> = {
  labels: [], // string[]
  datasets: [{
    label: 'All the affiliations currently in the election',
    data: [], // number[]
    backgroundColor: [], // string[]
    hoverOffset: 4,
  }]
};



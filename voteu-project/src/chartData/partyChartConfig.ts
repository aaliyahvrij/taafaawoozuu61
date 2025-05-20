/**
 * example of chart data
 */
import type { ChartData } from 'chart.js'


export const partyData: ChartData<'pie', number[], string> = {
  labels: [], // string[]
  datasets: [{
    label: 'All the parties currently in the election',
    data: [], // number[]
    backgroundColor: [], // string[]
    hoverOffset: 4,
  }]
};



package me.xcdu.bo;

import me.xcdu.bo.ChartBuilder.TopErrorRateChart;
import me.xcdu.bo.ChartBuilder.TopViewedChart;

public class OverviewCharts {
  public TopViewedChart topViewed;
  public TopErrorRateChart topErrorRate;

  public OverviewCharts(OverviewCharts charts) {
    this.topViewed = charts.topViewed;
    this.topErrorRate = charts.topErrorRate;
  }

  public OverviewCharts(TopViewedChart topViewed,
      TopErrorRateChart topErrorRate) {
    this.topViewed = topViewed;
    this.topErrorRate = topErrorRate;
  }
}

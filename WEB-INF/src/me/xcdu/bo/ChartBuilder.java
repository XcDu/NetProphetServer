package me.xcdu.bo;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class ChartBuilder {

  // Top Viewed Chart
  public class TopViewedChart {
    public ArrayList<String> categories;
    public ArrayList<Integer> data;

    public TopViewedChart() {
      categories = new ArrayList<String>();
      data = new ArrayList<Integer>();
    }
  }
  // Top Error Rate Chart
  public class TopErrorRateChart {
    public ArrayList<String> url;
    public ArrayList<Double> errorRate;

    public TopErrorRateChart() {
      url = new ArrayList<String>();
      errorRate = new ArrayList<Double>();
    }
  }
  // Delay Line Chart
  public class DelayLineChart {
    public SortedMap<Long, Long> data;

    public DelayLineChart() {
      data = new TreeMap<Long, Long>();
    }
  }
  // Delay Stacked Bar Chart
  public class DelayStackedBarChart {
    public ArrayList<String> categories;
    public ArrayList<DelayStackedBarChartSeries> series;

    public DelayStackedBarChart() {
      categories = new ArrayList<String>();
      series = new ArrayList<DelayStackedBarChartSeries>();
    }

    public void categoriesAddElement(String date) {
      categories.add(date);
    }

    public void seriesAddElementWithName(String name) {
      series.add(new DelayStackedBarChartSeries(name));
    }

    public void seriesAddData(int index, Long data) {
      series.get(index).data.add(data);
    }
  }
  public class DelayStackedBarChartSeries {
    public String name;
    public ArrayList<Long> data;

    public DelayStackedBarChartSeries() {
      name = "";
      data = new ArrayList<Long>();
    }

    public DelayStackedBarChartSeries(String name) {
      this.name = name;
      data = new ArrayList<Long>();
    }
  }
  // Delay Pie Chart
  public class DelayPieChart {
    public ArrayList<DelayPieChartSeries> series;

    public DelayPieChart() {
      series = new ArrayList<DelayPieChartSeries>();
    }

    public void seriesAddElement() {
      series.add(new DelayPieChartSeries());
    }

    public void setSeriesData(int index, String name, Double y) {
      series.get(index).data.add(new DelayPieChartData(name, y));
    }
  }

  public class DelayPieChartSeries {
    public String name;
    public boolean colorByPoint;
    public ArrayList<DelayPieChartData> data;

    public DelayPieChartSeries() {
      name = "";
      colorByPoint = true;
      data = new ArrayList<DelayPieChartData>();
    }
  }
  public class DelayPieChartData {
    public String name;
    public Double y;

    public DelayPieChartData() {
      this.name = "";
      this.y = new Double(0);
    }

    public DelayPieChartData(String name, Double y) {
      this.name = name;
      this.y = y;
    }
  }
  // Url List Error Rate Chart
  public class ErrorRateChart {
    public Double errorRate;

    public ErrorRateChart() {
      errorRate = 0.0;
    }
  }
  // Redirection Chart
  public class RedirectionChart {
    public ArrayList<String> redirectionList;

    public RedirectionChart() {
      redirectionList = new ArrayList<String>();
    }
  }

  // Methods
  public TopViewedChart createTopViewdChart() {
    return new TopViewedChart();
  }

  public TopErrorRateChart createTopErrorRateChart() {
    return new TopErrorRateChart();
  }

  public DelayLineChart createDelayLineChart() {
    return new DelayLineChart();
  }

  public DelayStackedBarChart createDelayStackedBarChart() {
    return new DelayStackedBarChart();
  }

  public DelayPieChart createDelayPieChart() {
    return new DelayPieChart();
  }

  public ErrorRateChart createErrorRateChart() {
    return new ErrorRateChart();
  }

  public RedirectionChart createRedirectionChart() {
    return new RedirectionChart();
  }
}

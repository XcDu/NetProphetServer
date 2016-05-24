/**
 *
 */
// Nicescroll Init
$(function() {
  $("#scrollbar-side").niceScroll({
    cursorwidth : 10,
    zindex : 10000
  });
});

$(function() {
  $("html").niceScroll({
    cursorwidth : 10,
    zindex : 10000
  });
});
// Nicescroll Init end

// Sidebar init
function getUrlIndex(sortBy) {
  $("ul.nav.nav-sidebar").empty();
  $.ajax({url:"UrlIndexServlet", data:{ app: application, sortBy : sortBy }, type: "POST",
    success: function (urlsIndex){
    for(var i=0;i<urlsIndex.length;++i) {
      var appendString="";
      for(var j=0;j<urlsIndex[i].subUrlList.length;++j){
        appendString += "<li><a class=\"selectable\" href=\"#\"><p data-toggle=\"tooltip\" data-placement=\"right\" title=\""+urlsIndex[i].subUrlList[j]+"\">"+urlsIndex[i].subUrlList[j]+"</p></a></li>\n";
      }
      $("ul.nav.nav-sidebar").append("<li><a data-toggle=\"collapse\" "+
        "href=\"#nav-sublist"+i+"\">"+urlsIndex[i].domain+"</a></li>").append(
        "<ul class=\"sub-list collapse\"  id=\"nav-sublist"+i+"\">\n"+
        appendString+"</ul>\n");
    }
    // var cnt=0;
    // for(urls in urlsIndex){
    //   var urlList = urlsIndex[urls];
    //   var appendString = "";
    //   for(var i=0;i<urlList.length;++i) {
    //     appendString += "<li><a class=\"selectable\" href=\"#\"><p>"+urlList[i]+"</p></a></li>\n";
    //   }
    //   $("ul.nav.nav-sidebar").append("<li><a data-toggle=\"collapse\" "+
    //    "href=\"#nav-sublist"+cnt+"\">"+urls+"</a></li>").append(
    //    "<ul class=\"sub-list collapse\"  id=\"nav-sublist"+cnt+"\">\n"+
    //    appendString+"</ul>\n");
    //    // console.log(cur);
    //    cnt++;
    // }
  }});
  $("#scrollbar-side").getNiceScroll().resize();
}
// Sidebar init end

// Url index init
$(getUrlIndex($(".sortby-selector select option:selected").text()));
// Url index init


// Charts init
$.ajax({url:"ChartServlet", data:{app: application, type:"overview"},
 success: drawOverviewHighCharts, dataType: 'json',type: "POST"});
// Charts init end

// Charts to show
// Charts to show : Overview charts
var wifiContentSelector='#wifi-content';
var mobileContentSelector='#mobile-content';
function mainContentClear(){
  $(wifiContentSelector).empty();
  $(mobileContentSelector).empty();
}
// Set attribute id in div as "wifi/mobile"+"-"+id. Use "#wifi-id" or
// "#mobile-id" to select the element;
function addCanvasDivs(id){
  $(wifiContentSelector).append("<div class='col-md-12 col-sm-12 card'><div id='wifi-"+id+"'></div></div>");
  $(mobileContentSelector).append("<div class='col-md-12 col-sm-12 card'><div id='mobile-"+id+"'></div></div>");
}
function drawOverviewHighCharts(result) {
  mainContentClear();
  addCanvasDivs("top-delay");
  addCanvasDivs("top-error-rate")
  // $('#wifi-content').append('<div class="col-xs-12 col-sm-12 card"><div id="wifi-top-delay"></div></div>');
  // $('#mobile-content').append('<div class="col-xs-12 col-sm-12 card"><div id="mobile-top-delay"></div></div>');
  // $('#wifi-content').append('<div class="col-xs-12 col-sm-12 card"><div id="wifi-top-error-rate"></div></div>');
  // $('#mobile-content').append('<div class="col-xs-12 col-sm-12 card"><div id="mobile-top-error-rate"></div></div>');
  drawOverviewTopViewed(result.wifi.topViewed,"#wifi-top-delay");
  drawOverviewTopViewed(result.mobile.topViewed,"#mobile-top-delay");
  drawOverviewTopErrorRate(result.wifi.topErrorRate,"#wifi-top-error-rate");
  drawOverviewTopErrorRate(result.mobile.topErrorRate,"#mobile-top-error-rate");
  $("html").getNiceScroll().resize();
}

function drawOverviewTopViewed(chart,location){
  var categories = chart.categories;
  var data = chart.data;
  $(location).highcharts({
      chart: {
          type: 'bar',
          style:{
              "font-family": "Arial",
          }
      },
      credits: {
        enabled: false
      },
      title: {
          text: 'Top 20 Viewed URL',
          style:{
            "font-weight": "bold"
          }
      },
      xAxis: {
          categories: categories,
          title: {
              text: null
          },
          labels: {
            step: 1
          }
      },
      yAxis: {
          min: 0,
          title: {
              text: 'Count',
              align: 'high'
          },
          labels: {
              overflow: 'justify'
          }
      },
      tooltip: {
          valueSuffix: ' times'
      },
      plotOptions: {
          bar: {
              dataLabels: {
                  enabled: true
              },
              color: '#52D3AA',
              maxPointWidth: 50
          }
      },
      series: [{
        name: 'Viewed count',
        data: data
      }]
  });
}
function drawOverviewTopErrorRate(chart,location){
  var url = chart.url;
  var errorRate = chart.errorRate;
  $(location).append('<div class="error-rate-table-title">Top 10 Error Rate</div><table class="table table-hover error-rate-table"> <thead> <tr> <th>URL</th> <th>Error Rate</th> </tr> </thead>\n<tbody> </tbody> </table>');
  if(url.length==0){
     $(location+" tbody").append('<tr> <td>None</td> <td> </td> </tr>');
  }else{
    for(var i=0;i<url.length;++i) {
       $(location+" tbody").append('<tr> <td> '+url[i]+'</td> <td>'+errorRate[i]+'</td> </tr>');
    }
  }
}
// Charts to show: Overview charts end

// Charts to show: Url index charts
function drawUrlHighCharts(result) {
  // $('#wifi-content').empty();
  // $('#mobile-content').empty();
  // $('#wifi-content').append('<div class="col-xs-12 col-sm-12 card"><div id="wifi-delay-line"></div></div>');
  // $('#mobile-content').append('<div class="col-xs-12 col-sm-12 card"><div id="mobile-delay-line"></div></div>');
  // $('#wifi-content').append('<div class="col-xs-12 col-sm-12 card"><div id="wifi-delay-stacked"></div></div>');
  // $('#mobile-content').append('<div class="col-xs-12 col-sm-12 card"><div id="mobile-delay-stacked"></div></div>');
  // $('#wifi-content').append('<div class="col-xs-12 col-sm-12 card"><div id="wifi-delay-pie"></div></div>');
  // $('#mobile-content').append('<div class="col-xs-12 col-sm-12 card"><div id="mobile-delay-pie"></div></div>');
  mainContentClear();
  addCanvasDivs("delay-line");
  addCanvasDivs("delay-stacked");
  addCanvasDivs("delay-pie");
  addCanvasDivs("error-rate");
  addCanvasDivs("redirection-list");
  drawDelayLineChart(result.wifi.delayLineChart,"#wifi-delay-line");
  drawDelayLineChart(result.mobile.delayLineChart,"#mobile-delay-line");
  drawDelayStackedBarChart(result.wifi.delayStackedBarChart,"#wifi-delay-stacked");
  drawDelayStackedBarChart(result.mobile.delayStackedBarChart,"#mobile-delay-stacked");
  drawDelayPieChart(result.wifi.delayPieChart,"#wifi-delay-pie");
  drawDelayPieChart(result.mobile.delayPieChart,"#mobile-delay-pie");
  drawErrorRateChart(result.wifi.errorRateChart, "#wifi-error-rate");
  drawErrorRateChart(result.mobile.errorRateChart, "#mobile-error-rate");
  drawRedirectionChart(result.wifi.redirectionChart, "#wifi-redirection-list");
  drawRedirectionChart(result.mobile.redirectionChart, "#mobile-redirection-list");
  $("html").getNiceScroll().resize();
}

function drawDelayLineChart(chart,location) {
    var lineChartData = chart.data;
    var seriesData = [];
    for(var date in lineChartData) {
      var tmp=[];
      var tmpDate = new Date(Number(date));
      tmp.push(Date.UTC(tmpDate.getFullYear(),tmpDate.getMonth(),tmpDate.getDate(),tmpDate.getHours(),tmpDate.getMinutes(),tmpDate.getSeconds(),tmpDate.getMilliseconds()));
      tmp.push(lineChartData[date]);
      seriesData.push(tmp);
    }
    $(location).highcharts({
        chart: {
            type: 'spline'
        },
        credits: {
          enabled: false
        },
        title: {
            text: 'Delay Overview'
        },
        plotOptions: {
          line: {
            marker: {
              enabled : true
            }
          }
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: 'Delay'
            },
        },
        tooltip: {
            formatter: function() {
                    return '<b>'+ this.series.name +'</b><br/>'+
                    Highcharts.dateFormat('%y-%M-%d %H:%M:%S:%L', this.x) +': '+ this.y +' ms';
            }
        },
        series: [
          {
          name: "OverallDelay",
          data : seriesData
          }
        ]
    });
}

function drawDelayStackedBarChart(chart,location) {
  var categories = chart.categories;
  var data = chart.series;
  if(typeof data != "undefined" && typeof data[0] != "undefined" && typeof data[1] != "undefined" && typeof data[2] != "undefined") {

      var dnsDelayData = data[0].data;
      for(var i=0;i<dnsDelayData.length;++i){
        if(dnsDelayData[i]<0){
          // do something here
          dnsDelayData[i]=0;
        }
      }


      var handshakeDelayData = data[1].data;
      for(var i=0;i<handshakeDelayData.length;++i) {
        if(handshakeDelayData[i]<0) {
          // do something here
          handshakeDelayData[i]=0;
        }
      }


      var tlsDelayData = data[2].data;
      for(var i=0;i<tlsDelayData.length;++i){
        if(tlsDelayData[i]<0){
          // do something here
          tlsDelayData[i]=0;
        }
      }
  }

  $(location).highcharts({
      chart: {
          type: 'bar'
      },
      credits: {
        enabled: false
      },
      title: {
          text: 'Delay Distribution'
      },
      xAxis: {
          title: {
            text: 'Require ID'
          },
          categories: categories
      },
      yAxis: {
          min: 0,
          title: {
              text: 'Delay Type'
          },
          dateTimeLabelFormats: {
            millisecond: '%H:%M:%S.%L'
          }
      },
      tooltip: {
        valueSuffix: 'ms'
      },
      plotOptions: {
          bar:{
            maxPointWidth: 50,
            minPointLength: 5
          },
          series: {
              stacking: 'normal'
          }
      },
      series: data

  });
}

function drawDelayPieChart(chart,location) {
  var series = chart.series;
    $(location).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        credits: {
          enabled: false
        },
        title: {
            text: 'Average Delay Percentage'
        },
        tooltip: {
            pointFormat: 'Delay: <b>{point.y}ms</b><br />Percentage: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series:series
    });
};

function drawErrorRateChart(chart,location){
  var errorRate=chart.errorRate;
  var data = [];
  var text = 'Error Rate: <b>'+errorRate+'%</b>';
  data.push(['Respose with Error', errorRate]);
  data.push(['Respose without Error', 1-errorRate]);
  $(location).highcharts({
      chart: {
          type: 'pie',
          plotBackgroundColor: null,
          plotBorderWidth: 0,
          plotShadow: false
      },
      credits: {
          enabled: false
      },
      title: {
        text: 'Error Rate'
      },
      subtitle: {
          text: text,
          align: 'center',
          verticalAlign: 'middle',
          y: 80
      },
      colors:[
        '#FF0000', '#A9FF96'
      ],
      tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      plotOptions: {
          pie: {
              dataLabels: {
                  enabled: true,
                  distance: -50,
                  style: {
                      fontWeight: 'bold',
                      color: 'white',
                      textShadow: '0px 1px 2px black'
                  }
              },
              startAngle: -90,
              endAngle: 90,
              center: ['50%', '75%']
          }
      },
      series: [{
          name: 'Error rate: ',
          innerSize: '50%',
          data: data
      }]
  });
}
function drawRedirectionChart(chart, location){
  var redirectionList = chart.redirectionList;
  $(location).append('<table class="table table-hover error-rate-table"> <thead> <tr> <th>Redirection URL</th> </thead>\n<tbody> </tbody> </table>');
  if(redirectionList.length===0){
    $(location+" tbody").append('<tr> <td> '+"None"+'</td> </tr>');
  }
  for(var i=0;i<redirectionList.length;++i) {
     $(location+" tbody").append('<tr> <td> '+redirectionList[i]+'</td> </tr>');
  }
}
// Charts to show: Url index charts end

// Click listener

$(function(){
  $('body').on('click',"a.selectable p" , function() {
    $(".selected").removeClass("selected");
    $(this).addClass("selected");
  });
});
$(function(){
  $('body').on('click',".overview a.selectable" , function() {
    $.ajax({url:"ChartServlet", data:{app: application, type:"overview"},
success: drawOverviewHighCharts, dataType: 'json',type: "POST"});
  });
});
$(function(){
  $('body').on('change',".sortby-selector select" , function() {
    //alert($(".sortby-selector select option:selected").text());
    getUrlIndex($(".sortby-selector select option:selected").text());
  });
});
$(function(){
  $('body').on('click', '.sub-list li a p', function() {
    var pathid = $(this).parents('ul').attr('id');
    var targetUrl = $("a[href='#"+pathid+"']").text()+$(this).text();
    $.ajax({url:"ChartServlet", data:{app:application, type:"urllist", targetUrl:targetUrl}, success: drawUrlHighCharts, dataType: 'json',type: "POST"});
  });
});
$(function(){

$(document).on('mouseover','#scrollbar-side', function () {
    $(this).getNiceScroll().resize();
  });
})

// Click listener end



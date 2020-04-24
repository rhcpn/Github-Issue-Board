$("#btn-chart").click(function () {
  showChart();
});

// 차트 open
function showChart() {
  const url = "/chart";

  ajaxRequest(url, state, label).then(function (array) {
    $(".highcharts-figure").show();

    Highcharts.chart("container", {
      chart: {
        type: "line",
      },
      title: {
        text: "Issue Board",
      },
      subtitle: {
        text: "open / closed count",
      },
      xAxis: {
        categories: array[0],
      },
      yAxis: {
        title: {
          text: "Number",
        },
      },
      plotOptions: {
        line: {
          dataLabels: {
            enabled: true,
          },
          enableMouseTracking: true,
        },
      },
      series: [
        {
          name: "Open",
          data: array[1],
        },
        {
          name: "Closed",
          data: array[2],
        },
      ],
    });
  });
}

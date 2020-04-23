//$(document).ready(function () {
$("#btn-chart").click(function () {
  showChart();
});

let today = moment().format("YYYY / MM / DD");
let today_1 = moment().subtract(1, "days").format("YYYY / MM / DD");
let today_2 = moment().subtract(2, "days").format("YYYY / MM / DD");
let today_3 = moment().subtract(3, "days").format("YYYY / MM / DD");
let today_4 = moment().subtract(4, "days").format("YYYY / MM / DD");

// 차트 open
function showChart() {
  const urlString =
    "https://api.github.com/repos/" + repo + "/issues?per_page=100&state=all";
  const url = "/chart";

  ajaxRequest(token, url, urlString).then(function (array) {
    loader.css("display", "none");
    $(".highcharts-figure").show();

    console.log(array);

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
//});

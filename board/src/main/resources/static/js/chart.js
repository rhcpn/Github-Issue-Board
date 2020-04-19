$(document).ready(function () {
  $(".highcharts-figure").hide();
  $("#btn-chart").hide();
  $("#btn-chart").click(function () {
    $(".highcharts-figure").toggle(function () {
      showChart();
    });
  });

  let today = moment().format("YYYY / MM / DD");
  let today_1 = moment().subtract(1, "days").format("YYYY / MM / DD");
  let today_2 = moment().subtract(2, "days").format("YYYY / MM / DD");
  let today_3 = moment().subtract(3, "days").format("YYYY / MM / DD");
  let today_4 = moment().subtract(4, "days").format("YYYY / MM / DD");

  function showChart() {
    const token = $("#token").val();
    const repo = $("#repo").val();
    const urlString =
      "https://api.github.com/repos/" +
      repo +
      "/issues?per_page=100&state=open" +
      label +
      state;
    const url = "/chart";

    ajaxRequest(token, url, urlString).then(function (array) {
      Highcharts.chart("container", {
        title: {
          text: "Issue Chart",
        },
        xAxis: {
          categories: [today_4, today_3, today_2, today_1, today],
        },
        labels: {
          items: [
            {
              html: "Total issue",
              style: {
                left: "50px",
                top: "18px",
                color:
                  // theme
                  (Highcharts.defaultOptions.title.style &&
                    Highcharts.defaultOptions.title.style.color) ||
                  "black",
              },
            },
          ],
        },
        series: [
          {
            type: "column",
            name: "Open",
            data: [9, 9, 9, 9, 9],
          },
          {
            type: "column",
            name: "Closed",
            data: [4, 3, 3, 9, 0],
          },
          {
            type: "pie",
            name: "Total issue",
            data: [
              {
                name: "All",
                y: 13,
                color: Highcharts.getOptions().colors[5], // Jane's color
              },
              {
                name: "Open",
                y: 200,
                color: Highcharts.getOptions().colors[0], // John's color
              },
              {
                name: "Closed",
                y: 19,
                color: Highcharts.getOptions().colors[1], // Joe's color
              },
            ],
            center: [100, 80],
            size: 80,
            showInLegend: false,
            dataLabels: {
              enabled: true,
            },
          },
        ],
      });
    });
  }
});
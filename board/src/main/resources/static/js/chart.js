$(document).ready(function () {
  $("#btn-chart").click(function () {
    //$(".highcharts-figure").toggle(function () {
    showChart();
    // });
  });

  let today = moment().format("YYYY / MM / DD");
  let today_1 = moment().subtract(1, "days").format("YYYY / MM / DD");
  let today_2 = moment().subtract(2, "days").format("YYYY / MM / DD");
  let today_3 = moment().subtract(3, "days").format("YYYY / MM / DD");
  let today_4 = moment().subtract(4, "days").format("YYYY / MM / DD");

  function showChart() {
    const urlString =
      "https://api.github.com/repos/" + repo + "/issues?per_page=100&state=all";
    const url = "/chart";

    ajaxRequest(token, url, urlString).then(function (array) {
      const openCount = Number(array[0].openCount);
      const closedCount = Number(array[0].closedCount);
      const allCount = openCount + closedCount;

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
            data: [
              Number(array[4].created_at),
              Number(array[3].created_at),
              Number(array[2].created_at),
              Number(array[1].created_at),
              Number(array[0].created_at),
            ],
          },
          {
            type: "column",
            name: "Closed",
            data: [
              Number(array[4].closed_at),
              Number(array[3].closed_at),
              Number(array[2].closed_at),
              Number(array[1].closed_at),
              Number(array[0].closed_at),
            ],
          },
          {
            type: "pie",
            name: "Total issue",
            data: [
              {
                name: "All",
                y: allCount,
                color: Highcharts.getOptions().colors[5], // Jane's color
              },
              {
                name: "Open",
                y: openCount,
                color: Highcharts.getOptions().colors[0], // John's color
              },
              {
                name: "Closed",
                y: closedCount,
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

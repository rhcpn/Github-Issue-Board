$(document).ready(function () {
  $("#btn-chart").click(function () {
    issueCount();
    setTimeout(showChart, 10000);
  });

  let today = moment().format("YYYY / MM / DD");
  let today_1 = moment().subtract(1, "days").format("YYYY / MM / DD");
  let today_2 = moment().subtract(2, "days").format("YYYY / MM / DD");
  let today_3 = moment().subtract(3, "days").format("YYYY / MM / DD");
  let today_4 = moment().subtract(4, "days").format("YYYY / MM / DD");

  let todayOpen = 0;
  let today_1Open = 0;
  let today_2Open = 0;
  let today_3Open = 0;
  let today_4Open = 0;

  let openCount = 0;

  function issueCount() {
    const token = $("#token").val();
    const repo = $("#repo").val();
    const urlStringOpen =
      "https://api.github.com/repos/" +
      repo +
      "/issues?per_page=100s&state=open";
    const url = "/list";

    ajaxRequest(token, url, urlStringOpen).then(function (array) {
      console.log("전체 open issue : " + array.length);
      openCount = array.length;
      for (var i = 0; i < array.length; i++) {
        let created_at = moment(array[i].created_at).format("YYYY / MM / DD");
        if (created_at === today) {
          todayOpen++;
        } else if (created_at === today_1) {
          today_1Open++;
        } else if (created_at === today_2) {
          today_2Open++;
        } else if (created_at === today_3) {
          today_3Open++;
        } else if (created_at === today_4) {
          today_4Open++;
        }
      }
      console.log("오늘 오픈된 이슈 : " + todayOpen);
      console.log("어제 오픈된 이슈 : " + today_1Open);
    });
  }

  function showChart() {
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
          data: [today_4Open, today_3Open, today_2Open, today_1Open, todayOpen],
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
              y: openCount,
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
  }
});

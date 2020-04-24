// issue label & state
let state = "";
let label = "";

// token & repo name
let token;
let repo;

let loader = $("div.loader");

// 차트 영역, chart 버튼, all / open / closed 버튼 숨기기
$(".highcharts-figure").hide();
$("#btn-chart").hide();
$("#state").hide();
$("#download").hide();

// Issue Board, home 클릭 시
$("#board").click(function () {
  state = "";
  label = "";
  printList();
});

// 리스트 조회 클릭 시
$("#btn-list").click(function () {
  token = $("#token").val();
  repo = $("#repo").val();
  printList();
  printLabel();
  $("#list-form").hide();
});

$("#btn-all").click(function () {
  state = $("#btn-all").val();
  printList(label, state);
});

$("#btn-open").click(function () {
  state = $("#btn-open").val();
  printList(label, state);
});

$("#btn-close").click(function () {
  state = $("#btn-close").val();
  printList(label, state);
});

$(document).on("click", "#btn-label", function () {
  label = $(this).val();
  state = "&state=open";
  printList(label, state);
});

// ajax 요청
function ajaxRequest(url, state, label) {
  return $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    data: { token: token, url: url, repo: repo, state: state, label: label },
    beforeSend: function () {
      loader.css("display", "block");
    },
    statusCode: {
      403: function (response) {
        alert("권한이 없습니다.");
      },
      404: function (response) {
        alert("잘못된 요청입니다.");
      },
      500: function (response) {
        alert("서버가 응답하지않습니다.");
      },
    },
  })
    .fail(function () {
      console.log("");
    })
    .always(function () {
      loader.hide();
    });
}

// 이슈 리스트
function printList(label, state) {
  const url = "/list";

  ajaxRequest(url, state, label).then(function (array) {
    $("#btn-chart").show();
    $("#download").show();
    $("#state").show();
    $(".list-group").html("");

    for (var i = 0; i < array.length; i++) {
      var labels = "";
      for (var j = 0; j < array[i].labels.length; j++) {
        labels +=
          "<span class= 'badge' style='background-color: #" +
          array[i].labels[j].color +
          ";'>" +
          array[i].labels[j].name +
          "</span>";
      }
      $(".list-group").append(
        "<a href='" +
          array[i].html_url +
          "' class='list-group-item list-group-item-action'>" +
          array[i].number +
          "<p class='font-weight-bold'>" +
          array[i].title +
          "</p>" +
          moment(array[i].created_at).format("YYYY / MM / DD HH:mm") +
          "<code>" +
          array[i].user.login +
          "</code>" +
          labels +
          "</a>"
      );
    }
  });
}

// 라벨 리스트
function printLabel() {
  const url = "/label";

  ajaxRequest(url, state, label).then(function (array) {
    for (var i = 0; i < array.length; i++) {
      $(".label-group").append(
        "<button type='button' class='badge' id='btn-label' value='&labels=" +
          array[i].name +
          "' style='background-color: #" +
          array[i].color +
          ";'>" +
          array[i].name +
          "</button>"
      );
    }
  });
}

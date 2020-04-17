//$(document).ready(function () {
let state;
let label;

$("#btn-list").click(function () {
  printList();
  printLabel();
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
  console.log($(this).val());
  printList(label, state);
});

// ajax 요청
function ajaxRequest(token, url, urlString) {
  var result;
  return (result = $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    data: { token: token, urlString: urlString },
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
  }));
}

// 이슈 리스트
function printList(label, state) {
  const token = $("#token").val();
  const repo = $("#repo").val();
  const urlString =
    "https://api.github.com/repos/" +
    repo +
    "/issues?per_page=100" +
    label +
    state;
  const url = "/list";

  console.log(urlString);

  ajaxRequest(token, url, urlString).then(
    function (array) {
      $("#list-form").hide();
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
    },
    function (error) {
      console.log(error);
    }
  );
}

// 라벨 리스트
function printLabel() {
  const token = $("#token").val();
  const repo = $("#repo").val();
  const urlLabel = "/label";
  const urlStringLabel =
    "https://api.github.com/repos/mobigen/IRIS-BigData-Platform/labels";

  ajaxRequest(token, urlLabel, urlStringLabel).then(
    function (array) {
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
    },
    function (error) {
      console.log(error);
    }
  );
}
//});

$("#download").click(function () {
  downloadCsv();
});

function downloadCsv() {
  const url = "/download.csv";

  window.location.href =
    "/download.csv?token=" + token + "&url=" + url + "&repo=" + repo;
}

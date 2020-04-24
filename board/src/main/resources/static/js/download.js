$("#download").click(function () {
  console.log(token);
  console.log(repo);
  downloadCsv();
});

function downloadCsv() {
  const urlString =
    "https://api.github.com/repos/" + repo + "/issues?per_page=100&state=all";

  window.location.href =
    "/download.csv?urlString=" + urlString + "&token=" + token;
}

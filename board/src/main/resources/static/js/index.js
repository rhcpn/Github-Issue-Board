/* var main = {
    init : function() {
        var _this = this;

        $('#btn-list').click(function() { 
           _this.list(); 
        }); 
    },

    // 리스트 출력
    list : function() {
        var data = {
            token : $('#token').val(),
            repo : $('#repo').val()
        };

        console.log("test");

        $.ajax({
            type: "POST",
            url: "https://api.github.com/repos/" + repo + "/issues?page=1&per_page=100",
            data: JSON.stringify(data),
            dataType: "json",
            success: function (response) {
                console.log(response);
            }
        });
    }

} */

function printList() {

    var token = $('#token').val();
    var repo = $('#repo').val();
    var urlString = "https://api.github.com/repos/"+repo+"/issues";
    var url = "/list";

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        data : {'token' : token, 'repo' : repo, 'urlString' : urlString},
        headers: {"Authorization": token},
        success: function (response) {
            console.log(response);
            var array = response;
            for(var i=0; i<array.length; i++) {

                $('.list-group').append("<a href='"+ array[i].html_url +"' class='list-group-item list-group-item-action'>"
                                        +array[i].number
                                        + "<p class='font-weight-bold'>"
                                        +array[i].title
                                        + "</p>"
                                        +array[i].created_at
                                        + "<code>"
                                        +array[i].user.login
                                        + "</code>"
                                        )
                for(var j=0; j<array[i].labels.length; j++) {
                    $('.list-group').append("<span class='badge' id='tag'>"
                                        +array[i].labels[j].name)
                                    .append("</span")
                                    .append("</a>");

                }
            }
             
        }
    });

}
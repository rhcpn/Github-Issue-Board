$(document).ready(function() {

    $('#btn-list').click(function() { 
        printList();
        printLabel();
    });

    $('#btn-all').click(function(){
        printList();
    });

    $('#btn-open').click(function () { 
        const state = $('#btn-open').val();
        printList(state);
        console.log("오픈");        
    });

    $('#btn-close').click(function () { 
        printList();        
    });


    // 이슈 리스트
    function printList(state) {

        var token = $('#token').val();
        var repo = $('#repo').val();
        var urlString = "https://api.github.com/repos/"+repo+"/issues?" + state;
        var url = "/list";

        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            contentType : 'application/json; charset=utf-8',
            data : {'token' : token, 'repo' : repo, 'urlString' : urlString},
            headers: {"Authorization": token},
            statusCode : {
                403 : function(response) {
                    alert("권한이 없습니다.");
                },
                404 : function(response) {  
                    alert("잘못된 요청입니다.");
                },
                500 : function(response) {
                    alert("서버가 응답하지않습니다.");
                }
            },
            success: function (response) {
                var array = response;
                console.log(array.length);
                console.log(urlString);

                $('#list-form').hide();
            
                $('.list-group').html("");
                for(var i=0; i<array.length; i++) {

                    var labels = "";

                    for(var j=0; j<array[i].labels.length; j++) {
                        labels += "<span class= 'badge' style='background-color: #" + array[i].labels[j].color + ";'>" 
                                            + array[i].labels[j].name 
                                            + "</span>"
                    } 
                    $('.list-group').append("<a href='"+ array[i].html_url +"' class='list-group-item list-group-item-action'>"
                                            + array[i].number
                                            + "<p class='font-weight-bold'>"
                                            + array[i].title
                                            + "</p>"
                                            +moment(array[i].created_at).format('YYYY / MM / DD HH:mm')
                                            + "<code>"
                                            + array[i].user.login
                                            + "</code>"
                                            + labels
                                            + "</a>"); 
                }
            }
        });
    }

    // 라벨 리스트
    function printLabel() {    
        var token = $('#token').val();
        var repo = $('#repo').val();
        var urlLabel = "/label";
        var urlStringLabel = "https://api.github.com/repos/mobigen/IRIS-BigData-Platform/labels";

        $.ajax({
            type: "GET",
            url: urlLabel,
            dataType: "json",
            contentType : 'application/json; charset=utf-8',
            data: {'token' : token, 'repo' : repo, 'urlString' : urlStringLabel},
            headers: {"Authorization": token},
            statusCode : {
                403 : function(response) {
                    alert("권한이 없습니다.");
                },
                404 : function(response) {  
                    alert("잘못된 요청입니다.");
                },
                500 : function(response) {
                    alert("서버가 잘못되었습니다.");
                }
            },
            success: function (response) {
                console.log(response);
                var array = response;

                for(var i=0; i<array.length; i++) {
                
                    $('.label-group').append("<a href='javascript:printList();'"
                                            + "<span class= 'badge' style='background-color: #" 
                                            + array[i].color + ";'>"
                                            + array[i].name
                                            + "</span>"
                                            + "</a>");
                } 
            }
        });

    }
});
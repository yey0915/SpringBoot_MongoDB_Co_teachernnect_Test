$(document).ready(function(){
		init()
		FindAllFileName()
	})
	
	    function loadImage() {
            var input = document.getElementById("image");
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = document.getElementById("preview");
                    img.src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        }
	
	
function FindAllFileName () {
            $.ajax({
                url: "/findFileNameAll",
                type: "GET",
                success: function(data) {
					console.log(data)
					for (var i = 0; i < data.length; i++) {
					  var filename = data[i];
					  var str = filename.substring(filename.lastIndexOf('.') + 1);
					  console.log('str :  '+ str)
					  if(str=='mp4'){
						var video =$('<video>', {src: '/images/' + filename});
						video.attr('controls', true);
						$('#ImageTest').append(video);
					} else {
					  var img = $('<img>', {src: '/images/' + filename});
					  $('#ImageTest').append(img);
					  }
}
              
                },
                error: function(data) {
	
	
                }
            });
        }
	
function dbUpdateForm(id){
	location.href='/updateForm/'+id;
	}

$("#dbSearchBtn").click(function(){
	
			var searchData = {
		"searchContent":$("#searchContent").val(),
		"searchDB":$("#searchDB option:selected").val()
	}
	console.log(searchData)
	$.ajax({
		type:"post",
		url:"/searchDb",
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(searchData)
	})
	.done(function(resp){
	 	 var str = "<table class='table table-hover mt-3 ' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str += "</tr>"
			})
			str += "</table>"
			$("#searchResult").html(str);
			})
	.fail(function(){
		alert("디비 검색 실패")
	});
	});

$("#listBtn").click(function(){
	location.href='/hello'
	});

$("#dbUpdateBtn").click(function(){

	var data={
			"id":$("#dbId").val(),
			"title":$("#dbTitle").val(),
			"message":$("#dbMessage").val()
	}
	
	$.ajax({
		type:"post",
		url:"/updateDb",
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(data)
	})
	.done(function(resp){
			location.href='/hello/'
			})
	.fail(function(){
		alert("디비 수정 실패")
	});
	});
	
	
function dbUpdate(id){
	$.ajax({
		type:"get",
		url:"/updateForm/"+id,
	})
	.done(function(resp){
		  location.href='/updateForm/id';
				var str = "<table class='table table-hover mt-3' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str+= "<td><a href='javascript:dbUpdateGo("+val.id+")'>수정</a></td>"
				
				str += "</tr>"
			})
			str += "</table>"
			$("#dbResult").html(str);
		
	})
	.fail(function(){
		
	})
}
	
function dbDel(id){
	$.ajax({
		type:"delete",
		url:"/dbDelete/"+id,
	})
	.done(function(resp){
		alert(id+"번 글 삭제 완료");
		init();
	})
	.fail(function(){
		alert("삭제 실패")
	})
}
	
var init = function(){
		$.ajax({
			type:"get",
			url:"/findAll",
			dataType:"JSON",
			contentType:"application/json;charset=utf-8",
		})
		.done(function(resp){
			//alert("resp"+resp)
			var str = "<table class='table table-hover mt-3  ' border=1>";
				str +="<th>" +"아이디"+"</th>"
				str +="<th>" +"제목"+"</th>"
				str +="<th>" +"메세지"+"</th>"
				str +="<th>" +"수정"+"</th>"
				str +="<th>" +"삭제"+"</th>"
			$.each(resp,function(key,val){
				str += "<tr>"
				str += "<td>" + val.id + "</td>"
				str += "<td>" + val.title + "</td>"
				str += "<td>" + val.message + "</td>"
				str+= "<td><a href='javascript:dbUpdateForm("+val.id+")'>수정</a></td>"
				str+= "<td><a href='javascript:dbDel("+val.id+")'>삭제</a></td>" 
				
				
				str += "</tr>"
			})
			str += "</table>"
			$("#dbResult").html(str);
		})
	};

$("#dbInsertBtn").click(function(){
	

	var data={
			"id":$("#dbId").val(),
			"title":$("#dbTitle").val(),
			"message":$("#dbMessage").val()
			
	}
	
	$.ajax({
		type:"post",
		url:"/insertDb",
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(data)
	})
	.done(function(resp){
				init();
			})
	.fail(function(){
		alert("디비 추가 실패")
	});
});

$("#dbFindAllBtn").click(function(){

	$.ajax({
		type:"post",
		url:"/insertDb",
		contentType:"application/json;charset=utf-8",
		data:JSON.stringify(data)
	})
	.done(function(resp){
		alert("디비 추가 성공");
			})
	.fail(function(){
		alert("디비 추가 실패")
	});
});   
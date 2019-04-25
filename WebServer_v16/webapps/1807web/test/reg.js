/**
 * reg.js
 */
$(function(){
	var flag = false;
	//文本框得到焦点事件
	$("form input").focus(function() {
		$(this).next().css("background-color","#B6AEAB");
		if($(this).prev().text()=="用户名:"){
			$(this).next().text("用户名长度在6到9位之间");
		}else if($(this).prev().text()=="密码:"){
			$(this).next().text("密码长度在6到12位之间");
		}else if($(this).prev().text()=="确认密码:"){
			$(this).next().text("密码长度在6到12位之间");
		}else if($(this).prev().text()=="邮箱:"){
			$(this).next().text("请输入合法的邮箱地址");
		}else if($(this).prev().text()=="手机号:"){
			$(this).next().text("请输入合法的手机号");
		}
	});
	
	//文本框失去焦点
	$("form input").blur(function() {
		if($(this).prev().text()=="用户名:"){
			var reg = /^\w{6,9}$/g;
			if(reg.test($(this).val())){
				$(this).next().css("background-color","#0d0");
				$(this).next().text("用户名格式正确");
			}else{
				if($(this).val()==""){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("用户名不能为空");
				}
				if($(this).val().length<$(this).attr(minlength)){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("用户名不能少于6位");
				}
			}
		}else if($(this).prev().text()=="密码:"){
			var reg = /^\w{6,12}$/g;
			if(reg.test($(this).val())){
				$(this).next().css("background-color","#0d0");
				$(this).next().text("密码格式正确");
			}else{
				if($(this).val()==""){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("密码不能为空");
				}
				if($(this).val().$(this).attr(minlength)){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("密码长度尽量不要少于6位");
				}
			}
		}else if($(this).prev().text()=="确认密码:"){
			var reg = /^\w{6,12}$/g;
			if(reg.test($(this).val())){
				if($(this).val()==$(this).parent().prev().children().eq(1).val()){
					$(this).next().css("background-color","#0d0");
					$(this).next().text("密码格式正确");
				}else{
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("两次密码不一致");
				}
			}else{
				if($(this).val()==""){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("密码不能为空");
				}
				if($(this).val().$(this).attr(minlength)){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("密码长度尽量不要少于6位");
				}
			}
		}else if($(this).prev().text()=="邮箱:"){
			var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
			if(reg.test($(this).val())){
				$(this).next().css("background-color","#0d0");
				$(this).next().text("邮箱格式正确");
			}else{
				if($(this).val()==""){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("邮箱不能为空");
				}else{
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("邮箱格式不正确");
				}
			}
		}else if($(this).prev().text()=="手机号:"){
			var reg = /^1[3|4|5|8][0-9]\d{4,8}$/;
			if(reg.test($(this).val())){
				$(this).next().css("background-color","#0d0");
				$(this).next().text("手机号格式正确");
				flag = true;
			}else{
				if($(this).val()==""){
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("手机号不能为空");
				}else{
					$(this).next().css("background-color","#ff0000");
					$(this).next().text("手机号格式不正确");
				}
			}
		}
	})
	//提交事件
	$(".btn  input").click(function() {
		if(flag){			
			$(".form").submit();
		}
	})
})

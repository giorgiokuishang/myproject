<%@ page language="java" pageEncoding="UTF-8"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
	<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>水资源管理系统</title>
		 <!-- 引入头部icon图标 -->	
		 <link rel="stylesheet" href="<%=basePath%>common/zui/fonts/favicon.ico">
		 <!-- 引入icon图标 -->
		 <link rel="stylesheet" href="<%=basePath%>common/zui/fonts/iconfont.css">
		 <!-- 引入zui样式 -->
		 <link rel="stylesheet" href="<%=basePath%>common/zui/css/zui.lite.min.css">
		<style>
		html body{
			height:100%;
			overflow: hidden;
		   }
		    .container-fluid{
               padding-left:0px;
               padding-right:0px;
		    }
		     .container-fluid img{
               height:100%;
		    }
           .container-fluid .row  .col-md-12{
               position: absolute;
               top:0px;
               left:0px;
               width: 100%;
               height:64px;
		    }
           .container-fluid .row  .col-md-12 .row .col-md-4{    
               height:64px;
               margin-top:18px; 
		    }
		   .container-fluid .row  .col-md-12 .row .col-md-4 img{   
		   	    margin-left:24px;
		   	    margin-top:3px;
		        float:left; 
		    }
		     .container-fluid .row  .col-md-12 .row .col-md-4 p{ 
		            margin-top: 8px;
				    margin-left: 10px;
				    margin-bottom: 0px;
				    font-size: 24px;
				    float: left;
		    }
		     .container-fluid .row  .col-md-12 .row .col-md-4 p+span{
		     	    margin-top: -10px;
				    padding-left: 13px;
				    padding-right: 0px;
				    font-size: 13px;
				    color:#999999;
				    border: 0px;
				}
           		     .container-fluid .row  .col-md-12 .row .col-md-4 span{ 
				    display: inline-block;
				    margin-top: 25px;
				    padding-right: 16px;
				    padding-left: 16px;
				    border-left: 1px solid #b6b6b6;
		    }
		       .container-fluid .row .col-md-12 .row .col-md-4 span:first-child{   
				     border-left:0px;
				     margin-left: 84px;
		    }
          #row-second{
      	        position: absolute;
			    top: 134px;
			    width: 100%;
			    height: 380px;
			    margin-right: -10px;
			    margin-left: -10px;
			    color:#FFFFFF;
          }
          #row-second .col-md-6 h2{
          	    font-size:30px;
			    margin-left: 109px;
			    margin-top: 89px;
			    margin-bottom: 30px;
			    white-space: nowrap!important;
          }
           #row-second .col-md-6 h4{
           	    font-size:14px;
           	    font-weight: 100;
			    margin-left: 109px;
			    white-space: nowrap;
          }
            #row-second .col-md-6 .login-right{
           	   width: 300px;
           	   height:360px;
           	   margin-top: 10px;
           	   margin-left:180px; 
           	   background: white;
           	   border-radius:10px;
          }
           #row-second .col-md-6 .login-right img{
           	   width: 60px;
           	   height:60px;
           	   margin-left: 130px;
   			   margin-top: 26px;
   			   margin-bottom: 26px;
          }
           #row-second .col-md-6 .login-right h3{
           	   color: #333333;
           	   font-weight: 550;
           	   font-size:18px;
           	   margin-top: 0px;
          }          
         .nav>li>a{
         	float:right;
         }          
        .nav>li>a:nth-child(1){
        	position: relative;
         	margin-left: 45px;
         	color: #368ae3;
         }
        .tab-content{
         	position: relative;
         	top: 20px;
         }
         .tab-content #tab2Content1{
         	position: absolute;
         	z-index:3333;
         	top:0px;
         	left: 0px;
         	width: 100%;
         }       
         .form-control{
         	width: 80%;
         	margin-left:10%; 
         	padding-left:26px;
         }
         #btn-p{
         margin-top:10px;
         	margin-left: 10%;
         	width: 80%;
         }
         #banben{
         	line-height: 57px;
         	margin-right:30px; 
         	text-align: right;
         	color:#3f82e7;
         }
         #erweima{
         	margin-left: 70px!important;
         	margin-top: 0px!important;
            margin-bottom: 0px!important;
         	width: 160px!important;
         	height: 160px!important;
         	background: red;
         }
        #erweimaimg{
            position: absolute;
            top:0px;
            left:59px;
            width: 10px!important;
            height:10px!important;
            margin-left: 0px!important;
		    margin-top: 0px!important;
		    margin-bottom: 0px!important;
         }        
          #row-third{
      	        position: relative;
      	        background: red;
      	        color:white;
			    bottom: 180px;
			    width: 100%;
			    height:20px;
			    margin-right: auto;
			    margin-left: auto;
          }
          #row-third .col-md-12{
          	    height: 120px;
			    background: white;
			    color: #838383;
			    margin-top: -21px; 
			    white-space: nowrap;

          }
          
           #row-third .col-md-12 p:first-child{
           	    margin-top:30px; 
          	    color:#5b5b5b;
          }                            
          .has-icon-left label{
            position: relative;
		    top: -52px;
		    left: -97px;		  		 
          }
           .has-icon-right label{
            position: relative;
		    top: -52px;
		    left: -97px;		  	 
          }
          .has-icon-right label img{           
		    display: inline-block;
		    width: 18px!important;
		    height: 20px!important;
          }
          
          .has-icon-left label img{           
		    display: inline-block;
		    width: 20px!important;
		    height: 20px!important;
          }
       
          #hasLeft{
           height:60px;
          }
           #hasright{
           height:40px;
          }
          @media (max-width: 1024px) {
    			 #row-third{
		          bottom:50px;
		    }
		  }
		   @media (max-width: 1024px) {
    			#row-second .col-md-6 h4,
    			#row-second .col-md-6 h2{
		         margin-left: 60px;
		    }
		    #row-second .col-md-6 .login-right{
		    	margin-left: 120px;
		    }
		    .container-fluid .row .col-md-12 .row .col-md-4 p{
		    	 letter-spacing:normal!important;
		    }
		    .container-fluid .row .col-md-12 .row .col-md-4 span{
		    	    margin-right: 35px;
		    }
		    #row-second{
		    	top:97px;
		    }
		    .container-fluid .row .col-md-12 .row .col-md-4{
		    	     margin-top: 0px; 
		    }
		    .container-fluid .row .col-md-12 .row .col-md-4 span{
		    	    margin-right:0px;
		    }
		    .container-fluid .row .col-md-12 .row .col-md-4 span:nth-child(1){
		    	        margin-left: -37px;
		    }
		     .container-fluid .row  .col-md-12 .row .col-md-4 img{   
		   	    margin-left:0px;
		    }
		  }
		</style>
	</head>
	<body>
		<div class="container-fluid">  
		     <img src="<%=basePath %>common/images/login/bg.png" width="100%" height="100%" class="img-responsive" alt="响应式图片测试">
		     <div class="row">
		     	 <div class="col-md-12  col-lg-12 col-sm-12 col-xs-12">
		     	 	 <div class="row">
		     	 		<div class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> 
		     	 			 <div class="row">
		                            <img src="<%=basePath %>common/images/login/LOGO.png" alt="">
                                    <p>水资源分析模拟综合平台</p>
                                    <span id="spanid">SHUIZIYUANFENXIMONIZONGHEPINGTAI</span>
                             </div>
		     	 		</div>
		     	 		<div class="col-md-4 col-lg-4 hidden-sm hidden-xs"></div>
		     	 		<div class="col-md-4 col-lg-4 col-sm-6 col-xs-6">
		     	 			<span>资源分析</span>
		     	 			<span>数据展示</span>
		     	 			<span>模拟综合</span>
		     	 			<span>实时监控</span>
		     	 		</div>
		     	 	 </div>
		     	 </div>
		     </div>
		     <div class="row" id="row-second">
		     	  <div class="col-md-6 col-lg-6 col-sm-6 col-xs-6">
		     	  	 <h2>现代化的基础设施,高效率的分析平台</h2>
		     	  	 <h4>水资源综合信息化系统主要包括水资源信息服务、水资源业务管理、水资源应急管理、综合数据应用组成。</h4>
		     	  	 <h4>通过对管理的基础信息进行多种方式的业务信息分析和表达,进行水资源模拟评测功能。</h4>
		     	  	 <h4>工作效率同比提高30%。</h4>
		     	  </div>
		     	  <div class="col-md-6 col-lg-6 col-sm-6 col-xs-6">
		     	  	  <div class="login-right">
		     	  	  	    <img src="<%=basePath %>common/images/login/LOGO2.png" alt="">
		     	  	  	    <h3 class="text-center">水资源分析模拟综合平台</h3> 	  	    
								<div class="tab-content">
								    <div class="tab-pane fade active in"  id="tab2Content1">
								      <form action="<%=basePath%>login/system!login.do" id="login_form" method="post" > 
								      	<div class="input-control has-icon-left" id="hasLeft">
										  <input id="inputAccountExample1" name="mSysUser.userName" type="text" class="form-control" placeholder="用户名">
										  <label for="inputAccountExample1" class="input-control-icon-left"> <img src="<%=basePath%>common/images/login/zhanghu.png" alt=""></label>
										</div>		
								      	<div class="input-control has-icon-right" id="hasright">    
										  <input id="inputPasswordExample1" name="mSysUser.userPwd"  type="password" class="form-control" placeholder="密码">
									
										  <label for="inputPasswordExample1" class="input-control-icon-right"><img src="<%=basePath%>common/images/login/mima.png" alt=""></label>
										</div>
											  <font color="red " style="display: none;margin-left: 28px;" id="login_error_font">提示 : 用户名或密码错误，请重新输入</font>
											    <font color="green " style="display: none;margin-left: 28px;" id="login_success_font">登录成功，等待跳转...</font>
										<button type="button" id="btn-p" class="btn btn-primary"  onclick="loginCheck()">登&nbsp;&nbsp;录</button> 
			                            <div id="banben"><a href="<%=basePath %>register.jsp" role="button">注册</a></div>
			                            </form>
									</div>
								         
								</div>
		     	  	  </div>
		     	  </div>
		     </div>
		     <div class="row text-center" id="row-third">
		     	 <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
<!-- 		     	     <p>技术支持 | 蓝宇汇通科技有限公司 | 蓝宇汇通服务中心 | 电话：010-68273370 邮箱：lyht@lanyuhuitong.com</p> -->
		     	     <p>Copyright © 中国电建集团中南勘测设计研究院有限公司, All Rights Reserved.</p>
		     	 </div>
		     </div>
		</div>
	</body>
	<script type="text/javascript">
	function loginCheck(){
	 var json={
			 "mSysUser.userName":$("*[name='mSysUser.userName']").val(),
	 		"mSysUser.userPwd":$("*[name='mSysUser.userPwd']").val()
	 }
	 var url="<%=basePath%>login/system!check.do" ;
		$.ajax({
			type:"POST",
			dataType:"JSON",
			data:json,
			url:url,
			success:function(res){
				if(res.mark=="error"){//登录失败
					$("#login_error_font").show();// 提示
					$("#login_success_font").hide()
				}else{
					$("#login_error_font").hide();//隐藏
					$("#login_success_font").show();// 提示
					document.getElementById("login_form").submit();//如果正确,提交
				}
				
			}
		})
	}

	</script>
	<!-- 引入jquery.js样式 -->
	 <!-- 解决ie -->
		<!--  [if lt IE 9]> -->
		    <script src="<%=basePath %>common/zui/lib/ieonly/html5shiv.js"></script>
		    <script src="<%=basePath %>common/zui/lib/ieonly/respond.js"></script>
		    <script src="<%=basePath %>common/zui/lib/ieonly/excanvas.js"></script>
		<!--   <![endif] -->
	<script src="<%=basePath %>common/zui/lib/jquery/jquery.js"></script>
    <!-- 引入zui.js样式 -->
	<script src="<%=basePath %>common/zui/js/zui.lite.min.js"></script>
	</html>	
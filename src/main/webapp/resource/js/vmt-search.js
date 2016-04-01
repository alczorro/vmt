//数据加载器定义
				var Loader=function(option){
					this.option=option;
					var self=this;
					//是否是搜索状态
					this.searchMode=false;
					//已经加载过的字母
					this.loadedData={};
					//当前浏览着的字母
					this.currentLetter='a';
					//tmpl 配置文件
					this.tmplOption={
							getHighestName: function(){
								var display=(this.data.currentDisplay).split(',');
								return (self.option.scope.indexOf(",")>-1)?display[0]:display[display.length-1];
							},
							encode:function(str){
								return encodeURIComponent(str);
							},
							getChoice:function(){
								var selectedItem=self.option.selected;
								if(selectedItem.isExists(this.data.oid)){
									return "icon-ok";
								}
								return "icon-plus0";
							},
							getStatus:function(){
								switch(this.data.status){
								case 'true':return '已确认';
								case 'false':return '未确认';
								case 'refuse':return '已拒绝';
								case 'apply':return '申请加入';
								}
							},
							getCoreMailStatus:function(){
								switch(this.data.accountStatus){
									case 'normal':return '正常';
									case 'locked':return '锁定';
									case 'stop':return '停用';
								}
							}
						};
					//下一个字母
					this.plusLetter=function(){
						if(self.currentLetter=='other'||self.currentLetter=='#'){
							return false;
						}
						if(self.currentLetter=='z'){
							self.currentLetter='other';
							return true;
						}
						self.currentLetter=String.fromCharCode(self.currentLetter.charCodeAt(0)+1);
						return true;
					};
					//前一个字母
					this.minusLetter=function(){
						if(self.currentLetter=='a'){
							return false;
						}
						if(self.currentLetter=='other'||self.currentLetter=='#'){
							self.currentLetter='z';
							return true;
						}
						self.currentLetter=String.fromCharCode(self.currentLetter.charCodeAt(0)-1);
						return true;
					};
					//获取列表中最后一个可见的letter
					this.firstVisibleLetter=function(){
						return $('.slider-content>ul>li:visible:first').attr("id");
					};
					//获取列表中第一个可见的letter
					this.lastvisibleLetter=function(){
						return $('.slider-content>ul>li:visible:last').attr("id");
					};
					//加载当前字母的数据
					this.loadData=function(loadedCallBack,isDown){
						var cl=self.currentLetter;
						var flag=isDown==null?true:isDown;
						self.ajaxRequest(function(data){
							if(data==null||data.length==0||data==''){
								if(flag){
									if(!self.plusLetter()){
										return
									}
								}else{
									if(!self.minusLetter()){
										return;
									}
								}
								self.loadData(loadedCallBack, isDown);
								return;
								
							}
							if(!$('#'+cl).is(":hidden")){
								return;
							}
							$('#'+cl).show();
							var $dest=$('#'+cl+"_result");
							$("#user_li").tmpl(data,self.tmplOption).appendTo($dest);
							$dest.children("li:even").addClass("even");
							if(loadedCallBack!=null){
								loadedCallBack();
							};
						});
					},
					//移动到该字母的地方
					this.move=function(ancolLetter){
						var height=0;
						$('.slider-content>ul>li:visible').each(function(i,n){
							if($(n).attr('id')==ancolLetter){
								$('.slider-content').scrollTop(height+1);
								return false;
							}
							height+=$(n).height();
						});
					},
					//初始化
					this.init=function(){
						if(self.currentLetter=='other'||self.currentLetter=='#'){
							self.loadData();
						}else{
							self.loadData(function(){
								var staffHeight = $(".slider-content").height();
								var ulHeight=$(".slider-content>ul").height();
								if(ulHeight<staffHeight+100){
									self.plusLetter();
									self.init();
								};
							},true);
						}
					};
					//预加载，往上加载几个字母
					this.preLoad=function(n){
						if(n==null){
							n=1;
						}
						var orgCurrentLetter=self.currentLetter;
						var $content=$('.slider-content');
						var $ul=$content.children("ul");
				        var  beforeHeight=$ul.height();
						var flag=true;
				        while(flag=self.plusLetter()){
							self.loadData(null,true);
							var afterHeight=$ul.height();
							if(afterHeight-beforeHeight>$content.height()){
								break;
							}
							
						}
						self.currentLetter=orgCurrentLetter;
						//认为是数据不够未加载满一屏
						if(!flag){
							while(self.minusLetter()){
								self.loadData(null,false);
								var afterHeight=$ul.height();
								if(afterHeight-beforeHeight>$content.height()){
									break;
								}
							}
						}else{
							for(var i=0;i<n;i++){
								self.minusLetter();
								self.loadData(null,false);  
							}
						}
						self.currentLetter=(orgCurrentLetter=='#'?'other':orgCurrentLetter);
						self.loadData(function(){
							self.move(self.currentLetter);
						}, true);
					},
					//清除可见结果
					this.clear=function(){
						for(var i=0;i<26;i++){
							var letter=String.fromCharCode(97+i);
							$('#'+letter).hide();
							$('#'+letter+"_result").html("");
						};
						$('#other').hide();
						$('#other_result').html("");
						$("#search").hide();
						$('#search_result').html("");
					},
					//销毁对象
					this.destroy=function(){
						self.clear();
						self.loadedData={};
						self.currentLetter='a';
						$('#search_button').unbind('click');
					};
					//ajax
					this.ajaxRequest=function(callBack){
						var cacheData=self.loadedData[self.currentLetter];
						if(cacheData!=null){
							callBack(cacheData.data);
							return;
						}
						$.ajax({
							 url : "view/"+self.currentLetter,
							 type : "POST",
							 data:{
								 scope:self.option.scope,
								 count:self.option.count
							 },
							 async:false,
							 success : function(data){
								 	 self.loadedData[self.currentLetter]={'data':null};
									 $(data).each(function(i,n){
										 if(n.letter=='#'){
												n. letter='other';
										 }
										 self.loadedData[n.letter]={'data':n.data};
									 });
								    callBack(self.loadedData[self.currentLetter].data);
								
							 }
						 });
					};
					////绑定滚动事件
					$('.slider-content').scroll(function(){
						if(self.searchMode){
							return;
						}
						var nScrollHight = $(this)[0].scrollHeight;
					    var nScrollTop = $(this)[0].scrollTop;
					    var nDivHight = $(this).height();
				         if(nScrollTop + nDivHight+10 >= nScrollHight){
				           self.currentLetter=self.lastvisibleLetter();
				           self.plusLetter();
				           self.loadData();
				         }else if(nScrollTop==0){
				           self.currentLetter=self.firstVisibleLetter();
				           self.minusLetter();
				           var $ul=$(this).children("ul");
				           var  beforeHeight=$ul.height();
				           self.loadData(null, false);
				           var afterHeight=$ul.height();
				           $(this).scrollTop(afterHeight-beforeHeight+1);
				         }
				    });
					//绑定侧栏字母点击事件
					$('.navLetter').live('click',function(){
						self.clear();
						self.currentLetter=$(this).html();
						self.preLoad(1);
					});
					//绑定searchKeyword按钮
					$('#search_button').on('click',function(){
						var scope=$(".sub-team>li[class=active]>a").attr("dn");
						if(scope==null){
							scope=self.option.scope;
						}
						self.searchMode=true;
						$.post("search/local",{
								 'keyword':$('#keyword').val(),
								 'scope':scope
							 }).done(function(data){
								 self.clear();
								 if(data&&data.length){
									 $('#slider').show();
									 $("#search").show();
									 $("#noResultMsg").hide();
									 $("#user_li").tmpl(data,self.tmplOption).appendTo("#search_result");
									 $('#search_result li:even').addClass("even");
								 }else{
									 $('#slider').hide();
									 $("#noResultMsg").show();
								 }
							 });
					});
					//bind enter function
					$('#keyword').bind('keyup', function(event){
						   if (event.keyCode=="13"){
							   $("#search_button").click();
						   }
						   if($(this).val()==''){
							   $("#noResultMsg").hide();
							   $('#slider').show();
							   self.searchMode=false;
							   self.clear();
							   self.currentLetter='a';
							   self.init();
						   };
					});
				};
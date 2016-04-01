<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="WEB-INF/tld/vmt.tld" prefix="vmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<!DOCTYPE html>
<html>
	<head>
		<title>组织通讯录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="header.jsp"/>
	</head>
	<body class="vmt">
		<jsp:include page="banner.jsp"/>
		<div class="container main help">
			<h2 class="login-title">帮助中心</h2>
			<div class="help-content">
		   		<div class="left-nav">
					<ul class="nav">
						<li class="active"><a class="commonQA">常见问题</a></li>
						<li><a class="changeLog">更新日志</a></li>
						<li><a class="api" href="${context }/resource/doc/vmt.zip" target="_blank">API文档下载</a></li>
					</ul>
				</div>
		
				<div class="right-content">
					<div class="commonQA">
						<ol>
							<li>
								<h4>组织结构与群组有什么区别?</h4>
								<p>组织结构对应于实体组织机构，支持多级部门，成员可属于部门或者组织机构。群组直接包括成员，没有下级部门。</p>
							</li>
						</ol>
					</div>
					
					<div class="changeLog" style="display:none;">
					<p>2015年9月1日</p>
					<ul>
						<li>发布版本7.1.13</li>
						<li>支持HTTPS安全连接</li>
					</ul>
					<p>2014年11月17日</p>
					<ul>
						<li>发布版本7.1.12p2</li>
						<li>修复手机号隐藏时，消息发送不正常的BUG</li>
						<li>修复群发邮件时附件名称编码引起长度不够的BUG</li>
					</ul>
					<p>2014年9月23日</p>
					<ul>
						<li>发布版本7.1.12p1</li>
						<li>短信群发，邮件群发新增全选功能</li>
						<li>新增更新团队描述API</li>
					</ul>
					<p>2014年8月8日</p>
					<ul>
						<li>发布版本7.1.12</li>
						<li>新增邮件群发及推送到科信，以及相关的管理功能</li>
						<li>添加已有组织机构成员时，支持按部门进行选取</li>
						<li>新增成员信息共享设置，隐藏手机号码选项</li>
					</ul>
					<p>2014年7月17日</p>
					<ul>
						<li>发布版本7.1.11p4</li>
						<li>添加成员时优先使用管理员添加的成员名称</li>
					</ul>
					<p>2014年7月14日</p>
					<ul>
						<li>发布版本7.1.11p3</li>
						<li>修复成员信息共享设置为成员不可见时,不返回组织信息的BUG</li>
					</ul>
					<p>2014年7月11日</p>
					<ul>
						<li>发布版本7.1.11p2</li>
						<li>修复创建群组同步不到团队文档库的BUG</li>
						<li>修复密码中有特殊字符转译失败的BUG</li>
						
					</ul>
					<p>2014年7月9日</p>
					<ul>
						<li>发布版本7.1.11p1</li>
						<li>更改邮箱账号验证规则</li>
					</ul>
					<p>2014年6月12日</p>
					<ul>
						<li>发布版本7.1.11</li>
						<li>新增短信群发允许手动输入电话号码</li>
						<li>新增短信群发搜索用户</li>
					</ul>
					<p>2014年6月10日</p>
					<ul>
						<li>发布版本7.1.10</li>
						<li>新增短信群发功能</li>
					</ul>
					<p>2014年5月27日</p>
					<ul>
						<li>发布版本7.1.9p2</li>
						<li>修复通过API包查询应用有误的BUG</li>
					</ul>
					<p>2014年5月23日</p>
					<ul>
						<li>发布版本7.1.9p1</li>
						<li>新增应用图标尺寸支持（20*20）</li>
					</ul>
					<p>2014年5月9日</p>
					<ul>
						<li>发布版本7.1.9</li>
						<li>新增组织机构应用图标管理和个性化</li>
						<li>修复新创建邮箱账号时，已有UMT用户的邮箱不能正确创建邮箱的BUG</li>
					</ul>
					

					<p>2014年3月31日</p>
					<ul>
						<li>发布版本7.1.8</li>
						<li>支持用户申请邮箱账号与管理员审核</li>
						<li>支持批量编辑</li>
						<li>支持批量删除邮箱账号</li>
						<li>改进批量导入与批量修改</li>
					</ul>
					<p>2014年3月14日</p>
						<ul>
							<li>发布版本7.1.7</li>
							<li>支持同一用户可属于多个组织机构</li>
							<li>支持适应移动端尺寸的组织机构与群组创建页面</li>
							<li>支持科信开通申请审核与组织机构/群组应用</li>
							<li>组织机构多LOGO支持</li>
							<li>支持组织机构禁用某科信用户</li>
							<li>针对以Coremail组织通讯录的组织机构管理员，限制只能为当前域的邮箱</li>
							<li>后台系统管理界面，把相应的域名在界面显示，方便管理</li>
						</ul>
						<p>2014年1月16日</p>
						<ul>
							<li>发布版本7.1.6p4</li>
							<li>部门列表过多时，末尾部门显示不全的BUG</li>
							<li>移动部门的部门列表统一为按权重排序</li>
						</ul>
						<p>2014年1月10日</p>
						<ul>
							<li>发布版本7.1.6p3</li>
							<li>修正移动用户选择错误</li>
							<li>搜索增加职称条件</li>
						</ul>
						<p>2014年1月3日</p>
						<ul>
							<li>发布版本7.1.6p2</li>
							<li>修正手动添加用户时提示信息有误的BUG</li>
							<li>修正过期时间验证BUG</li>
						</ul>
						
						<p>2013年12月20日</p>
						<ul>
							<li>发布版本7.1.6p1</li>
							<li>组织机构/群组代码不允许为空</li>
							<li>CoreMail同步时增加过期时间</li>
						</ul>
						<p>2013年12月16日</p>
						<ul>
							<li>发布版本v.7.1.6</li>
							<li>新增邮箱账户到期时间</li>
						</ul>
						
						<p>2013年12月6日</p>
						<ul>
							<li>发布版本v.7.1.5</li>
							<li>新增部门隐藏功能</li>
						</ul>
					
						<p>2013年10月10日</p>
						<ul>
							<li>发布版本v.7.1.4p1</li>
							<li>解决升级时js/css缓存更新BUG</li>
						</ul>
						<p>2013年10月9日</p>
						<ul>
							<li>发布版本v.7.1.4</li>
							<li>更新组织通讯录组织结构时，同时更新院邮箱组织结构</li>
							<li>支持管理从组织通讯添加、删除、停用、锁定科技网邮箱，以及密码重置</li>
							<li>支持搜索加入公开群组</li>
							<li>支持从Excel批量更新用户</li>
							<li>部门代号更改为仅支持英文与数字</li>
						</ul>
						<p>2013年8月22日</p>
						<ul>
							<li>发布版本v.7.1.3</li>
							<li>API更新，增加用户办公电话等详细信息</li>
							<li>修复MQ消息BUG</li>
						</ul>
					
						<p>2013年8月16日</p>
						<ul>
							<li>发布版本v.7.1.2</li>
							<li>显示组织机构/团队管理员及人数</li>
							<li>更新用户邀请时MQ消息发送逻辑</li>
							<li>支持手机浏览视图</li>
						</ul>
						<p>2013年8月12日</p>
						<ul>
							<li>发布版本v.7.1.1</li>
							<li>增加部门人数的统计</li>
							<li>组织机构下树状与列表视图切换</li>
							<li>系统管理员手动触发MQ消息</li>
						</ul>
						<p>2013年8月9日</p>
						<ul>
							<li>发布版本v.7.1.0</li>
							<li>增加用户属性:办公室，办公电话，手机等</li>
							<li>添加用户是否可见、权重等属性</li>
							<li>上传LOGO</li>
							<li>增加基于MQ的消息分发</li>
						</ul>
						<p>2013年8月7日</p>
						<ul>
							<li>发布版本v.7.0.4</li>
							<li>增加用户只能属于一个组织机构的逻辑</li>
							<li>CoreMail数据导入支持排序字段</li>
						</ul>
						<p>2013年7月17日</p>
						<ul>
							<li>发布版本v.7.0.3</li>
							<li>优化导航栏加载速度</li>
						</ul>
						<p>2013年6月7日</p>
						<ul>
							<li>发布版本v.7.0.2</li>
							<li>新增层次式组织结构视图下的成员搜索</li>
							<li>单点登录优化</li>
						</ul>
						<p>2013年5月31日</p>
						<ul>
							<li>发布版本v.7.0.1</li>
							<li>增加团队移动功能</li>
							<li>邀请确认邮件内容修改</li>
							<li>在层级视图时支持多选用户</li>
							<li>取消选中用户</li>
							<li>CoreMail和DDL的数据导入</li>
							<li>界面优化与BUG修改</li>
						</ul>
						<p>2013年5月24日</p>
						<ul>
							<li>发布版本v.7.0.0p2</li>
							<li>增加URL拦截功能，修复部分隐藏链接权限控制的BUG</li>
							<li>修复了“拒绝后应看不到拒绝的团队”的BUG</li>
							<li>修复hcolumns的BUG，一个页面可以允许有多个层级视图</li>
							<li>新增首页（未登录时），调整部分界面风格</li>
						</ul>
						<p>2013年5月22日</p>
						<ul>
							<li>发布版本v.7.0.0p1</li>
							<li>搜索优化，按登录用户的域名搜索</li>	
							<li>部门为空的时候，删除，提示不能删除自己的BUG，已修复</li>
							<li>自动添加人员的时候，去掉本地缓存</li>
							<li>添加人员成功后，取消按钮无用，删掉</li>
						</ul>
						
					</div>
				</div>	
			</div>
		</div>
	</body>
	
	<script>
		$(document).ready(function(){
			$(".left-nav ul.nav > li > a").click(function(){
				$(".left-nav ul.nav > li.active").removeClass("active");
				$(this).parent().addClass("active");
				var classname = $(this).attr("class");
				$(".right-content div").hide();
				$(".right-content div." + classname).show();
				$(".right-content div." + classname + " *").show();
				$(window).scrollTop(0);
			});
			$('#helpB').addClass('active');
		});
	</script>
</html>

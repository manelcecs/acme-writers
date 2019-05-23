<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="${systemName} Co., Inc."
		height="200px" width="500px" /></a>
</div>
<div>
	<ul id="jMenu">

		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="adminConfig/administrator/display.do"><spring:message
								code="master.page.administrator.configuration" /></a></li>
					<li><a href="genre/administrator/list.do"><spring:message
								code="master.page.administrator.genres" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('READER')">
			<li><a class="fNiv" href="finder/reader/edit.do"><spring:message
						code="master.page.finder.edit" /></a></li>
			<li><a class="fNiv"
				href="announcement/reader/listAllMyWriters.do"><spring:message
						code="master.page.announcement.listMy" /></a></li>
		</security:authorize>


		<security:authorize access="hasRole('PUBLISHER')">
			<li><a class="fNiv" href="contest/publisher/list.do"><spring:message
						code="master.page.myContests" /></a></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="writer/list.do"><spring:message
						code="master.page.writer.display" /></a></li>
			<li><a class="fNiv" href="search/display.do"><spring:message
						code="master.page.search.display" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a class="fNIv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="writer/register.do"><spring:message
								code="master.page.register.wiriter" /></a></li>
					<li><a href="reader/register.do"><spring:message
								code="master.page.register.reader" /></a></li>
					<li><a href="publisher/register.do"><spring:message
								code="master.page.register.publisher" /></a></li>
					<li><a href="sponsor/register.do"><spring:message
								code="master.page.register.sponsor" /></a></li>
				</ul></li>
			<li><a class="fNiv" href="contest/list.do"><spring:message
						code="master.page.contest.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('WRITER')">
			<li><a class="fNiv"
				href="announcement/writer/list.do"><spring:message
						code="master.page.announcement.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()and not(hasRole('BAN'))">
			<li><a class="fNiv" href="writer/list.do"><spring:message
						code="master.page.writer.display" /></a></li>
			<li>
			<li><a class="fNiv" href="search/display.do"><spring:message
						code="master.page.search.display" /></a></li>

			<li><a class="fNiv" href="contest/list.do"><spring:message
						code="master.page.contest.list" /></a></li>

			<li><a class="fNiv" href="messageBox/list.do"><spring:message
						code="master.page.boxes" /></a></li>
		</security:authorize>


		<security:authorize access="hasRole('BAN')">
			<li><a class="fNiv" href="search/display.do"><spring:message
						code="master.page.search.display" /></a></li>
			<li><a class="fNiv" href="contest/list.do"><spring:message
						code="master.page.contest.list" /></a></li>
		</security:authorize>


		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="j_spring_security_logout"><spring:message
						code="master.page.logout" /> </a></li>
		</security:authorize>



	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>


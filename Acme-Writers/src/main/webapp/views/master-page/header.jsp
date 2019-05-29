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
	<a href="#"><img src="${banner}" alt="${systemName} Co., Inc." height="200px" width="500px"/></a>
</div>
<div>
	<ul id="jMenu">
		
		
		<!-- Books -->
		
		<li>
			<a class="fNiv"><spring:message	code="master.page.list.AllBooks" /></a>
			<ul>
				<li class="arrow"></li>
				<li>
					<a href="search/display.do"><spring:message code="master.page.search.display" /></a>
				</li>
				<li>
					<a href="book/listAll.do"><spring:message code="master.page.book.system" /></a>
				</li>
				<li>
					<a href="book/listRecommended.do"><spring:message code="master.page.book.recommended" /></a>
				</li>
				<security:authorize access="hasRole('WRITER')">
					<li>
						<a href="book/writer/list.do"><spring:message code="master.page.list.books" /></a>
					</li>
				</security:authorize>
				<security:authorize access="hasRole('PUBLISHER')">
					<li>
						<a href="book/publisher/list.do"><spring:message code="master.page.list.books" /></a>
					</li>
				</security:authorize>
				<security:authorize access="hasRole('READER')">
					<li>
						<a href="book/reader/listFavourites.do"><spring:message code="master.page.list.booksFavourites" /></a>
					</li>
				</security:authorize>
			</ul>
		</li>
		
		<!-- List of contests -->
		<security:authorize access="not hasRole('PUBLISHER')">
		<li>
			<a class="fNiv" href="contest/list.do"><spring:message code="master.page.contest.list" /></a>
		</li> 
		</security:authorize>
		

		
		<!-- Actors -->
		<li>
			<a class="fNiv"><spring:message	code="master.page.actors" /></a>
			<ul>
				<li class="arrow"></li>
				<li>
					<a href="actor/listWriters.do"><spring:message code="master.page.list.writers" /></a>
		
				</li>
				<li>
					<a href="actor/listPublishers.do"><spring:message code="master.page.list.publishers" /></a>
				</li>
			</ul>
		</li>
		
		<!-- ADMINISTRATOR -->
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li>
				<a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li>
						<a href="adminConfig/administrator/display.do"><spring:message code="master.page.administrator.configuration" /></a>
					</li>
					<li>
						<a href="administrator/administrator/register.do"><spring:message code="master.page.administrator.register" /></a>
					</li>
					<li>
						<a href="genre/administrator/list.do"><spring:message code="master.page.administrator.genres" /></a>
					</li>
					<li>
					    <a href="dashboard/administrator/display.do"><spring:message code="master.page.header.dashboard" /></a>
					</li>
					<li>
					   <a href="administrator/process.do"><spring:message code="master.page.process.launch" /></a>
					</li>
					<li>
						<a href="actor/administrator/listActors.do"><spring:message code="master.page.administrator.listActors" /></a>
					</li>
				</ul>
			</li>
		</security:authorize>
		
		<!-- Finder -->
		<security:authorize access="hasRole('READER')">
			<li>
				<a class="fNiv" href="finder/reader/edit.do"><spring:message code="master.page.finder.edit" /></a>
			</li>
			<li>
				<a class="fNiv" href="announcement/reader/listAllMyWriters.do"><spring:message code="master.page.announcement.listMy" /></a>
			</li>
		</security:authorize>
		
		<!-- List of announcements and participations -->
		<security:authorize access="hasRole('WRITER')">			
			<li>
				<a class="fNiv" href="announcement/writer/list.do"><spring:message code="master.page.announcement.list" /></a>
			</li>
			<li>
				<a class="fNiv" href="participation/writer/list.do"><spring:message code="master.page.myParticipations" /></a>
			</li>
		</security:authorize>
	
		
		
		<security:authorize access="hasRole('PUBLISHER')">
		<li>
			<a class="fNiv"><spring:message	code="master.page.contests" /></a>
			<ul>
			
				<li>
					<a href="contest/publisher/list.do"><spring:message code="master.page.myContests" /></a>
				</li>
				<li>
					<a href="contest/list.do"><spring:message code="master.page.contest.list" /></a>
				</li> 
			</ul>
		</li>
			<li>
				<a class="fNiv" href="participation/publisher/list.do"><spring:message code="master.page.myParticipations" /></a>
			</li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('SPONSOR')">
			<li>
				<a class="fNiv" href="sponsorship/sponsor/list.do"><spring:message code="master.page.mySponsorships" /></a>
			</li>
		</security:authorize>
		
		
		<security:authorize access="isAuthenticated() and not(hasRole('BAN'))">
			<li>
				<a class="fNiv" href="messageBox/list.do"><spring:message code="master.page.boxes" /></a>
			</li>
			
		</security:authorize>
		
		<!-- My profile and logout -->
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="actor/display.do"><spring:message code="master.page.profile" />(<security:authentication property="principal.username" />)</a></li>
			<li><a class="fNiv"><spring:message code="actor.settings" /></a>
				<ul>
					<li>
						<a href="actor/displayData.do"><spring:message code="master.page.displayData" /></a>
					</li>
				</ul>
			</li>
			<li>
				<a class="fNiv" href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a>
			</li>
		</security:authorize>
		
		
		<!-- Login -->
		<security:authorize access="isAnonymous()">
		<li>
			<a class="fNiv"><spring:message code="master.page.register" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="writer/register.do"><spring:message code="master.page.register.wiriter" /></a></li>
				<li><a href="reader/register.do"><spring:message code="master.page.register.reader" /></a></li>
				<li><a href="publisher/register.do"><spring:message code="master.page.register.publisher" /></a></li>
				<li><a href="sponsor/register.do"><spring:message code="master.page.register.sponsor" /></a></li>
			</ul>
		</li>
		<li>
			<a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a>
		</li>
		</security:authorize>
		
	
		
		
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>


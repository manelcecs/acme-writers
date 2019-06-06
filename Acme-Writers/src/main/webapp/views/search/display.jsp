<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<section>

		<form:form id="search" action="search/display.do" modelAttribute="searchForm" method="post">	
			<acme:textbox code="search.keyword" path="keyword" />
			<acme:submit name="save" code="search.save" />
		</form:form>

	</section>
	
	<section>
	
		<display:table pagesize="10" name="books" id="book" requestURI="${requestURI}">
			<display:column titleKey="search.book.ticker"> <jstl:out value="${book.ticker.identifier}"/>
			</display:column>
			<display:column titleKey="search.book.title"><jstl:out value="${book.title}"/>
			</display:column>
			<display:column titleKey="search.book.writer"><jstl:out value="${book.writer.name} ${book.writer.surname}"/></display:column>			
			<display:column titleKey="search.book.description"><jstl:out value="${book.description}"/></display:column>
			<display:column titleKey="search.book.lang"><jstl:out value="${book.lang}"/></display:column>
			
			<jstl:choose>
				<jstl:when test="${cookie.language.value == 'es'}">
					<display:column titleKey="search.book.genre"><jstl:out value="${book.genre.nameES}"/></display:column>
				</jstl:when>
				
				<jstl:otherwise>
					<display:column titleKey="search.book.genre"><jstl:out value="${book.genre.nameEN}"/></display:column>
				</jstl:otherwise>
			</jstl:choose>
		</display:table>
	
	</section>


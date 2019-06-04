<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('READER')">

	<section>

		<form:form id="editFinder" action="finder/reader/edit.do"
			modelAttribute="finder" method="post">

			<acme:textbox code="finder.edit.keyword" path="keyWord" />
			<form:label path="lang">
				<spring:message code="finder.edit.lang" />
			</form:label>	
			<form:select path="lang">
				<form:option value="" ><jstl:out value="----"/></form:option>
				<jstl:forEach items="${languages}" var="language">
					<form:option value="${language}" ><jstl:out value="${language}"/></form:option>
				</jstl:forEach>
			</form:select>
			<form:errors path="lang" cssClass="error" />			
			<acme:inputNumber code="finder.edit.minNumWords" path="minNumWords" />
			<acme:inputNumber code="finder.edit.maxNumWords" path="maxNumWords" />
			
			<jstl:choose>
				<jstl:when test="${cookie.language.value == 'es'}">
					<acme:select items="${genres}" itemLabel="nameES" code="finder.edit.genre" path="genre"/>
				</jstl:when>
				
				<jstl:otherwise>
					<acme:select items="${genres}" itemLabel="nameEN" code="finder.edit.genre" path="genre"/>
				</jstl:otherwise>
			</jstl:choose>
			
			<acme:cancel url="/" code="finder.edit.cancel" />
			<acme:submit name="clear" code="finder.edit.clear" />
			<acme:submit name="save" code="finder.edit.save" />
			

		</form:form>

	</section>
	
	<section>
	
		<display:table pagesize="10" name="books" id="book" requestURI="${requestURI}">
			<display:column titleKey="finder.edit.book.ticker"> <jstl:out value="${book.ticker.identifier}"/>
			</display:column>
			<display:column titleKey="finder.edit.book.title"><jstl:out value="${book.title}"/>
			</display:column>
			<display:column titleKey="finder.edit.book.writer"><jstl:out value="${book.writer.name} ${book.writer.surname}"/></display:column>			
			<display:column titleKey="finder.edit.book.publisher"><jstl:out value="${book.publisher.name} ${book.publisher.surname}"/></display:column>			
			<display:column titleKey="finder.edit.book.description"><jstl:out value="${book.description}"/></display:column>
			<display:column titleKey="finder.edit.book.lang"><jstl:out value="${book.lang}"/></display:column>
			
			<jstl:choose>
				<jstl:when test="${cookie.language.value == 'es'}">
					<display:column titleKey="finder.edit.book.genre"><jstl:out value="${book.genre.nameES}"/></display:column>
				</jstl:when>
				
				<jstl:otherwise>
					<display:column titleKey="finder.edit.book.genre"><jstl:out value="${book.genre.nameEN}"/></display:column>
				</jstl:otherwise>
			</jstl:choose>
			<display:column titleKey="finder.edit.book.numWords"><jstl:out value="${book.numWords}"/></display:column>			
		</display:table>
	
	</section>




</security:authorize>



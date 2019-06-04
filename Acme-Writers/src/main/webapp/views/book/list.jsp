
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<h2><spring:message code="${title}"/></h2>


<jstl:if test="${myList}">
 <acme:button url="book/writer/create.do" type="button" code="book.list.create"/>
</jstl:if>

<display:table pagesize="5" name="books" id="book" requestURI="${requestURI}">
   		 <display:column titleKey="book.list.title"><jstl:out value="${book.title}"/></display:column>
   		 <display:column titleKey="book.list.lang"><jstl:out value="${book.lang}"/></display:column>
   		 <display:column titleKey="book.list.genre">
   		 	<jstl:choose>
				<jstl:when test="${cookie.language.value == 'es'}">
					<jstl:out value="${book.genre.nameES}"/>
				</jstl:when>
				
				<jstl:otherwise>
					<jstl:out value="${book.genre.nameEN}"/>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
   		<display:column titleKey="book.list.numWords"><jstl:out value="${book.numWords}"/></display:column>
   		<display:column titleKey="book.list.publisher"><jstl:out value="${book.publisher.commercialName}"/></display:column>
		<jstl:choose>
		
		
		
		<jstl:when test="${myList}">
			 <display:column titleKey="book.list.display">
   				 <acme:button url="book/writer/display.do?idBook=${book.id}" type="button" code="book.list.display"/>
   			 </display:column>
   			 
   			 <display:column titleKey="book.list.edit">
   			 	<jstl:if test="${book.draft}">
   				 <acme:button url="book/writer/edit.do?idBook=${book.id}" type="button" code="book.list.edit"/>
   			 	</jstl:if>
   			 </display:column>
   			 
   			 <display:column titleKey="book.list.delete">
   			 <jstl:if test="${book.draft}">
   				 <acme:button url="book/writer/delete.do?idBook=${book.id}" type="button" code="book.list.delete"/>
   			 </jstl:if>
   			 </display:column>
   			 
   			 <display:column titleKey="book.list.cancel">
   			 <jstl:choose>
   			 <jstl:when test="${!book.draft and (book.status == 'ACCEPTED' || book.status == 'INDEPENDENT')}">
   			 	<jstl:if test="${book.cancelled}">
   				 <acme:button url="book/writer/cancel.do?idBook=${book.id}" type="button" code="book.list.revokeCancel"/>
   			 	</jstl:if>
   			 	<jstl:if test="${!book.cancelled}">
   				 <acme:button url="book/writer/cancel.do?idBook=${book.id}" type="button" code="book.list.cancel"/>
   			 	</jstl:if>
   			 </jstl:when>
   			 <jstl:when test="${!book.draft and book.status == 'REJECTED'}">
   			 	<spring:message code="book.list.rejectedBook"/>
   			 </jstl:when>
   			 </jstl:choose>
   			 </display:column>
   			 
   			 <display:column titleKey="book.list.changeDraft">
   			 <jstl:if test="${book.draft and booksCanChangeDraft.contains(book)}">
   				 <acme:button url="book/writer/changeDraft.do?idBook=${book.id}" type="button" code="book.list.changeDraft"/>
   			 </jstl:if>
   			 </display:column>
   		 	<display:column titleKey="book.list.status"><jstl:out value="${book.status}"/></display:column>
   		 	
   		 	<display:column titleKey="book.list.copy">
   			 <jstl:if test="${book.status == 'REJECTED'}">
   				 <acme:button url="book/writer/copy.do?idBook=${book.id}" type="button" code="book.list.copy"/>
   			 </jstl:if>
   			 </display:column>
   		 	<display:column titleKey="book.list.score">
   		 	<jstl:choose> 
				<jstl:when test="${book.score == null}">
					<jstl:out value="N/A"/>
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${book.score}"/>
				</jstl:otherwise>
			</jstl:choose>
   		 	</display:column>
		</jstl:when>
		
		<jstl:when test="${publisher}">
		<display:column titleKey="book.list.writer"><jstl:out value="${book.writer.name}"/></display:column>
   		<display:column titleKey="book.list.seeWriter"><acme:button url="writer/display.do?writerId=${book.writer.id}" type="button" code="book.list.display"/></display:column>
			<display:column titleKey="book.list.display">
   				 <acme:button url="book/publisher/display.do?idBook=${book.id}" type="button" code="book.list.display"/>
   			 </display:column>
   			 <display:column titleKey="book.list.status"><jstl:out value="${book.status}"/></display:column>
		</jstl:when>
		
		<jstl:when test="${favourites}">
		<display:column titleKey="book.list.writer"><jstl:out value="${book.writer.name}"/></display:column>
   		<display:column titleKey="book.list.seeWriter"><acme:button url="writer/display.do?writerId=${book.writer.id}" type="button" code="book.list.display"/></display:column>
			<display:column titleKey="book.list.favourites">
			<jstl:if test="${!favouritesBooks.contains(book)}">
   				 <acme:button url="book/reader/addToList.do?idBook=${book.id}" type="button" code="book.list.addToList"/>
			</jstl:if>
			<jstl:if test="${favouritesBooks.contains(book)}">
   				 <acme:button url="book/reader/removeFromFavourites.do?idBook=${book.id}" type="button" code="book.list.removeFromList"/>
			</jstl:if>
   			 </display:column>
   			 <display:column titleKey="book.list.display">
   				 <acme:button url="book/display.do?idBook=${book.id}" type="button" code="book.list.display"/>
   			 </display:column>
   			 <display:column titleKey="book.list.opinion">
   				 <acme:button url="opinion/reader/create.do?idBook=${book.id}&urlBack=${requestURI}" type="button" code="book.list.opinion"/>
   			 </display:column>
		</jstl:when>

		<jstl:otherwise>
		<display:column titleKey="book.list.writer"><jstl:out value="${book.writer.name}"/></display:column>
   		<display:column titleKey="book.list.seeWriter"><acme:button url="writer/display.do?writerId=${book.writer.id}" type="button" code="book.list.display"/></display:column>
			<display:column titleKey="book.list.display">
   				 <acme:button url="book/display.do?idBook=${book.id}" type="button" code="book.list.display"/>
   			 </display:column>
   			 <display:column titleKey="book.list.score">
   			 <jstl:choose> 
				<jstl:when test="${book.score == null}">
					<jstl:out value="N/A"/>
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${book.score}"/>
				</jstl:otherwise>
			</jstl:choose>
   			 </display:column>

		</jstl:otherwise>
		</jstl:choose>
</display:table>



<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:choose>

<jstl:when test="${logged}">
	<acme:button url="book/writer/list.do" type="button" code="book.display.back"/>
</jstl:when>
<jstl:otherwise>
	<button onclick="window.location.href=document.referrer"><spring:message code="book.display.back"/></button>
</jstl:otherwise>
</jstl:choose>
<br/>

<jstl:if test="${publisher and book.status == 'PENDING'}">
 <acme:button url="book/publisher/changeStatus.do?idBook=${book.id}&status=ACCEPTED" type="button" code="book.list.accept"/>
 <acme:button url="book/publisher/changeStatus.do?idBook=${book.id}&status=REJECTED" type="button" code="book.list.reject"/>
</jstl:if><br/>

<img src="<jstl:out value="${book.cover}"/>" alt="<jstl:out value="${book.cover}"/>" height="250" width="250">
<acme:text label="book.display.title" value="${book.title}"/>
<acme:text label="book.display.description" value="${book.description}"/>
<acme:text label="book.display.language" value="${book.language}"/>
<acme:text label="book.display.score" value="${book.score}"/>
<acme:text label="book.display.numWords" value="${book.numWords}"/>
<jstl:choose>
	<jstl:when test="${cookie.language.value == 'es'}">
		<acme:text label="book.display.genre" value="${book.genre.nameES}"/>
	</jstl:when>
	
	<jstl:otherwise>
		<acme:text label="book.display.genre" value="${book.genre.nameEN}"/>
	</jstl:otherwise>
</jstl:choose>
<jstl:if test="${book.publisher ne null}">
	<acme:text label="book.display.publisher" value="${book.publisher.commercialName}"/>

</jstl:if>
<p><strong><spring:message code="book.display.writer" />:</strong>  <jstl:out value="${book.writer.name}"/> <jstl:out value="${book.writer.surname}"/></p>
<h4><spring:message code="book.display.chapters"/></h4>
<jstl:if test="${logged and book.draft}">
	<acme:button url="chapter/writer/create.do?idBook=${book.id}" type="button" code="book.display.create.chapter"/>
</jstl:if>
<display:table pagesize="5" name="chapters" id="chapter" requestURI="${requestURIChapters}">
   		 <display:column titleKey="book.display.chapter.title"><jstl:out value="${chapter.title}"/></display:column>
   		 <display:column titleKey="book.display.chapter.number"><jstl:out value="${chapter.number}"/></display:column>
   		 <jstl:choose>
			<jstl:when test="${logged}">
				<display:column titleKey="book.display.chapter.display">
	   				 <acme:button url="chapter/writer/display.do?idChapter=${chapter.id}" type="button" code="book.display.chapter.display"/>
	   			 </display:column>
	   			<jstl:if test="${book.draft}">
	   			 <display:column titleKey="book.display.chapter.edit">
	   				 <acme:button url="chapter/writer/edit.do?idChapter=${chapter.id}" type="button" code="book.display.chapter.edit"/>
	   			 </display:column>
	   			 
	   			 <display:column titleKey="book.display.chapter.delete">
	   				 <acme:button url="chapter/writer/delete.do?idChapter=${chapter.id}" type="button" code="book.display.chapter.delete"/>
	   			 </display:column>
	   			</jstl:if>
			</jstl:when>
			
			<jstl:when test="${publisher}">
			<display:column titleKey="book.display.chapter.display">
   				 <acme:button url="chapter/publisher/display.do?idChapter=${chapter.id}" type="button" code="book.display.chapter.display"/>
   			 </display:column>
			 </jstl:when>
			 
			<jstl:otherwise>
			<display:column titleKey="book.display.chapter.display">
   				 <acme:button url="chapter/display.do?idChapter=${chapter.id}" type="button" code="book.display.chapter.display"/>
   			 </display:column>
			 </jstl:otherwise>
   		 </jstl:choose>
</display:table>

<!-- SI ES UN LECTOR Y NO TIENE UNA OPINION A ESTE LIBRO, SE PUEDE PONER UN BOTON -->
<h4><spring:message code="book.display.opinions"/></h4>
<display:table pagesize="5" name="opinions" id="opinion" requestURI="${requestURIOpinion}">
   		 <display:column titleKey="book.display.opinion.reader"><jstl:out value="${opinon.reader.name}"/> <jstl:out value="${opinon.reader.surname}"/></display:column>
   		 <display:column titleKey="book.display.opinion.moment"><jstl:out value="${opinon.moment}"/></display:column>
   		 <%-- <display:column titleKey="book.display.opinion.positiveOpinion"><jstl:out value="${opinon.positiveOpinion}"/></display:column> --%>
   		 <display:column titleKey="book.display.opinion.review"><jstl:out value="${opinon.review}"/></display:column>
</display:table>


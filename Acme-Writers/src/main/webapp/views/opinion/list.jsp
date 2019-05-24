<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table pagesize="5" name="opinions" id="opinion" requestURI="${requestURI}">
			<display:column titleKey="opinion.list.book"><jstl:out value="${opinion.book.title}"/></display:column>
			<display:column titleKey="opinion.list.like">
				<jstl:choose>
					<jstl:when test="${opinion.positiveOpinion}">
						<img height="50px" alt="http://pngimg.com/uploads/like/like_PNG17.png" src="http://pngimg.com/uploads/like/like_PNG17.png">
					</jstl:when>
					<jstl:otherwise>
						<img height="50px" alt="https://www.freeiconspng.com/uploads/facebook-dislike-transparent-25.png" src="https://www.freeiconspng.com/uploads/facebook-dislike-transparent-25.png">
					</jstl:otherwise>
				</jstl:choose>
			</display:column>
			<display:column titleKey="opinion.list.moment"><jstl:out value="${opinion.moment}"/></display:column>
		
				<display:column titleKey="opinion.list.seeMore">
					<acme:button url="opinion/reader/display.do?idOpinion=${opinion.id}" type="button" code="opinion.list.seeMore"/>
				</display:column>

				<display:column titleKey="opinion.list.edit">
						<acme:button url="opinion/reader/edit.do?idOpinion=${opinion.id}" type="button" code="opinion.list.edit"/>
				</display:column>

				<display:column titleKey="opinion.list.delete">
						<acme:button url="opinion/reader/delete.do?idOpinion=${opinion.id}" type="button" code="opinion.list.delete"/>
				</display:column>
				
</display:table>
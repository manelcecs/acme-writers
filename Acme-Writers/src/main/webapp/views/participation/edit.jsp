<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="participation" action="participation/publisher/edit.do">
	<acme:hidden path="id"/>
	
	<acme:text label="participation.edit.comment" value="${participationBD.comment}"/>
	<acme:text label="participation.edit.moment" value="${participationBD.moment}"/>
	<acme:text label="participation.edit.contest" value="${participationBD.contest.title}"/>
	<acme:text label="participation.edit.book" value="${participationBD.book.title}"/>
			<jstl:if test="${actual.before(participationBD.contest.deadline) && participationBD.status == 'PENDING'}">
 				<form:label path="status"><spring:message code="participation.edit.status"/></form:label>
     			<form:select path="status" multiple="false" >
	     			<form:option value="ACCEPTED" ><jstl:out value="ACCEPTED"/></form:option>
	     			<form:option value="REJECTED" ><jstl:out value="REJECTED"/></form:option>
   				</form:select>
		    <br/>
		    </jstl:if> 
			<jstl:if test="${!actual.before(participationBD.contest.deadline) && participationBD.status == 'ACCEPTED'}">
 				<form:label path="position"><spring:message code="participation.edit.position"/></form:label>			
				<form:select path="position" multiple="false" >
					<form:option value="" ><spring:message code="participation.edit.nullPosition"/></form:option>
					<jstl:forEach items="${positions}" var="position">
	     				<form:option value="${position}" ><jstl:out value="${position}"/></form:option>
   					</jstl:forEach>
   				</form:select>
   				<form:errors path="position" cssClass="error" />			
   						    
   			<br/>
			</jstl:if>
		<acme:submit name="save" code="participation.edit.save" />
		<acme:cancel url="/participation/publisher/list.do" code="participation.edit.cancel"/>
</form:form>
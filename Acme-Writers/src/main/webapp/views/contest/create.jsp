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

<form:form modelAttribute="contest" action="contest/publisher/create.do">
		
		<p>
			<acme:textarea code="contest.create.description" path="description"/>
		</p>
		<p>
			<acme:textarea code="contest.create.prize" path="prize"/>
		</p>
		<p>
			<acme:inputDate code="contest.create.deadline" path="deadline"/>
		</p>
		
		<form:label class="textboxLabel" path="rules"><spring:message code="contest.create.rules" /></form:label>
   		<div id="rules">
    	<jstl:if test="${empty contest.rules}">
        	<form:input class="textbox" path="rules" type="text"/>        	
    	</jstl:if>
   
    	<jstl:forEach items="${contest.rules}" var="rule">
        	<form:input class="textbox" path="rules" type="text" value="${rule}"/> 
    	</jstl:forEach> 
    	</div>
   		<form:errors path="rules" cssClass="error" />
    	    			
		<acme:submit name="save" code="contest.create.save"/>
		<acme:cancel url="contest/publisher/list.do" code="contest.create.back"/>
	</form:form>
	
	<button class="addTag" onclick="addComment('rules','rules', 'textbox')"><spring:message code="contest.create.addRules" /></button>
	

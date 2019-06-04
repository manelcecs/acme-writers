
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form modelAttribute="bookForm" action="book/writer/save.do">
   	<acme:hidden path="id"/>
   	<acme:hidden path="version"/>
   	 
  
   	<p>
   		 <acme:textarea code="book.edit.title" path="title"/>
   	</p>
   	<p>
   		 <acme:textarea code="book.edit.description" path="description"/>
   	</p>
   	<p>
		<spring:message code="book.edit.lang"/>: <select name="lang">
	  <option value="OTHER" <jstl:if test="${bookForm.lang == 'OTHER'}"><jstl:out value="selected"/></jstl:if>>OTHER</option>
	  <option value="EN" <jstl:if test="${bookForm.lang == 'EN'}"><jstl:out value="selected"/></jstl:if>>EN</option>
	  <option value="ES" <jstl:if test="${bookForm.lang == 'ES'}"><jstl:out value="selected"/></jstl:if>>ES</option>
	  <option value="IT" <jstl:if test="${bookForm.lang == 'IT'}"><jstl:out value="selected"/></jstl:if>>IT</option>
	  <option value="FR" <jstl:if test="${bookForm.lang == 'FR'}"><jstl:out value="selected"/></jstl:if>>FR</option>
	  <option value="DE" <jstl:if test="${bookForm.lang == 'DE'}"><jstl:out value="selected"/></jstl:if>>DE</option>
	</select>
   	</p>
   	<p>
   		 <acme:textbox code="book.edit.cover" path="cover"/>
   	</p>

   	<acme:select items="${publishers}" itemLabel="commercialName" code="book.edit.publisher" path="publisher"/>
   	 
	
   	 
   	<jstl:choose>
	<jstl:when test="${cookie.language.value == 'es'}">
		<acme:select items="${genres}" itemLabel="nameES" code="book.edit.genre" path="genre" optional="false"/>
	</jstl:when>
	
	<jstl:otherwise>
		<acme:select items="${genres}" itemLabel="nameEN" code="book.edit.genre" path="genre" optional="false"/>
	</jstl:otherwise>
	</jstl:choose>
   	  	 
   	 
   	<acme:submit name="save" code="book.edit.save"/>

</form:form>

<acme:button code="book.edit.cancel" type="button" url="/book/writer/list.do"/>
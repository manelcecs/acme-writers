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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="chapter" action="chapter/writer/save.do">
   	 <acme:hidden path="id"/>
   	 <acme:hidden path="version"/>
   	 <acme:hidden path="book"/>
   	 
  
   	 <p>
   		 <acme:textbox code="chapter.edit.title" path="title"/>
   	 </p>
   	 <p>
   		 <acme:inputNumber code="chapter.edit.number" path="number"/>
   	 </p>
   	 <p>
   		 <acme:textarea code="chapter.edit.text" path="text"/>
   	 </p>
   	 <acme:submit name="save" code="chapter.edit.save"/>
    </form:form>

<acme:button code="chapter.edit.cancel" type="button" url="/book/writer/list.do"/>


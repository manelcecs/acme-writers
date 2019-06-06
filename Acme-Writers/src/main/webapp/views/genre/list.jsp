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

<acme:button url="genre/administrator/create.do" type="button" code="genre.list.create"/>

<section>

		<display:table name="genres" id="genre">
			<display:column titleKey="genre.list.parent" ><jstl:out value="${genre.parent.nameEN}"/> / <jstl:out value="${genre.parent.nameES}"/></display:column>
			<display:column titleKey="genre.list.name" ><jstl:out value="${genre.nameEN}"/> / <jstl:out value="${genre.nameES}"/></display:column>
			<display:column titleKey="genre.list.edit" ><acme:button url="genre/administrator/edit.do?idGenre=${genre.id}" type="button" code="genre.list.edit"/></display:column>
			<display:column titleKey="genre.list.delete" ><acme:button url="genre/administrator/delete.do?idGenre=${genre.id}" type="button" code="genre.list.delete"/></display:column>
		</display:table>

</section>





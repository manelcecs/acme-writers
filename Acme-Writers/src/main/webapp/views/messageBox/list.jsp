<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:button url="message/create.do" type="button" code="messageBox.list.sendMessage"/>
<acme:button url="messageBox/create.do" type="button" code="messageBox.list.createBox"/>

<section id="main">

	<jstl:if test="${empty boxes}">
		<p><spring:message code="messageBox.list.emptyBox"/></p>
	</jstl:if>

	<section id="displayBoxes">

	<jstl:forEach var="box" items="${boxes}">

		<jstl:choose>
			<jstl:when test="${boxSelect.id == box.id}">
				<div class="boxSelect">
					<jstl:choose>
						<jstl:when test="${box.parent == null}">
							<p><jstl:out value="${box.name}"/></p>								
						</jstl:when>
						<jstl:otherwise>
							<p><jstl:out value="${box.parent.name}"/> > <jstl:out value="${box.name}"/></p>
						</jstl:otherwise>
					</jstl:choose>
				
				<jstl:if test="${box.deleteable == true}">
		
					<a href="messageBox/edit.do?idMessageBox=${box.id}"><spring:message code="messageBox.list.edit"/></a>
					<a href="messageBox/delete.do?idMessageBox=${box.id}"><spring:message code="messageBox.list.delete"/></a>
		
				</jstl:if>
				
				</div>
			</jstl:when>

			<jstl:otherwise>
				<div class="boxNoSelect" >
					<jstl:choose>
						<jstl:when test="${box.parent == null}">
							<a href="messageBox/display.do?idBox=${box.id}"><jstl:out value="${box.name}"/></a>										
						</jstl:when>
						<jstl:otherwise>
							<a href="messageBox/display.do?idBox=${box.id}"><jstl:out value="${box.parent.name}"/> > <jstl:out value="${box.name}"/></a>
						</jstl:otherwise>
					</jstl:choose>
				</div>
			</jstl:otherwise>
		</jstl:choose>
		
		

	</jstl:forEach>
	
	</section>

	<section id="displayMessages">

		<display:table pagesize="5" name="messages" id="message" requestURI="${requestURI}">
			<display:column titleKey="messageBox.list.moment"><jstl:out value="${message.moment}"/></display:column>
			<display:column titleKey="messageBox.list.email" ><jstl:out value="${message.sender.email}"/></display:column>
			<display:column titleKey="messageBox.list.subject"><jstl:out value="${message.subject}"/></display:column>
			<display:column titleKey="messageBox.list.priority"><jstl:out value="${message.priority}"/></display:column>
			<display:column><acme:button url="message/display.do?idMessage=${message.id}" type="button" code="messageBox.list.moreDetails"/></display:column>
			<display:column><acme:button url="message/delete.do?idMessage=${message.id}" type="button" code="messageBox.list.deleteMessage"/></display:column>
			<display:column><acme:button url="message/removeFrom.do?idMessageBox=${boxSelect.id}&idMessage=${message.id}" type="button" code="messageBox.list.remove"/></display:column>
		</display:table>

	</section>

<hr>
</section>


<style>
#main {
	float: left;
	width: 100%;
}

#displayBoxes {
	float: left;
	margin: 20px;
	width: 15%;
	border: 1px solid black;
}

.boxSelect{
	background-color: black;
	width: 100%;
	float: left;
	border: 1px solid black;
}

.boxSelect > p{
	float:left;
	margin-left: 35px;
	color: white;
}

.boxSelect > a{
	float: right;
	color: white;
	display:block;
	width: 25%;
	margin-top: 15px;
	text-decoration: none;
	display: block;
}

.boxNoSelect{
	float: left;
	width: 100%;
	border: 1px solid black;
}

.boxNoSelect > a{
	display: block;
	width: 70%;
	padding: 10px 15%;
	text-decoration: none;
	color: black;
	background-color: white;
}

.boxNoSelect > a:hover{
	display: block;
	width: 70%;
	padding: 10px 15%;
	text-decoration: none;
	background-color: #006666;
	color: white;
}

#displayMessages {
	float: right;
	padding: 20px 50px;
	margin: 20px;
	width: 70%;
	border: 2px solid black;
}

.selectLabel{
	width: 100%;
	float: left;
	margin-bottom: 5px;
}

.select{
	width: 100%;
	float: left;
	margin-bottom: 20px;
}

.botones{
  	margin-left: 70px;
  	float: right;
}

.botones > button{
	margin-left: 10px;
}

#main>hr{
width: 100%;
float: left;
margin-top: 50px;
}
</style>

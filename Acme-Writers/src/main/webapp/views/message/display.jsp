<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<section id="main">

	<section id="displayMessage">

			<p>
				<strong><spring:message code="message.display.sender"/></strong>
				<jstl:out value="${Message.sender.name}"/>
				<jstl:forEach items="${Message.sender.surnames}" var="surname"><jstl:out value="${surname}"/></jstl:forEach> 
				(<jstl:out value="${Message.sender.email}"></jstl:out>)
			</p>
			
			<p class="right">
				<strong><spring:message code="message.display.timeSend"/></strong>
				<jstl:out value="${Message.moment}"></jstl:out>
			</p>
			
		<hr>
		
			<p>
				<strong><spring:message code="message.display.subject"/></strong>
				<jstl:out value="${Message.subject}"></jstl:out>
			</p>
			
			<p class="right">
				<strong><spring:message code="message.display.priority"/></strong>
				<jstl:out value="${Message.priority}"></jstl:out>
			</p>
		
		<hr>
		
			<p>
				<strong><spring:message code="message.display.body"/></strong>
				<jstl:out value="${Message.body}"></jstl:out>
			</p>
		
		<hr>
		
			<p><strong><spring:message code="message.display.tags"/></strong></p>
			<jstl:forEach items="${tags}" var="tag">
				<p>
					<jstl:out value="${tag}"></jstl:out>
				</p>
			</jstl:forEach>
		
		<hr>
		
			<p><strong><spring:message code="message.display.recipients"/></strong></p>
			<jstl:forEach items="${recipients}" var="recipient">
				<p>
					<jstl:out value="${recipient.email}"></jstl:out>
				</p>
			</jstl:forEach>

	</section>

	<section id="addto">

		<form:form action="message/addTo.do" modelAttribute="Message">

			<acme:hidden path="id" />
			<acme:select items="${boxesToMove}" itemLabel="name" code="message.display.addTo" path="messageBoxes" />
	
			<div class="botones">		
				<acme:submit name="save" code="message.display.confirm" />
				<acme:cancel url="messageBox/list.do" code="message.display.cancel" />
			</div>

		</form:form>


	</section>

<hr>
</section>



<style>
#main {
	float: left;
	width: 100%;
	ba
}

#displayMessage>p {
	margin: 15px 20px 5px 20px;
	float: left;
}

#displayMessage>p.right {
	margin: 15px 20px 5px 20px;
	float: right;
}

#displayMessage>hr {
	float: left;
	width: 100%;
}

#displayMessage {
	float: left;
	padding: 20px 50px;
	margin: 20px;
	width: 45%;
	border: 1px solid black;
}

#addto {
	float: left;
	padding: 20px 50px;
	margin: 20px;
	width: 30%;
	border: 1px solid black;
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


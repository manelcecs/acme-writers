<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<script type='text/javascript'>
	function checkPhone(str) {
		if (str != "") {
			var patt = new RegExp("^(\[+][1-9][0-9]{0,2}[ ]{1}\[(][1-9][0-9]{0,2}\[)][ ]{1}[0-9]{4,}|\[+][1-9][0-9]{0,2}[ ]{1}[0-9]{4,}|[0-9]{4,}|[ ]{1,})$");
			if (patt.test(str) == false) { return confirm("<spring:message code="writer.edit.phone.error"/>"); }
		}
	}
</script>

<jstl:if test="${edit }">
	<form:form action="writer/writer/save.do"
		modelAttribute="writer">
		<acme:textbox code="writer.edit.name" path="name" />
		<acme:textbox code="writer.edit.surname" path="surname" /> 
		<acme:textbox code="writer.edit.photoURL" path="photoURL" />
		<acme:textbox code="writer.edit.address" path="address" />
		<acme:textbox code="writer.edit.email" path="email" />
		<acme:textbox code="writer.edit.phoneNumber" path="phoneNumber" id="phone" />
		<br />
		<acme:textbox code="writer.edit.creditcard.holder" path="creditCard.holder"/>
		<acme:textbox code="writer.edit.creditcard.make" path="creditCard.make"/>
		<acme:inputNumber code="writer.edit.creditcard.number" path="creditCard.number"/>
		<acme:inputNumber code="writer.edit.creditcard.expirationMonth" path="creditCard.expirationMonth" />
		<acme:inputNumber code="writer.edit.creditcard.expirationYear" path="creditCard.expirationYear" />
		<acme:inputNumber code="writer.edit.creditcard.CVV" path="creditCard.cvv"/>
		<br/>
		<spring:message code="writer.edit.submit" var="submit"/>
		<input type="submit" name="submit" onclick="return checkPhone(this.form.phone.value)" value="${ submit}" />
		<acme:cancel url="/actor/display.do" code="writer.edit.cancel" />
	</form:form>
</jstl:if>
<jstl:if test="${not edit }">
	<form:form action="writer/save.do"
		modelAttribute="writerForm">
		<acme:textbox code="writer.edit.username"
			path="userAccount.username" />
		<acme:password code="writer.edit.password"
			path="userAccount.password" />
		<acme:password code="writer.edit.confirmPassword"
			path="confirmPassword" />

		<acme:textbox code="writer.edit.name" path="name" />
		<acme:textbox code="writer.edit.surname" path="surname" /> 
		<acme:textbox code="writer.edit.photoURL" path="photoURL" />
		<acme:textbox code="writer.edit.address" path="address" />
		<acme:textbox code="writer.edit.email" path="email" />
		<acme:textbox code="writer.edit.phoneNumber" path="phoneNumber" id="phone" />
		<br />
		<acme:textbox code="writer.edit.creditcard.holder" path="creditCard.holder"/>
		<acme:textbox code="writer.edit.creditcard.make" path="creditCard.make"/>
		<acme:inputNumber code="writer.edit.creditcard.number" path="creditCard.number"/>
		<acme:inputNumber code="writer.edit.creditcard.expirationMonth" path="creditCard.expirationMonth" />
		<acme:inputNumber code="writer.edit.creditcard.expirationYear" path="creditCard.expirationYear" />
		<acme:inputNumber code="writer.edit.creditcard.CVV" path="creditCard.cvv"/>
		<br/>
		<form:checkbox path="termsAndConditions" />
		<b><spring:message code="writer.edit.termsAndConditions" /></b>
		<form:errors path="termsAndConditions" cssClass="error" />
		<br />
		<spring:message code="writer.edit.submit" var="submit"/>
		<input type="submit" name="submit" onclick="return checkPhone(this.form.phone.value)" value="${ submit}"/>
		<acme:cancel url="/" code="writer.edit.cancel" />
	</form:form>
</jstl:if>
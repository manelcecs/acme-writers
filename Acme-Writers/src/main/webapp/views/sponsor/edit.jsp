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
			if (patt.test(str) == false) { return confirm("<spring:message code="sponsor.edit.phone.error"/>"); }
		}
	}
</script>

<jstl:if test="${edit }">
	<form:form action="sponsor/sponsor/save.do"
		modelAttribute="sponsor">
		<acme:textbox code="sponsor.edit.name" path="name" />
		<acme:textbox code="sponsor.edit.surname" path="surname" />
		<acme:textbox code="sponsor.edit.photoURL" path="photoURL" />
		<acme:textbox code="sponsor.edit.address" path="address" />
		<acme:textbox code="sponsor.edit.companyName" path="companyName" />
		<acme:textbox code="sponsor.edit.email" path="email" />
		<acme:textbox code="sponsor.edit.phoneNumber" path="phoneNumber" id="phone" />
		<br />
		<acme:textbox code="sponsor.edit.creditcard.holder" path="creditCard.holder"/>
		<acme:textbox code="sponsor.edit.creditcard.make" path="creditCard.make"/>
		<acme:inputNumber code="sponsor.edit.creditcard.number" path="creditCard.number"/>
		<acme:inputNumber code="sponsor.edit.creditcard.expirationMonth" path="creditCard.expirationMonth" />
		<acme:inputNumber code="sponsor.edit.creditcard.expirationYear" path="creditCard.expirationYear" />
		<acme:inputNumber code="sponsor.edit.creditcard.CVV" path="creditCard.cvv"/>
		<br/>
		<spring:message code="sponsor.edit.submit" var="submit"/>
		<input type="submit" name="submit" onclick="return checkPhone(this.form.phone.value)" value="${ submit}" />
		<acme:cancel url="/actor/display.do" code="sponsor.edit.cancel" />
	</form:form>
</jstl:if>
<jstl:if test="${not edit }">
	<form:form action="sponsor/save.do"
		modelAttribute="sponsorForm">
		<acme:textbox code="sponsor.edit.username"
			path="userAccount.username" />
		<acme:password code="sponsor.edit.password"
			path="userAccount.password" />
		<acme:password code="sponsor.edit.confirmPassword"
			path="confirmPassword" />

		<acme:textbox code="sponsor.edit.name" path="name" />
		<acme:textbox code="sponsor.edit.surname" path="surname" />
		<acme:textbox code="sponsor.edit.photoURL" path="photoURL" />
		<acme:textbox code="sponsor.edit.address" path="address" />
		<acme:textbox code="sponsor.edit.companyName" path="companyName" />
		<acme:textbox code="sponsor.edit.email" path="email" />
		<acme:textbox code="sponsor.edit.phoneNumber" path="phoneNumber"
			id="phone" />
		<br />
		<acme:textbox code="sponsor.edit.creditcard.holder" path="creditCard.holder"/>
		<acme:textbox code="sponsor.edit.creditcard.make" path="creditCard.make"/>
		<acme:inputNumber code="sponsor.edit.creditcard.number" path="creditCard.number"/>
		<acme:inputNumber code="sponsor.edit.creditcard.expirationMonth" path="creditCard.expirationMonth" />
		<acme:inputNumber code="sponsor.edit.creditcard.expirationYear" path="creditCard.expirationYear" />
		<acme:inputNumber code="sponsor.edit.creditcard.CVV" path="creditCard.cvv"/>
		<br/>
		<form:checkbox path="termsAndConditions" />
		<b><spring:message code="sponsor.edit.termsAndConditions" /></b>
		<form:errors path="termsAndConditions" cssClass="error" />
		<br />
		<spring:message code="sponsor.edit.submit" var="submit"/>
		<input type="submit" name="submit" onclick="return checkPhone(this.form.phone.value)" value="${ submit}"/>
		<acme:cancel url="/" code="sponsor.edit.cancel" />
	</form:form>
</jstl:if>
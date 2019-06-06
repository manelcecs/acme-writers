<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<script type='text/javascript'>
	function checkPhone(str) {
		if (str != "") {
			var patt = new RegExp("^(\[+][1-9][0-9]{0,2}[ ]{1}\[(][1-9][0-9]{0,2}\[)][ ]{1}[0-9]{4,}|\[+][1-9][0-9]{0,2}[ ]{1}[0-9]{4,}|[0-9]{4,}|[ ]{1,})$");
			if (patt.test(str) == false) { return confirm("<spring:message code="publisher.edit.phone.error"/>"); }
		}
	}
</script>

<jstl:if test="${edit }">
	<form:form action="publisher/publisher/save.do" modelAttribute="publisher">
		<acme:textbox code="publisher.edit.name" path="name" />
		<acme:textbox code="publisher.edit.surname" path="surname" />
		<acme:textbox code="publisher.edit.photoURL" path="photoURL" />
		<acme:textbox code="publisher.edit.address" path="address" />
		<acme:textbox code="publisher.edit.vat" path="VAT" />
		<acme:textbox code="publisher.edit.email" path="email" />
		<acme:textbox code="publisher.edit.phoneNumber" path="phoneNumber"
			id="phone" />
		<br />
		<acme:textbox code="publisher.edit.commercialName" path="commercialName" />
		<br />
		<acme:textbox code="publisher.edit.creditcard.holder"
			path="creditCard.holder" />
		<acme:textbox code="writer.edit.creditcard.holder" path="creditCard.holder"/>
		<form:label path="creditCard.make"><spring:message code="publisher.edit.creditcard.make"/></form:label> 
     			<form:select path="creditCard.make" multiple="false" > 
	     			<jstl:forEach items="${makers }" var="make"> 
	     				<form:option value="${ make}" label="${make}" /> 
	     			</jstl:forEach> 
   				</form:select> 
		<acme:inputNumber code="publisher.edit.creditcard.number"
			path="creditCard.number" />
		<acme:inputNumber code="publisher.edit.creditcard.expirationMonth"
			path="creditCard.expirationMonth" />
		<acme:inputNumber code="publisher.edit.creditcard.expirationYear"
			path="creditCard.expirationYear" />
		<acme:inputNumber code="publisher.edit.creditcard.CVV"
			path="creditCard.cvv" />
		<br />
		<spring:message code="publisher.edit.submit" var="submit" />
		<input type="submit" name="submit"
			onclick="return checkPhone(this.form.phone.value)" value="${ submit}" />
		<acme:cancel url="/actor/display.do" code="publisher.edit.cancel" />
	</form:form>
</jstl:if>
<jstl:if test="${not edit }">
	<form:form action="publisher/save.do" modelAttribute="publisherForm">
		<acme:textbox code="publisher.edit.username" path="userAccount.username" />
		<acme:password code="publisher.edit.password" path="userAccount.password" />
		<acme:password code="publisher.edit.confirmPassword"
			path="confirmPassword" />

		<acme:textbox code="publisher.edit.name" path="name" />
		<acme:textbox code="publisher.edit.surname" path="surname" />
		<acme:textbox code="publisher.edit.photoURL" path="photoURL" />
		<acme:textbox code="publisher.edit.address" path="address" />
		<acme:textbox code="publisher.edit.vat" path="VAT" />
		<acme:textbox code="publisher.edit.email" path="email" />
		<acme:textbox code="publisher.edit.phoneNumber" path="phoneNumber"
			id="phone" />
		<br />
		<acme:textbox code="publisher.edit.commercialName" path="commercialName" />
		<br />
		<acme:textbox code="publisher.edit.creditcard.holder"
			path="creditCard.holder" />
		<form:label path="creditCard.make"><spring:message code="publisher.edit.creditcard.make"/></form:label> 
     			<form:select path="creditCard.make" multiple="false" > 
	     			<jstl:forEach items="${makers }" var="make"> 
	     				<form:option value="${ make}" label="${make}" /> 
	     			</jstl:forEach> 
   				</form:select> 
		<acme:inputNumber code="publisher.edit.creditcard.number"
			path="creditCard.number" />
		<acme:inputNumber code="publisher.edit.creditcard.expirationMonth"
			path="creditCard.expirationMonth" />
		<acme:inputNumber code="publisher.edit.creditcard.expirationYear"
			path="creditCard.expirationYear" />
		<acme:inputNumber code="publisher.edit.creditcard.CVV"
			path="creditCard.cvv" />
		<br />
		<form:checkbox path="termsAndConditions" />
		<b><spring:message code="publisher.edit.termsAndConditions" /></b>
		<form:errors path="termsAndConditions" cssClass="error" />
		<br />
		<spring:message code="publisher.edit.submit" var="submit" />
		<input type="submit" name="submit"
			onclick="return checkPhone(this.form.phone.value)" value="${ submit}" />
		<acme:cancel url="/" code="publisher.edit.cancel" />
	</form:form>
</jstl:if>
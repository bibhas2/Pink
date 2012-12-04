<%@ taglib uri="http://mobiarch" prefix="p" %>

<html>
<body>
<a href="list">Back2 to List</a><br/>

<p:errors style="color: red"/>
<p:form action="customers/register">
<%--
Name: <p:input type="text" name="customer.fullName"/><br/>
E-mail: <p:input type="text" name="customer.email"/><br/>
Age: <p:input type="text" name="customer.age"/><br/>
Salary: <p:input type="text" name="customer.salary"/><br/>
Department:
<p:select name="customer.department" size="5" multiple="multiple" items="departments" itemLabel="name" itemValue="id"/>
<br/>
Level: <p:input type="radio" name="customer.level" value="G" label="Gold"/>
<p:input type="radio" name="customer.level" value="S" label="Silver"/>
<p:input type="radio" name="customer.level" value="B" label="Bronze"/>
<br/>
<p:input id="promo" type="checkbox" name="customer.sendEmail" value="Y" label="Send me promotional e-mails"/> 
<br/>
 --%>
 
Name: <p:input type="text" name="customer.fullName" size="35"/><br/>
Salary: <p:input type="text" name="customer.salary" size="35"/><br/>
Birth day: <p:input name="customer.birthDay" size="35" placeholder="mm-dd-yyyy"/><br/>
Level: <p:input type="radio" name="customer.level" value="G" label="Gold"/> 
<p:input type="radio" name="customer.level" value="S" label="Silver"/>
<p:input type="radio" name="customer.level" value="B" label="Bronze"/>
<br/>
<p:input type="checkbox" name="customer.active" value="true" 
    label="Has shopped in last 6 months"/><br/>
<p:input type="checkbox" name="customer.residenceState" value="NY" 
    label="New York Resident"/><br/>

<input type="submit" value="Add Customer"/>
</p:form>
</body>
</html>
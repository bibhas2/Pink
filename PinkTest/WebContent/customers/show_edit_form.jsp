<%@ taglib uri="http://mobiarch" prefix="p" %>

<html>
<body>
<p:errors id="test" class="errors"/>
<p:form action="customers/update">
<%--
Name: <p:input type="text" name="customer.fullName"/><br/>
E-mail: <p:input type="text" name="customer.email"/><br/>
Age: <p:input type="text" name="customer.age"/><br/>
Salary: <p:input type="text" name="customer.salary"/><br/>
Department:<br/>
<p:select name="customer.department" size="5" multiple="multiple" items="departments" itemLabel="name" itemValue="id"/><br/>
Level: <p:input type="radio" name="customer.level" value="G" label="Gold"/>
<p:input type="radio" name="customer.level" value="S" label="Silver"/>
<p:input type="radio" name="customer.level" value="B" label="Bronze"/>
<br/>
<p:input id="promo" type="checkbox" name="customer.sendEmail" value="Y" label="Send me promotional e-mails"/> 
<br/>
 --%>
 
Name: <p:input type="text" name="customer.fullName" size="35"/><br/>
Salary: <p:input type="text" name="customer.salary" size="35"/><br/>
Level: <p:input type="radio" name="customer.level" value="G" label="Gold"/> 
<p:input type="radio" name="customer.level" value="S" label="Silver"/>
<p:input type="radio" name="customer.level" value="B" label="Bronze"/><br/>

<p:input type="checkbox" name="customer.active" value="true" 
    label="Has shopped in last 6 months"/><br/>
<p:input type="checkbox" name="customer.residenceState" value="NY" 
    label="New York Resident2"/><br/>
    
<p:input type="hidden" name="customer.id"/>
<input type="submit" value="Update Customer"/>
</p:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h3><a href="meals">← Back</a></h3>
<h2>${meal == null ? 'Add' : 'Edit'}</h2>

<form method="post" action="meals">
    <c:if test="${meal != null}"><input type="hidden" name="id" value="${meal.id}"/></c:if>

    Дата/время: <input type="datetime-local" name="dateTime"
                      value="${meal != null ? fn:replace(meal.dateTime, 'T', ' ') : ''}" required><br>

    Описание: <input type="text" name="description"
                    value="${meal != null ? meal.description : ''}" required><br>

    Калории: <input type="number" name="calories"
                   value="${meal != null ? meal.calories : ''}" required><br>

    <button type="submit">${meal == null ? 'Add' : 'Save'}</button>
</form>
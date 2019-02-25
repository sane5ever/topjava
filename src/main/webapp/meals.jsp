<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<section>
    <a href="meals?action=add"><img src="img/add.png" alt="Add"> </a>
    <table cellpadding="8" cellspacing="0">
        <tr>
            <th>Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <c:choose>
                <c:when test="${meal.exceed}">
                    <tr style="color:red">
                    <td>${meal.dateTime}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals?uuid=${meal.uuid}&action=edit"><img src="img/pencil.png" alt="Edit"></a></td>
                    <td><a href="meals?uuid=${meal.uuid}&action=delete"><img src="img/delete.png" alt="Delete"></a></td>
                </c:when>
                <c:otherwise>
                    <tr style="color:green">
                        <td>${meal.dateTime}</td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a href="meals?uuid=${meal.uuid}&action=edit"><img src="img/pencil.png" alt="Edit"></a></td>
                        <td><a href="meals?uuid=${meal.uuid}&action=delete"><img src="img/delete.png" alt="Delete"></a></td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </table>
</section>
</body>
</html>
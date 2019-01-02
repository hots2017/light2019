<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>图书修改页面</title>
</head>
<body>
<form action="/book/update" method="post">
<input type="hidden" name="id" value="${book.id}"/>
图书名称：<input type="text" name="bookName" value="${book.bookName}"/><br/>
<input type="submit" value="提交"/>
</form>
</body>
</html>
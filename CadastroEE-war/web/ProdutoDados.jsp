<%-- 
    Document   : ProdutoDados
    Created on : 12 de nov. de 2023, 16:46:27
    Author     : JPZanirati
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dados do Produto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</head>
<body class="container">
    <h1>Dados do Produto</h1>
    
    <form action="ServletProdutoFC" method="post" class="form">
        <input type="hidden" name="acao" value="${empty produto ? 'incluir' : 'alterar'}">
        <c:if test="${not empty produto}">
            <input type="hidden" name="id" value="${produto.id}">
        </c:if>
        
        <div class="mb-3">
            <label for="nome" class="form-label">Nome do Produto:</label>
            <input type="text" id="nome" name="nome" class="form-control" value="${empty produto ? '' : produto.nome}" required>
        </div>
        
        <div class="mb-3">
            <label for="quantidade" class="form-label">Quantidade:</label>
            <input type="number" id="quantidade" name="quantidade" class="form-control" value="${empty produto ? '' : produto.quantidade}" required>
        </div>
        
        <div class="mb-3">
            <label for="preco" class="form-label">Preço:</label>
            <input type="text" id="preco" name="preco" class="form-control" value="${empty produto ? '' : produto.preco}" required>
        </div>
        
        <button type="submit" class="btn btn-primary">Salvar</button>
    </form>
</body>
</html>
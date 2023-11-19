package cadastroee.servlets;

import cadastroee.model.Produto;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author JPZanirati
 */

@WebServlet("/ServletProdutoFC")
public class ServletProdutoFC extends HttpServlet {
    @EJB
    private ProdutoFacadeLocal facade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String destino = "";

        if (acao.equals("listar")) {
            List<Produto> listaProdutos = facade.listarProdutos();
            request.setAttribute("listaProdutos", listaProdutos);
            destino = "ProdutoLista.jsp";
            
        } else if (acao.equals("formIncluir")) {
            destino = "ProdutoDados.jsp";
            
        } else if (acao.equals("formAlterar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Produto produto = facade.buscarProdutoPorId(id);
            request.setAttribute("produto", produto);
            destino = "ProdutoDados.jsp";
            
        } else if (acao.equals("excluir")) {
            int id = Integer.parseInt(request.getParameter("id"));
            facade.removerProduto(id);
            List<Produto> listaProdutos = facade.listarProdutos();
            request.setAttribute("listaProdutos", listaProdutos);
            destino = "ProdutoLista.jsp";
            
        } else if (acao.equals("alterar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Produto produto = facade.buscarProdutoPorId(id);
            // Preencher outros campos com valores do request
            // Realizar alterações no produto via facade
            List<Produto> listaProdutos = facade.listarProdutos();
            request.setAttribute("listaProdutos", listaProdutos);
            destino = "ProdutoLista.jsp";
            
        } else if (acao.equals("incluir")) {
            Produto novoProduto = new Produto();
            // Preencher os campos com valores do request
            facade.inserirProduto(novoProduto);
            List<Produto> listaProdutos = facade.listarProdutos();
            request.setAttribute("listaProdutos", listaProdutos);
            destino = "ProdutoLista.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destino);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

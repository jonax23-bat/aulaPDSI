package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    // ==========================================
    // [GET /alunos] Listagem de todos os alunos
    // ==========================================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "alunos/lista";
    }

    // ==========================================
    // [GET /alunos/novo] Abertura do formulário de novo aluno
    // ==========================================
    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("aluno")) {
            model.addAttribute("aluno", new Aluno());
        }
        return "alunos/formulario";
    }

    // ==========================================
    // [POST /alunos/salvar] Salvar ou atualizar aluno com captura de mensagens de validação
    // ==========================================
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Aluno aluno, RedirectAttributes redirectAttributes, Model model) {
        try {
            service.salvar(aluno);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno salvo com sucesso!");
            return "redirect:/alunos";
        } catch (IllegalArgumentException e) {
            // Se houver erro de validação (ex: RG duplicado ou vazio), retorna ao formulário com o erro
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("aluno", aluno);
            return "alunos/formulario";
        }
    }

    // ==========================================
    // [GET /alunos/editar/{id}] Formulário de edição de aluno
    // ==========================================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("aluno")) {
                model.addAttribute("aluno", service.buscarPorId(id));
            }
            return "alunos/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/alunos";
        }
    }

    // ==========================================
    // [POST /alunos/excluir/{id}] Exclusão de aluno
    // ==========================================
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir aluno: " + e.getMessage());
        }
        return "redirect:/alunos";
    }
}

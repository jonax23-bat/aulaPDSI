package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// ==========================================
// [PARTE 2 - CONTROLLER DE CURSOS]
// Gerencia as rotas HTTP e coordena a comunicação com CursoService e as Views
// ==========================================
@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    // ==========================================
    // [GET /cursos] Listagem de cursos
    // ==========================================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", service.listarTodos());
        return "cursos/lista";
    }

    // ==========================================
    // [GET /cursos/novo] Abrir formulário para novo curso
    // ==========================================
    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", new Curso());
        }
        return "cursos/formulario";
    }

    // ==========================================
    // [POST /cursos/salvar] Salvar ou atualizar curso com validação
    // ==========================================
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Curso curso, RedirectAttributes redirectAttributes, Model model) {
        try {
            service.salvar(curso);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Curso salvo com sucesso!");
            return "redirect:/cursos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("curso", curso);
            return "cursos/formulario";
        }
    }

    // ==========================================
    // [GET /cursos/editar/{id}] Abrir formulário preenchido para edição
    // ==========================================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("curso")) {
                model.addAttribute("curso", service.buscarPorId(id));
            }
            return "cursos/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/cursos";
        }
    }

    // ==========================================
    // [POST /cursos/excluir/{id}] Excluir curso
    // ==========================================
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Curso excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não foi possível excluir o curso: " + e.getMessage());
        }
        return "redirect:/cursos";
    }
}
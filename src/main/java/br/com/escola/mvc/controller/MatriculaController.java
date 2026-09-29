package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.service.AlunoService;
import br.com.escola.mvc.service.CursoService;
import br.com.escola.mvc.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller de rotas HTTP de Matrículas
@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final AlunoService alunoService;
    private final CursoService cursoService;

    public MatriculaController(MatriculaService matriculaService,
                               AlunoService alunoService,
                               CursoService cursoService) {
        this.matriculaService = matriculaService;
        this.alunoService = alunoService;
        this.cursoService = cursoService;
    }

    // Listagem de todas as matrículas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("matriculas", matriculaService.listarTodas());
        return "matriculas/lista";
    }

    // Formulário para nova matrícula
    @GetMapping("/nova")
    public String nova(Model model) {
        if (!model.containsAttribute("matricula")) {
            model.addAttribute("matricula", new Matricula());
        }
        carregarListasAuxiliares(model);
        return "matriculas/formulario";
    }

    // Salvar ou atualizar matrícula
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Matricula matricula, RedirectAttributes redirectAttributes, Model model) {
        try {
            matriculaService.salvar(matricula);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Matrícula realizada com sucesso!");
            return "redirect:/matriculas";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("matricula", matricula);
            carregarListasAuxiliares(model);
            return "matriculas/formulario";
        }
    }

    // Formulário para edição de matrícula
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("matricula")) {
                model.addAttribute("matricula", matriculaService.buscarPorId(id));
            }
            carregarListasAuxiliares(model);
            return "matriculas/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/matriculas";
        }
    }

    // Exclusão de matrícula
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            matriculaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Matrícula excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir matrícula: " + e.getMessage());
        }
        return "redirect:/matriculas";
    }

    // Método auxiliar para abastecer os dropdowns (<select>) do formulário
    private void carregarListasAuxiliares(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("statusList", StatusMatricula.values());
    }
}

package com.michaloruba.obslugasesji.controller;

import com.michaloruba.obslugasesji.entity.Subject;
import com.michaloruba.obslugasesji.rest.NotFoundException;
import com.michaloruba.obslugasesji.service.SpecializationService;
import com.michaloruba.obslugasesji.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private SubjectService subjectService;
    private SpecializationService specializationService;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public SubjectController(SubjectService subjectService, SpecializationService specializationService) {
        this.subjectService = subjectService;
        this.specializationService = specializationService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String showListOfSubjects(@RequestParam("name") Optional<String> name, @RequestParam("page") Optional<Integer> page, Model model){
        int currentPage = page.orElse(1);
        Page<Subject> subjectPage = subjectService.findByName(name.orElse("_"), PageRequest.of(currentPage - 1, 10, Sort.Direction.ASC, "semester", "name"));
        int totalPages = subjectPage.getTotalPages();

        model.addAttribute("subjects", subjectPage);
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "/subjects/subjects-list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        model.addAttribute("subject", new Subject());
        model.addAttribute("specs", specializationService.findAll());
        return "/subjects/subject-form";
    }

    @PostMapping("/save")
    public String saveSubject(@Valid @ModelAttribute Subject subject, BindingResult bindingResult, Model model){
        try {
            if(subject.getSpecialization() == null){
                throw new NotFoundException("Specialization not found");
            }
            specializationService.findById(subject.getSpecialization().getId());
        } catch (NotFoundException e){
            logger.warn(e.getMessage());
            bindingResult.rejectValue("specialization", "error.subject", "invalid specialization (can not be null)");
        }
        if (bindingResult.hasErrors()){
            model.addAttribute("specs", specializationService.findAll());
            return "/subjects/subject-form";
        }
        subjectService.save(subject);
        return "redirect:/subjects/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("subId") int subId, Model model){
        model.addAttribute("subject", subjectService.findById(subId));
        model.addAttribute("specs", specializationService.findAll());
        return "/subjects/subject-form";
    }

    @GetMapping("/delete")
    public String deleteSubject(@RequestParam("subId") int subId){
        subjectService.deleteById(subId);
        return "redirect:/subjects/list";
    }
}

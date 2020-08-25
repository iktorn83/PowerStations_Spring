package com.wkowalczyk.restapi.controller.WEB;

import com.wkowalczyk.restapi.model.Elektrownia;
import com.wkowalczyk.restapi.repo.ElektrownieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Controller
public class ElektrownieControllerWEB {
    private ElektrownieRepository elektrownieRepository;
    private List<Elektrownia> elektrowniaList;

    @Autowired
    public ElektrownieControllerWEB(ElektrownieRepository elektrownieRepository) {
        this.elektrownieRepository = elektrownieRepository;
    }

    @PostConstruct
    private void initData() {
        elektrowniaList = elektrownieRepository.findAll();
        populateData();
    }

    private void populateData() {
    /*Elektrownia elektrowniaJeden = new Elektrownia(1,"Bełchatów",5102);
        Elektrownia elektrowniaDwa = new Elektrownia(2,"Kozienice",4016);
        Elektrownia elektrowniaTrzy = new Elektrownia(3,"Opole",3342);
        Elektrownia elektrowniaCztery = new Elektrownia(4,"Połaniec",1882);

        elektrowniaList.add(elektrowniaJeden);
        List<Zdarzenie> zdarzenia = new ArrayList<>();
        Zdarzenie zdarzenie = new Zdarzenie(1, TypZdarzenia.AWARIA,50,elektrowniaDwa);
        zdarzenia.add(zdarzenie);
        elektrowniaDwa.setZdarzenia(zdarzenia);
        elektrowniaList.add(elektrowniaDwa);
        Zdarzenie zdarzenieDwa = new Zdarzenie(2, TypZdarzenia.PRZERWA,200,elektrowniaCztery);
        List<Zdarzenie> zdarzeniaDwa = new ArrayList<>();
        zdarzeniaDwa.add(zdarzenieDwa);
        zdarzeniaDwa.add(zdarzenie);
        elektrowniaCztery.setZdarzenia(zdarzeniaDwa);
        elektrowniaList.add(elektrowniaTrzy);
        elektrowniaList.add(elektrowniaCztery);*/

     /*   List<Elektrownia> temp = new ArrayList<>(elektrownieRepository.findAll());
        Elektrownia elektrownia1;
        for(Elektrownia elektrownia : temp){
            elektrownia1  =  new Elektrownia(elektrownia.getId(),elektrownia.getNazwaElektrowni(),elektrownia.getMocElektrowni());
          if(elektrownia.getZdarzenia()!=null){
              elektrownia1.setZdarzenia(elektrownia.getZdarzenia());
          }
          elektrowniaList.add(elektrownia1);

        }*/
    }

    @GetMapping("/web/elektrownie")
    public String getListElektrownie(Model elektrownieModel) {
        initData();
        elektrownieModel.addAttribute("elektrownie", elektrowniaList);
        return "list-elektrownie";
    }

    @GetMapping("/web/elektrownie/showElektrownieAdd")
    public String addElektrownie(Model elektrownieModel) {
        elektrownieModel.addAttribute("elektrownia", new Elektrownia());
        return "add-elektrownie";
    }

    @GetMapping("/web/elektrownie/showElektrownieAdd/{error}")
    public String addElektrownie(Model elektrownieModel, @PathVariable(value = "error") String error) {
        elektrownieModel.addAttribute("elektrownia", new Elektrownia());
        elektrownieModel.addAttribute("error", error);
        return "add-elektrownie";
    }

    @GetMapping("/web/elektrownie/showElektrownieUpdate")
    public String updateElektrownie(@RequestParam("elektrowniaId") long elektrowniaId, Model elektrownieModel) {
        Optional<Elektrownia> elektrownia = elektrownieRepository.findById(elektrowniaId);
        if (elektrownia.isPresent()) {
            Elektrownia elektrownia1 = elektrownia.get();
            elektrownia1.setId(elektrowniaId);
            elektrownieModel.addAttribute("elektrownia", elektrownia1);
        }
        return "update-elektrownie";
    }

    @PostMapping("/web/elektrownie/updateElektrownie")
    public String updateElektrownie(@ModelAttribute Elektrownia elektrownia) {
        System.out.println("AJDI" + elektrownia.getId());
        Elektrownia elektrowniaToUpdate = elektrownieRepository.getOne(elektrownia.getId());
        elektrowniaToUpdate.setMocElektrowni(elektrownia.getMocElektrowni().intValue());
        elektrowniaToUpdate.setNazwaElektrowni(elektrownia.getNazwaElektrowni());
        elektrownieRepository.save(elektrowniaToUpdate);
        return "redirect:/web/elektrownie";
    }

    @PostMapping("/web/elektrownie/addElektrownie")
    public String saveElektrownie(@ModelAttribute Elektrownia elektrownia) {
        elektrownia.setId(elektrowniaList.size() + 1);
        elektrownia.setMocElektrowni(elektrownia.getMocElektrowni().intValue());
        boolean flag = true;
        for (Elektrownia elektrownia1 : elektrownieRepository.findAll()) {
            if (elektrownia1.getNazwaElektrowni().equals(elektrownia.getNazwaElektrowni())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            elektrownieRepository.save(elektrownia);
            return "redirect:/web/elektrownie";
        } else return "redirect:/web/elektrownie/showElektrownieAdd/" + elektrownia.getNazwaElektrowni();
    }

    @GetMapping("/web/elektrownie/deleteElektrownie/{id}")
    public String deleteElektrownie(@PathVariable(value = "id") Long id) {
        Optional<Elektrownia> elektrownia = elektrownieRepository.findById(id);
        if (elektrownia.isPresent()) {
            elektrownieRepository.deleteById(id);
        }
        return "redirect:/web/elektrownie";
    }

    @GetMapping
    public String index() {
        return "redirect:/web/elektrownie";
    }

    @RequestMapping("/web/login.html")
    public String login() {
        return "login.html";
    }

    @RequestMapping("/web/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}

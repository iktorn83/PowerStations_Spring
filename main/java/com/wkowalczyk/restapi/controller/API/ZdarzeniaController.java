package com.wkowalczyk.restapi.controller.API;

import com.wkowalczyk.restapi.exception.NotFoundException;
import com.wkowalczyk.restapi.model.Zdarzenie;
import com.wkowalczyk.restapi.repo.ElektrownieRepository;
import com.wkowalczyk.restapi.repo.ZdarzeniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZdarzeniaController {
    @Autowired
    private ZdarzeniaRepository zdarzeniaRepository;

    @Autowired
    private ElektrownieRepository elektrownieRepository;

    @GetMapping("/elektrownie/{idElektrowni}/zdarzenia")
    public List<Zdarzenie> getZdarzenieByElektrownieId(@PathVariable long idElektrowni) {
        assertElektrowniaExists(idElektrowni);
        return zdarzeniaRepository.findByElektrowniaId(idElektrowni);
    }

    @PostMapping("/elektrownie/{idElektrowni}/zdarzenia")
    public Zdarzenie addZdarzenie(@PathVariable long idElektrowni,
                                  @RequestBody Zdarzenie zdarzenieToSave) {
        return elektrownieRepository.findById(idElektrowni)
                .map(zdarzenie -> {
                    zdarzenieToSave.setElektrownia(zdarzenie);
                    return zdarzeniaRepository.save(zdarzenieToSave);
                }).orElseThrow(() -> new NotFoundException("Brak elektrowni o zadanym ID:" + idElektrowni));
    }

    @PutMapping("/elektrownie/{idElektrowni}/zdarzenia/{idZdarzenia}")
    public Zdarzenie updateZdarzenie(@PathVariable long idElektrowni,
                                     @PathVariable long idZdarzenia,
                                     @RequestBody Zdarzenie zdarzenieToUpdate) {
        assertElektrowniaExists(idElektrowni);
        return zdarzeniaRepository.findById(idZdarzenia)
                .map(zdarzenie -> {
                    zdarzenie.setTypZdarzenia(zdarzenieToUpdate.getTypZdarzenia());
                    zdarzenie.setUbytekMocy(zdarzenieToUpdate.getUbytekMocy());
                    zdarzenie.setDataRozpoczecia(zdarzenieToUpdate.getDataRozpoczecia());
                    zdarzenie.setDataZakonczenia(zdarzenieToUpdate.getDataZakonczenia());
                    zdarzenie.setElektrownia(zdarzenieToUpdate.getElektrownia());
                    return zdarzeniaRepository.save(zdarzenie);
                }).orElseThrow(() -> new NotFoundException("Brak zdarzenia o zadanym ID:" + idZdarzenia));
    }

    @DeleteMapping("/elektrownie/{idElektrowni}/zdarzenia/{idZdarzenia}")
    public String deleteZdarzenie(@PathVariable long idElektrowni,
                                  @PathVariable long idZdarzenia) {
        assertElektrowniaExists(idElektrowni);
        assertZdarzenieExistsInElektrownia(idElektrowni, idZdarzenia);

        return zdarzeniaRepository.findById(idZdarzenia)
                .map(zdarzenie -> {
                    zdarzeniaRepository.delete(zdarzenie);
                    return "Usunięto poprawnie!";
                }).orElseThrow(() -> new NotFoundException("Brak zdarzenia o zadanym ID:" + idZdarzenia));
    }

    private void assertElektrowniaExists(long id) {
        if (!elektrownieRepository.existsById(id)) {
            throw new NotFoundException("Brak elektrowni o zadanym ID:" + id);
        }
    }

    private void assertZdarzenieExistsInElektrownia(long idElektrowni, long idZdarzenia) {
        Zdarzenie zdarzenie = zdarzeniaRepository.getOne(idZdarzenia);
        long idMacierzystejElektrowni = zdarzenie.getElektrownia().getId();

        if (elektrownieRepository.getOne(idElektrowni).getId() != idMacierzystejElektrowni) {
            throw new NotFoundException("Brak zdarzenia o zadanym ID:" + idZdarzenia + " w podanej elektrowni");
        }
    }

}

package ru.job4j.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.application.domain.Person;
import ru.job4j.application.domain.Report;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private RestTemplate rest;

    private static final String API = "http://localhost:8080/person/";

    private static final String API_ID = "http://localhost:8080/person/{id}";

    @GetMapping("/report")
    public String getFullReport(Model model) {
        model.addAttribute("reports", getListReport());
        return "report";
    }

    @PostMapping("/save")
    public String createPerson(@ModelAttribute Person person) {
        rest.postForObject(API, person, Person.class);
        return "redirect:/reports/report";
    }

    @GetMapping("/update")
    public String getFormEdit(@RequestParam("id") int id, Model model) {
        Person remote = rest.getForObject(API_ID, Person.class, id);
        model.addAttribute("person", remote);
        return "edit";
    }

    @PostMapping("/update")
    public String updatePerson(@ModelAttribute Person person) {
        rest.put(API, person);
        return "redirect:/reports/report";
    }

    @GetMapping("/delete")
    public String deletePerson(@RequestParam("id") int id) {
        rest.delete(API_ID, id);
        return "redirect:/reports/report";
    }

    private List<Report> getListReport() {
        List<Report> rsl = new ArrayList<>();
        int ids = 1;
        List<Person> persons = rest.exchange(
                API,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() { }
        ).getBody();
        for (Person person : persons) {
            Report report = Report.of(ids, "First", person);
            rsl.add(report);
        }
        return rsl;
    }
}

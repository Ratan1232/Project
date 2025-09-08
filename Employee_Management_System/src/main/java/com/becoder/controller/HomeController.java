package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.becoder.entity.Employee;
import com.becoder.service.EmpService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private EmpService empService;

    // Home page (list of employees)
    @GetMapping("/")
    public String index(Model m, HttpSession session) {
        List<Employee> list = empService.getAllEmp();
        m.addAttribute("empList", list);

        // ✅ Show session message if available
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            m.addAttribute("msg", msg);
            session.removeAttribute("msg"); // clear after showing once
        }

        return "index";
    }

    // Load Save Employee Form
    @GetMapping("/loadEmpSave")
    public String loadEmpSave() {
        return "emp_save";
    }

    // Edit Employee Form
    @GetMapping("/editEmp/{id}")
    public String editEmp(@PathVariable int id, Model model) {
        Employee emp = empService.getEmpById(id);
        model.addAttribute("emp", emp);
        return "edit_Emp"; // Thymeleaf template
    }

    // Save New Employee
    @PostMapping("/saveEmp")
    public String saveEmp(@ModelAttribute Employee emp, HttpSession session) {
        Employee newEmp = empService.saveEmp(emp);

        if (newEmp != null) {
            session.setAttribute("msg", "Employee registered successfully!");
        } else {
            session.setAttribute("msg", "Something went wrong on server!");
        }
        return "redirect:/";
    }

    // ✅ Update Existing Employee
    @PostMapping("/updateEmp")
    public String updateEmp(@ModelAttribute Employee emp, HttpSession session) {
        Employee updatedEmp = empService.saveEmp(emp); // save() works as update if ID exists

        if (updatedEmp != null) {
            session.setAttribute("msg", "Employee updated successfully!");
        } else {
            session.setAttribute("msg", "Update failed! Try again.");
        }
        return "redirect:/";
    }

 // Delete Employee (GET method for simplicity)
    @GetMapping("/deleteEmp/{id}")
    public String deleteEmp(@PathVariable int id, HttpSession session) {
        empService.deleteEmp(id);
        session.setAttribute("msg", "Employee deleted successfully!");
        return "redirect:/";
    }

}

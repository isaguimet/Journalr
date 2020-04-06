package Journalr.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;

import Journalr.com.repositories.EditorRepository;
import Journalr.com.repositories.PaperRepository;
import Journalr.com.repositories.ReviewerRepository;
import Journalr.com.repositories.UserRepository;

import Journalr.com.model.User;

import java.util.*;
//import java.util.Map;

import Journalr.com.model.Paper;
import Journalr.com.model.Reviewer;

@Controller
public class EditorController {

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    EditorRepository editorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewerRepository reviewerRepository;
    /**
     * This method takes in the current displaying model as input.  It responds to the mapping 
     * /editor (the edtior page).  Upon clicking this, it would re-display a admin view with a list
     * of all the papers in the system.
     * @param model The model is the current displaying template.
     * @return
     */
    @RequestMapping("/editor")
    public String showAllPapers(Model model) {
        List<Paper> allPapers = paperRepository.findAll();
        model.addAttribute("allPapers", allPapers);

        return "editor";
    }

    /**
     * This method adds the paper to the approved journals.
     * @param paperID the id of the paper we want to add to the journal
     * @return this method will redirect back to the editor page
     */
    @RequestMapping(path="/addjournal/{paperID}")
    public String addJournal(@PathVariable(name = "paperID") int paperID) {
        
        Paper paper = paperRepository.findById(paperID).get();
        paper.setApproved(true);
        paperRepository.save(paper);

        return "redirect:/editor";
    }

    /**
     * This method shows the reviewers for the given paper
     * @param paperID the id of the paper we want to show the reviewers of
     * @return this method will redirect to the reviewersperpaper page
     */
    @RequestMapping(path="/reviewersperpaper/{paperID}")
    public String showReviewersPerPaper(@PathVariable(name = "paperID") int paperID,
                                        Model model) {
        
        Paper paper = paperRepository.findById(paperID).get();
        model.addAttribute("paper", paper);

        List<Integer> listReviewerIds = paperRepository.findReviewersPerPaper(paperID);
        List<User> listReviewers = new ArrayList<User>();
        for (Integer reviewerId : listReviewerIds) {
            listReviewers.add(userRepository.findById(reviewerId).get());
        }

        model.addAttribute("listReviewers", listReviewers);

        return "reviewersperpaper";
    }

}
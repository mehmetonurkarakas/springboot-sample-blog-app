package com.example.springbootblogApp.controllers;

import com.example.springbootblogApp.models.Account;
import com.example.springbootblogApp.models.Post;
import com.example.springbootblogApp.services.AccountService;
import com.example.springbootblogApp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/posts/{id}")
    public String post(Model model, @PathVariable Long id) {
        Optional<Post> post = postService.getById(id);

        if (post.isEmpty()) {
            return "404";
        }
        Post post1 = post.get();
        model.addAttribute("post", post1);
        return "post";
    }

    @GetMapping("/posts/new")
    public String newPost(Model model) {
        Optional<Account> optionalAccount = accountService.findByEmail("user@gmail.com");
        if(optionalAccount.isPresent()){
            Post post = new Post();
            model.addAttribute("post", post);
            post.setAccount(optionalAccount.get());
            return "post_new";
        }
        return "404";
    }

    @PostMapping("/posts/new")
    public String createPost(@ModelAttribute Post post) {

        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editPost(Model model, @PathVariable Long id) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        }
        return "404";
    }


    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id,Post post, BindingResult result,Model model){
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            postService.save(existingPost);
        }
        return "redirect:/posts/" + post.getId() ;
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {

        Optional<Post> optionalPost = postService.getById(id);
        optionalPost.ifPresent(post -> postService.delete(post));


        return "redirect:/";
    }

}

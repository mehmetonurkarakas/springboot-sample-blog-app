package com.example.springbootblogApp.config;

import com.example.springbootblogApp.models.Account;
import com.example.springbootblogApp.models.Authority;
import com.example.springbootblogApp.models.Post;
import com.example.springbootblogApp.repositories.AuthorityRepository;
import com.example.springbootblogApp.services.AccountService;
import com.example.springbootblogApp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {


     @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;



    @Override
    public void run(String... args) throws Exception {

        List<Post> posts = postService.getAll();

        if (posts.isEmpty()) {

            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);


            Account account1 = new Account();
            account1.setFirstName("user");
            account1.setLastName("user");
            account1.setEmail("user@gmail.com");
            account1.setPassword("password");
            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);

            accountService.save(account1);

            Account account2 = new Account();
            account2.setFirstName("admin");
            account2.setLastName("admin");
            account2.setEmail("admin@gmail.com");
            account2.setPassword("password");
            Set<Authority> authorities2 = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
            authorityRepository.findById("ROLE_USER").ifPresent(authorities2::add);
            account2.setAuthorities(authorities2);

            accountService.save(account2);
            Post post1 = new Post();
            post1.setTitle("First Post");
            post1.setBody("This is the first post");
            post1.setAccount(account1);
            postService.save(post1);

            Post post2 = new Post();
            post2.setTitle("Second Post");
            post2.setBody("This is the second post");
            post2.setAccount(account2);
            postService.save(post2);
        }
    }



}

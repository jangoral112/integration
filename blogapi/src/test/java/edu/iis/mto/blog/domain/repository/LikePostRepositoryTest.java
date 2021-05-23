package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class LikePostRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;
    
    private User sampleUser;
    
    private BlogPost sampleBlogPost;
    
    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setAccountStatus(AccountStatus.CONFIRMED);
        sampleUser.setEmail("sample@mail.com");
        
        sampleBlogPost = new BlogPost();
        sampleBlogPost.setEntry("Sample Entry");
        sampleBlogPost.setUser(sampleUser);
    }

    @Test
    void shouldSaveLikePost() {
        entityManager.persistAndFlush(sampleUser);
        entityManager.persistAndFlush(sampleBlogPost);

        LikePost likePost = new LikePost();
        likePost.setUser(sampleUser);
        likePost.setPost(sampleBlogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        assertThat(persistedLikePost.getId(), notNullValue());
    }

    @Test
    void shouldAddSavedLikePostToLikesInBlogPost() {
        entityManager.persistAndFlush(sampleUser);
        entityManager.persistAndFlush(sampleBlogPost);
        
        LikePost likePost = new LikePost();
        likePost.setUser(sampleUser);
        likePost.setPost(sampleBlogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        entityManager.refresh(sampleBlogPost);
        assertThat(sampleBlogPost.getLikesCount(), equalTo(1));
        assertThat(sampleBlogPost.getLikes().get(0), equalTo(persistedLikePost));
    }

    @Test
    void shouldModifyUserInLikePost() {
        entityManager.persistAndFlush(sampleUser);
        entityManager.persistAndFlush(sampleBlogPost);
        LikePost persistedLikePost = new LikePost();
        persistedLikePost.setUser(sampleUser);
        persistedLikePost.setPost(sampleBlogPost);
        entityManager.persistAndFlush(persistedLikePost);

        User userToUpdateWith = new User();
        userToUpdateWith.setAccountStatus(AccountStatus.CONFIRMED);
        String userMail = "newUser@mail.com";
        userToUpdateWith.setEmail(userMail);
        entityManager.persistAndFlush(userToUpdateWith);

        LikePost likePost = new LikePost();
        likePost.setId(persistedLikePost.getId());
        likePost.setPost(sampleBlogPost);
        likePost.setUser(userToUpdateWith);
        likePostRepository.save(likePost);

        entityManager.flush();

        entityManager.refresh(persistedLikePost);
        assertThat(persistedLikePost.getUser().getId(), equalTo(userToUpdateWith.getId()));
        assertThat(persistedLikePost.getUser().getEmail(), equalTo(userMail));
    }

    @Test
    void shouldModifyBlogPostInLikePost() {
        entityManager.persistAndFlush(sampleUser);
        entityManager.persistAndFlush(sampleBlogPost);
        LikePost persistedLikePost = new LikePost();
        persistedLikePost.setUser(sampleUser);
        persistedLikePost.setPost(sampleBlogPost);
        entityManager.persistAndFlush(persistedLikePost);

        BlogPost blogPostToUpdateWith = new BlogPost();
        String blogPostEntry = "BlogPost Entry";
        blogPostToUpdateWith.setEntry(blogPostEntry);
        blogPostToUpdateWith.setUser(sampleUser);
        entityManager.persistAndFlush(blogPostToUpdateWith);

        LikePost likePost = new LikePost();
        likePost.setId(persistedLikePost.getId());
        likePost.setPost(blogPostToUpdateWith);
        likePost.setUser(sampleUser);
        likePostRepository.save(likePost);

        entityManager.flush();

        entityManager.refresh(persistedLikePost);
        assertThat(persistedLikePost.getPost().getId(), equalTo(blogPostToUpdateWith.getId()));
        assertThat(persistedLikePost.getPost().getEntry(), equalTo(blogPostEntry));
    }
    
}

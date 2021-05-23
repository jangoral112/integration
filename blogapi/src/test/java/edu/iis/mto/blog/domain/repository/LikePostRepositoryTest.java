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
        entityManager.persist(sampleUser);
        entityManager.persist(sampleBlogPost);
        entityManager.flush();

        LikePost likePost = new LikePost();
        likePost.setUser(sampleUser);
        likePost.setPost(sampleBlogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        assertThat(persistedLikePost.getId(), notNullValue());
    }

    @Test
    void shouldAddSavedLikePostToLikesInBlogPost() {
        entityManager.persist(sampleUser);
        entityManager.persist(sampleBlogPost);
        entityManager.flush();
        
        LikePost likePost = new LikePost();
        likePost.setUser(sampleUser);
        likePost.setPost(sampleBlogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        entityManager.refresh(sampleBlogPost);
        assertThat(sampleBlogPost.getLikesCount(), equalTo(1));
        assertThat(sampleBlogPost.getLikes().get(0), equalTo(persistedLikePost));
    }
    
}

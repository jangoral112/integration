package edu.iis.mto.blog.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LikePostRepository likePostRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Captor
    private ArgumentCaptor<LikePost> likePostCaptor;

    private User samplePostOwner;

    private BlogPost sampleBlogPost;

    @BeforeEach
    void setUp() {
        samplePostOwner = new User();
        Long ownerId = 2L;
        samplePostOwner.setId(ownerId);

        sampleBlogPost = new BlogPost();
        sampleBlogPost.setUser(samplePostOwner);
        Long postId = 1L;
        sampleBlogPost.setId(postId);
    }

    @Test
    void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), equalTo(AccountStatus.NEW));
    }

    @Test
    void shouldAddLikeToBlogPostWhenUserIsCONFIRMED() {
        User confirmedUser = new User();
        Long confirmedUserId = 1L;
        confirmedUser.setId(confirmedUserId);
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);

        when(userRepository.findById(confirmedUserId)).thenReturn(Optional.of(confirmedUser));
        when(blogPostRepository.findById(sampleBlogPost.getId())).thenReturn(Optional.of(sampleBlogPost));
        when(likePostRepository.findByUserAndPost(confirmedUser, sampleBlogPost)).thenReturn(Optional.empty());
        doReturn(null).when(likePostRepository).save(likePostCaptor.capture());

        Boolean expectedResult = true;

        Boolean result =  blogService.addLikeToPost(confirmedUserId, sampleBlogPost.getId());

        assertThat(result, equalTo(expectedResult));
        LikePost likePost = likePostCaptor.getValue();
        assertThat(likePost.getPost().getId(), equalTo(sampleBlogPost.getId()));
        assertThat(likePost.getUser().getId(), equalTo(confirmedUserId));
    }
}

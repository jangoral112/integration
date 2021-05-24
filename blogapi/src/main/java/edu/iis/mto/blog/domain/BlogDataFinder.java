package edu.iis.mto.blog.domain;

import java.util.List;
import java.util.stream.Collectors;

import edu.iis.mto.blog.domain.model.AccountStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.dto.PostData;
import edu.iis.mto.blog.dto.UserData;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.DataFinder;

import javax.persistence.EntityNotFoundException;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Service
public class BlogDataFinder extends DomainService implements DataFinder {

    protected BlogDataFinder(UserRepository userRepository, BlogPostRepository blogPostRepository, LikePostRepository likePostRepository,
            BlogDataMapper mapper) {
        super(userRepository, blogPostRepository, likePostRepository, mapper);
    }

    @Override
    public UserData getUserData(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapper.mapToDto(user);
    }

    @Override
    public List<UserData> findUsers(String searchString) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString,
                searchString, searchString);

        return users.stream()
                    .filter(user -> user.getAccountStatus() != AccountStatus.REMOVED)
                    .map(mapper::mapToDto)
                    .collect(Collectors.toList());
    }

    @Override
    public PostData getPost(Long postId) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                                              .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        return mapper.mapToDto(blogPost);
    }

    @Override
    public List<PostData> getUserPosts(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(user.getAccountStatus() != AccountStatus.CONFIRMED) {
            throw new DomainError(DomainError.USER_NOT_CONFIRMED);
        }

        List<BlogPost> posts = blogPostRepository.findByUser(user);
        return posts.stream()
                    .map(mapper::mapToDto)
                    .collect(Collectors.toList());
    }

}

package co.com.post_comments.alpha.domain.post.entities.root;

import co.com.post_comments.alpha.domain.post.entities.Comment;
import co.com.post_comments.alpha.domain.post.events.CommentAdded;
import co.com.post_comments.alpha.domain.post.events.PostCreated;
import co.com.post_comments.alpha.domain.post.values.Author;
import co.com.post_comments.alpha.domain.post.values.Content;
import co.com.post_comments.alpha.domain.post.values.Date;
import co.com.post_comments.alpha.domain.post.values.Title;
import co.com.post_comments.alpha.domain.post.values.identities.CommentId;
import co.com.post_comments.alpha.domain.post.values.identities.PostId;
import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class Post extends AggregateEvent<PostId> {
    String author;
    String title;
    String postedAt;
    List<Comment> comments;

    private Post(PostId entityId) {
        super(entityId);
        super.subscribe(new PostEventListener(this));
    }

    public Post(PostId entityId, Author author, Title title, Date postedAt) {
        super(entityId);
        super
                .appendChange(new PostCreated(entityId.value(), title.value(), author.value(), postedAt.value().toString()))
                .apply();
    }

    public static Post from(PostId postId, List<DomainEvent> events) {
        Post post = new Post(postId);
        events.forEach(post::applyEvent);
        return post;
    }

    public void addComment(PostId postId, CommentId commentId, Author commentAuthor, Content commentContent, Date postedAt) {
        super
                .appendChange(new CommentAdded(postId.value(), commentId.value(), commentAuthor.value(), commentContent.value(), postedAt.value().toString()))
                .apply();
    }

    public String author() {
        return author;
    }

    public String title() {
        return title;
    }

    public String postedAt() {
        return postedAt;
    }

    public List<Comment> comments() {
        return comments;
    }
}

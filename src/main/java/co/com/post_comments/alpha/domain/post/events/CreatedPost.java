package co.com.post_comments.alpha.domain.post.events;

import co.com.post_comments.alpha.domain.post.identities.PostId;
import co.com.post_comments.alpha.domain.post.values.Author;
import co.com.post_comments.alpha.domain.post.values.Date;
import co.com.post_comments.alpha.domain.post.values.Title;
import co.com.sofka.domain.generic.DomainEvent;

public class CreatedPost extends DomainEvent {
    private final PostId postId;
    private final Title title;
    private final Author author;
    private final Date postedAt;

    public CreatedPost(PostId postId, Title title, Author author, Date postedAt) {
        super("PostAndComments.posts.CreatedPost");
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.postedAt = postedAt;
    }

    public PostId postId() {
        return postId;
    }

    public Title title() {
        return title;
    }

    public Author author() {
        return author;
    }

    public Date postedAt() {
        return postedAt;
    }
}

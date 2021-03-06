package demo;

import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.richfaces.push.MessageException;
import org.richfaces.push.TopicKey;
import org.richfaces.push.TopicsContext;

@ManagedBean
public class RichBean {

    public Date getDate() {
        return new Date();
    }

    public void push() throws MessageException {
        TopicKey topicKey = new TopicKey("sampleAddress");
        TopicsContext topicsContext = TopicsContext.lookup();

        topicsContext.publish(topicKey, new Date().toString());

        System.out.println("push event");
    }
}

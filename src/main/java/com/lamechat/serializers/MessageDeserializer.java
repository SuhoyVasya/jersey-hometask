package com.lamechat.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lamechat.model.Message;
import com.lamechat.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDeserializer extends StdDeserializer<Message> {

    public MessageDeserializer() {
        this(null);
    }

    public MessageDeserializer(Class<Message> vc) {
        super(vc);
    }

    @Override
    public Message deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY hh:mm:ss");
        try {
            JsonNode node = p.getCodec().readTree(p);
            String nickname = node.get("nickname").textValue();
            String text = node.get("text").textValue();
            Date date = format.parse(node.get("date").textValue());
            Message.MessageType type = Message.MessageType.valueOf(node.get("type").textValue());
            JsonNode hashNode = node.get("userID");
            if(hashNode!=null){
                String hash = hashNode.textValue();
                User user = User.usersData.get(hash);
                return new Message(type, text, date, user);
            }
            for (User user : User.usersData.values()) {
                if (user.getNickname().equals(nickname)) {
                    return new Message(type, text, date, user);
                }
            }
        } catch (IOException | ParseException e) {
        }
        return null;
    }
}


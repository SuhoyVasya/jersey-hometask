package com.lamechat.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lamechat.model.Message;


import java.io.IOException;
import java.text.SimpleDateFormat;

public class MessageSerializer extends StdSerializer<Message> {

    public MessageSerializer() {
        this(null);
    }

    public MessageSerializer(Class<Message> t) {
        super(t);
    }

    @Override
    public void serialize(Message value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
        gen.writeStartObject();
        gen.writeStringField("type", value.getType().toString());
        gen.writeStringField("nickname", value.getUser().getNickname());
        gen.writeStringField("text", value.getText());
        gen.writeStringField("date", format.format(value.getDate()));
        gen.writeEndObject();


    }
}

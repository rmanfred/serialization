package io.adelina.serialization.json;

import com.acme.avro.MyClass;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import com.acme.avro.relatives;
import org.msgpack.core.*;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.MapValue;
import org.msgpack.value.Value;
import org.yaml.snakeyaml.Yaml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.*;

public class Main {
    // Times to run loops to test
    private static final int timesToRun = 1000;

    public static void main(String[] args) {

        try {
            out.println("Serialization: ");
            serializeUserProfile();
            out.println("Deserialization: ");
            deserializeUserProfile();
        } catch (IOException file)
        {
            out.println("Exeption occured: " + file.getMessage());
        }

    }

    /**
     * Serialization
     * @throws IOException
     */
    private static void serializeUserProfile() throws IOException {


        // Data for UserProfile object
        float salary = 120000.45f;
        List<String> friends = new ArrayList<>();
        friends.add("Irina");
        friends.add("Sasha");
        friends.add("Sergey");
        HashMap<String, Integer> relatives = new HashMap<>();
        relatives.put("Asya", 49);
        relatives.put("Ruslan", 54);
        relatives.put("Farkhat", 27);

        // Set data
        UserProfile adelina = new UserProfile();
        adelina.setName("Adelina");
        adelina.setEmail("dzhalelova@mail.com");
        adelina.setAge(23);
        adelina.setDeveloper(true);
        adelina.setSalary(salary);
        adelina.setFriends(friends);
        adelina.setRelatives(relatives);

        // Java native
        serializeJavaNative(adelina);

        // JSON
        serializeJson(adelina);

        // XML
        serializeXml(adelina);

        // Avro
        serializeAvro(adelina);

        // Yaml
        serializeYaml(adelina);

        // Message pack
        serializeMessagePack(adelina);

    }

    private static void serializeJavaNative(final UserProfile adelina) throws IOException {
        out.println("Performance test running for serialization Java:");
        long theCurrentTimeJava = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream("person.out"));
            objectOutputStream.writeObject(adelina);
            objectOutputStream.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeJava + " ms processing time for Java");
    }

    private static void serializeJson(UserProfile adelina) throws IOException {
        out.println("Performance test running for serialization JSON:");
        long theCurrentTimeJSON = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            Writer writer = new FileWriter("fileJson.JSON");
            Gson gson = new Gson();
            gson.toJson(adelina, writer);
            writer.close();

        }
        out.println(currentTimeMillis() - theCurrentTimeJSON + " ms processing time for JSON");
    }

    private static void serializeXml(UserProfile adelina) throws FileNotFoundException {
        out.println("Performance test running for serialization XML:");
        long theCurrentTimeXML = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            XMLEncoder xml = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("myProfile.xml")));
            xml.writeObject(adelina);
            xml.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeXML + " ms processing time for XML");
    }

    private static void serializeAvro(UserProfile adelina) throws IOException {

        float salary = 120000.45f;
        List<CharSequence> friends = new ArrayList<>();
        friends.add("Irina");
        friends.add("Sasha");
        friends.add("Sergey");

        relatives rels = new relatives();
        rels.setAsya(49);
        rels.setFarkhat(27);
        rels.setRuslan(54);

        MyClass user = new MyClass();
        user.setName("Adelina");
        user.setAge(23);
        user.setEmail("dzhalelova@yandex.ru");
        user.setIsDeveloper(true);
        user.setSalary(salary);
        user.setFriends(friends);
        user.setRelatives(rels);

        out.println("Performance test running for serialization Avro:");
        long theCurrentTimeAvro = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            // ApacheAvro
            DatumWriter<MyClass> userProfileDatumWriter = new SpecificDatumWriter<>(MyClass.class);
            DataFileWriter<MyClass> dataFileWriter = new DataFileWriter<>(userProfileDatumWriter);
            dataFileWriter.create(user.getSchema(), new File("user.avro"));
            dataFileWriter.append(user);
            dataFileWriter.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeAvro + " ms processing time for Avro");
    }

    private static void serializeYaml(UserProfile adelina) throws IOException {
        out.println("Performance test running for serialization Yaml:");
        long theCurrentTimeYaml = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++){
            Writer writerYml = new FileWriter("yamlSer.yml");
            Yaml yml = new Yaml();
            yml.dump(adelina, writerYml);
            writerYml.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeYaml + " ms processing time for YAML");
    }

    private static void serializeMessagePack(UserProfile adelina) throws IOException {
        out.println("Performance test running for serialization Message Pack:");
        long theCurrentTimeMsgPack = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            File file = new File("msgPack.txt");
            MessagePacker packer = MessagePack.newDefaultPacker(new FileOutputStream(file));
            packer.packString(adelina.getName());
            packer.packString(adelina.getEmail());
            packer.packInt(adelina.getAge());
            packer.packBoolean(adelina.isDeveloper());
            packer.packFloat(adelina.getSalary());
            packer.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeMsgPack + " ms processing time for MessagePack");
    }

    /**
     * Deserialization
     * @throws IOException
     */
    private static void deserializeUserProfile() throws IOException {
        // Java native
        deserializeJava();

        // JSON
        deserializeJson();

        // XML
        deserializeXml();

        // Avro
        deserializeAvro();

        // Yaml
        deserializeYaml();

        // Message Pack
        deserializeMessagePack();

    }

    private static void deserializeJava() throws IOException {
        out.println("Performance test running for deserialization Java:");

        long theCurrentTimeJava = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("person.out"));
            try {
                UserProfile userJava = (UserProfile) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            objectInputStream.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeJava + " ms processing time for Java");

        // Repeat to output
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("person.out"));
        try {
            UserProfile userJava = (UserProfile) objectInputStream.readObject();
            out.println("User Java: " + userJava.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        objectInputStream.close();
    }

    private static void deserializeJson() throws IOException {
        out.println("Performance test running for deserialization JSON:");

        long theCurrentTimeJSON = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("fileJson.json");
            JsonReader reader = new JsonReader(fileReader);
            UserProfile user = gson.fromJson(reader, UserProfile.class);
            reader.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeJSON + " ms processing time for JSON");

        // Repeat to output
        Gson gson = new Gson();
        FileReader fileReader = new FileReader("fileJson.json");
        JsonReader reader = new JsonReader(fileReader);
        UserProfile user = gson.fromJson(reader, UserProfile.class);
        out.println("User Json: " + user.toString());
        reader.close();
    }

    private static void deserializeXml() throws FileNotFoundException {
        out.println("Performance test running for deserialization XML:");

        long theCurrentTimeXML = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            XMLDecoder xml = new XMLDecoder(new BufferedInputStream(new FileInputStream("myProfile.xml")));
            UserProfile userXML = (UserProfile) xml.readObject();
            xml.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeXML + " ms processing time for XML");

        // Repeat to output
        XMLDecoder xml = new XMLDecoder(new BufferedInputStream(new FileInputStream("myProfile.xml")));
        UserProfile userXML = (UserProfile) xml.readObject();
        out.println("User Xml: " + userXML.toString());
        xml.close();
    }

    private static void deserializeAvro() throws IOException {
        out.println("Performance test running for serialization Avro:");

        long theCurrentTimeAvro = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++) {
            DatumReader<MyClass> userProfileAvroDatumReader = new SpecificDatumReader<>(MyClass.class);
            DataFileReader<MyClass> dataFileReader = new DataFileReader<MyClass>(
                    new File("user.avro"), userProfileAvroDatumReader);
            MyClass userProfileAvro = dataFileReader.next();
            dataFileReader.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeAvro + " ms processing time for Avro");

        // Repeat to output
        DatumReader<MyClass> userProfileAvroDatumReader = new SpecificDatumReader<>(MyClass.class);
        DataFileReader<MyClass> dataFileReader = new DataFileReader<MyClass>(
                new File("user.avro"), userProfileAvroDatumReader);
        MyClass userProfileAvro = dataFileReader.next();
        out.println("User Avro: " + userProfileAvro.toString());
        dataFileReader.close();
    }

    private static void deserializeYaml() throws IOException {
        out.println("Performance test running for serialization Yaml:");

        long theCurrentTimeYaml = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++){
            InputStream inputStream = new FileInputStream("yamlSer.yml");
            Yaml yaml = new Yaml();
            UserProfile userProfile = yaml.load(inputStream);
            inputStream.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeYaml + " ms processing time for Yaml");

        // Repeat to output
        InputStream inputStream = new FileInputStream("yamlSer.yml");
        Yaml yaml = new Yaml();
        UserProfile userProfile = yaml.load(inputStream);
        out.println("User Yaml: " + userProfile.toString());
        inputStream.close();
    }

    private static void deserializeMessagePack() throws IOException {
        out.println("Performance test running for serialization MessagePack:");

        long theCurrentTimeMsgPack = currentTimeMillis();
        for (int i = 0; i < timesToRun; i++){
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new FileInputStream("msgPack.txt"));
            while (unpacker.hasNext()){
                MessageFormat format = unpacker.getNextFormat();
                Value v = unpacker.unpackValue();
                switch (v.getValueType()) {
                    case BOOLEAN:
                        boolean b = v.asBooleanValue().getBoolean();
                        break;
                    case INTEGER:
                        IntegerValue iv = v.asIntegerValue();
                        if (iv.isInIntRange()) {
                            int q = iv.toInt();
                        }
                        break;
                    case STRING:
                        String s = v.asStringValue().asString();
                        break;
                    case MAP:
                        MapValue map = v.asMapValue();
                    case ARRAY:
                        ArrayValue list =  v.asArrayValue();
                    case FLOAT:
                        Float f = v.asFloatValue().toFloat();
                }
            }
            unpacker.close();
        }
        out.println(currentTimeMillis() - theCurrentTimeMsgPack + " ms processing time for Message Pack");

//        // Repeat to output
//        UserProfile userProfile = new UserProfile();
//        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new FileInputStream("msgPack.txt"));
//        while (unpacker.hasNext()){
//            MessageFormat format = unpacker.getNextFormat();
//            Value v = unpacker.unpackValue();
//            switch (v.getValueType()) {
//                case BOOLEAN:
//                    boolean b = v.asBooleanValue().getBoolean();
//                    userProfile.setDeveloper(b);
//                    break;
//                case INTEGER:
//                    IntegerValue iv = v.asIntegerValue();
//                    if (iv.isInIntRange()) {
//                        int q = iv.toInt();
//                        userProfile.setAge(q);
//                    }
//                    break;
//                case STRING:
//                    String s = v.asStringValue().asString();
//                    userProfile.setName(s);
//                    break;
//                case MAP:
//                    MapValue map = v.asMapValue();
//                    userProfile.setRelatives((HashMap<String, Integer>) map);
//                case ARRAY:
//                    ArrayValue list =  v.asArrayValue();
//            }
//            out.println("User message pack: " + userProfile.toString());
//        }
//        unpacker.close();
    }
}

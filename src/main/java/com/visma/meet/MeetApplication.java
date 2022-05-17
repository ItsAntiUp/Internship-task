package com.visma.meet;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.visma.meet.dao.Holder;
import com.visma.meet.dao.Utility;
import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MeetApplication {
	private static final String personFilePath = "personSaveFile.json";
	private static final String meetingFilePath = "meetingSaveFile.json";

	public static void main(String[] args) {
		SpringApplication.run(MeetApplication.class, args);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
			try{
				return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			} catch (DateTimeParseException e){
				return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ"));
			}
		}).setPrettyPrinting().create();

		try {
			Type personType = new TypeToken<List<Person>>() {}.getType();
			Type meetingType = new TypeToken<List<Meeting>>() {}.getType();

			List<Person> personHolder = new ArrayList<>();
			List<Meeting> meetingHolder = new ArrayList<>();

			JsonReader personReader = new JsonReader(new FileReader(personFilePath));
			JsonReader meetingReader = new JsonReader(new FileReader(meetingFilePath));

			//Trying to parse data from json file
			try {
				personHolder = gson.fromJson(personReader, personType);
			}
			catch (JsonSyntaxException e){
				System.out.println(Utility.MSG_NO_PEOPLE_FOUND);
			}

			try {
				meetingHolder = gson.fromJson(meetingReader, meetingType);
			}
			catch (JsonSyntaxException e){
				System.out.println(Utility.MSG_NO_MEETINGS_FOUND);
			}

			Holder.setPersonHolder(personHolder);
			Holder.setActiveMeetings(meetingHolder);

			personReader.close();
			meetingReader.close();
		}
		catch (IOException e){
			System.err.println(Utility.ERR_IO_READING);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Writer personWriter = new FileWriter(personFilePath);
					Writer meetingWriter = new FileWriter(meetingFilePath);

					gson.toJson(Holder.getPersonHolder(), personWriter);
					gson.toJson(Holder.getActiveMeetings(), meetingWriter);

					personWriter.close();
					meetingWriter.close();
				}
				catch (IOException e){
					System.err.println(Utility.ERR_IO_WRITING);
				}
			}
		}));
	}
}

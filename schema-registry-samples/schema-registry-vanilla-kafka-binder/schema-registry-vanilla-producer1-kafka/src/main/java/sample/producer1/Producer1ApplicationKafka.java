package sample.producer1;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import com.example.Sensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableSchemaRegistryClient
@RestController
public class Producer1ApplicationKafka {

	private Random random = new Random();

	BlockingQueue<Sensor> unbounded = new LinkedBlockingQueue<>();

	public static void main(String[] args) {
		SpringApplication.run(Producer1ApplicationKafka.class, args);
	}

	private Sensor randomSensor() {
		Sensor sensor = new Sensor();
		sensor.setId(UUID.randomUUID().toString() + "-v1");
		sensor.setAcceleration(random.nextFloat() * 10);
		sensor.setVelocity(random.nextFloat() * 100);
		sensor.setTemperature(random.nextFloat() * 50);
		return sensor;
	}

	@RequestMapping(value = "/messages", method = RequestMethod.POST)
	public String sendMessage() {
		unbounded.offer(randomSensor());
		return "ok, have fun with v1 payload!";
	}

	@Bean
	public Supplier<Sensor> supplier() {
		return () -> unbounded.poll();
	}
}


package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);


	}

	@Bean
	public CommandLineRunner initData(PlayerRepository pRepository, GameRepository gRepository, GamePlayerRepository gpRepository, ShipRepository shRepository, SalvoRepository saRepository) {
		return (args) -> {
			// save a couple of players, games, gameplayers and ships.

			//Players
			Player jack = new Player("Jack@gmail.com", "Jack");
			Player chloe = new Player("Chloe@gmail.com", "Chloe");
			Player michelle = new Player("Michelle@gmail.com", "Michelle");
			Player john = new Player("John@gmail.com", "John");
			Player luciana = new Player("Luciana@gmail.com", "Luciana");
			Player peter = new Player("Peter@gmail.com", "Peter");

			//Games
			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();

			//Gameplayers
			GamePlayer gameplayer1 = new GamePlayer(game1,jack);
			GamePlayer gameplayer2 = new GamePlayer(game1,chloe);

			GamePlayer gameplayer3 = new GamePlayer(game2,michelle);
			GamePlayer gameplayer4 = new GamePlayer(game2,john);

			GamePlayer gameplayer5 = new GamePlayer(game3,luciana);
			GamePlayer gameplayer6 = new GamePlayer(game3,peter);

			//Ships
			Ship destructor1 = new Ship("destructor", Arrays.asList("A1", "A2", "A3"),gameplayer1);
			Ship submarino1 = new Ship("submarino", Arrays.asList("B1", "B2", "B3"),gameplayer1);
			Ship acorazado1 = new Ship("acorazado", Arrays.asList("C1", "C2", "C3"),gameplayer1);
			Ship destructor2 = new Ship("carrier", Arrays.asList("D2", "D3", "D4","D5"),gameplayer2);
			Ship submarino2 = new Ship("corveta", Arrays.asList("F3", "F4", "F5"),gameplayer2);
			Ship acorazado2 = new Ship("lancha", Arrays.asList("D7", "D8"),gameplayer2);

			//Salvos
			Salvo salvo11 = new Salvo(3, gameplayer1, Arrays.asList("C1", "C2", "C3"));
			Salvo salvo12 = new Salvo(3, gameplayer1, Arrays.asList("D1", "D2", "D3"));
			Salvo salvo13 = new Salvo(3, gameplayer1, Arrays.asList("F1", "G5", "J8"));
			Salvo salvo22 = new Salvo(3, gameplayer2, Arrays.asList("D1", "D2", "D3"));
			Salvo salvo23 = new Salvo(3, gameplayer2, Arrays.asList("F5", "F3", "G5"));
			Salvo salvo3 = new Salvo(3, gameplayer3, Arrays.asList("C1", "C2", "C3"));
			Salvo salvo4 = new Salvo(3, gameplayer4, Arrays.asList("C1", "C2", "C3"));
			Salvo salvo5 = new Salvo(3, gameplayer5, Arrays.asList("C1", "C2", "C3"));
			Salvo salvo6 = new Salvo(3, gameplayer6, Arrays.asList("C1", "C2", "C3"));


			//Save in repositories.
			pRepository.save(jack);
			pRepository.save(chloe);
			pRepository.save(michelle);
			pRepository.save(john);
			pRepository.save(luciana);
			pRepository.save(peter);
			gRepository.save(game1);
			gRepository.save(game2);
			gRepository.save(game3);
			gpRepository.save(gameplayer1);
			gpRepository.save(gameplayer2);
			gpRepository.save(gameplayer3);
			gpRepository.save(gameplayer4);
			gpRepository.save(gameplayer5);
			gpRepository.save(gameplayer6);
			shRepository.save(destructor1);
			shRepository.save(submarino1);
			shRepository.save(acorazado1);
			shRepository.save(destructor2);
			shRepository.save(submarino2);
			shRepository.save(acorazado2);
			saRepository.save(salvo11);
			saRepository.save(salvo12);
			saRepository.save(salvo13);
			saRepository.save(salvo22);
			saRepository.save(salvo23);
			saRepository.save(salvo3);
			saRepository.save(salvo4);
			saRepository.save(salvo5);
			saRepository.save(salvo6);
		};
	}
}

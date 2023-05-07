package application;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.Thread;


public class SpaceInvaders extends Application {
	
	//variables
	private static final Random RAND = new Random();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 60;
	static final Image PLAYER_IMG = new Image("file:src/imagenes/nave.png"); 
	static final Image EXPLOSION_IMG = new Image("file:src/imagenes/explosion.png");
	
	//tamano de las bombas
	static final int EXPLOSION_W = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	
	// imagenes de las bombas
	static final Image BOMBS_IMG[] = {
			new Image("file:src/imagenes/1.png"),
			new Image("file:src/imagenes/2.png"),
			new Image("file:src/imagenes/3.png"),
			new Image("file:src/imagenes/4.png"),
			new Image("file:src/imagenes/5.png"),
			new Image("file:src/imagenes/6.png"),

	};
	
	final int MAX_BOMBS = 10,  MAX_SHOTS = MAX_BOMBS * 2;
	// boleano final juego
	boolean gameOver = false;
	// Graficos java fx
	private GraphicsContext gc;
	
	// Jugador y arraylist de tiros, universo y bombas
	Rocket player;
	List<Shot> shots;
	List<Universe> univ;
	List<Bomb> Bombs;
	
	// movimiento y score
	private double mouseX;
	private int score;

	//start
	public void start(Stage stage) throws Exception {
		// creamos un canvas
		Canvas canvas = new Canvas(WIDTH, HEIGHT);	
		gc = canvas.getGraphicsContext2D();
		// Timeline del juego
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		// cursor
		canvas.setCursor(Cursor.MOVE);
		canvas.setOnMouseMoved(e -> mouseX = e.getX());
		canvas.setOnMouseClicked(e -> {
			if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
			if(gameOver) { 
				gameOver = false;
				setup();
			}
		});
		setup();
		stage.setScene(new Scene(new StackPane(canvas)));
		stage.setTitle("Space Invaders");
		stage.show();
		
	}

	//cargar juego
	private void setup() {
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		Bombs = new ArrayList<>();
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
		score = 0;
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);
	}
	
	//correr graficos
	private void run(GraphicsContext gc) {
		gc.setFill(Color.grayRgb(20));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		gc.fillText("Puntuación: " + score, 60,  20);
	 
		// cuando finalice el juego
		if(gameOver) {
			gc.setFont(Font.font(35));
			gc.setFill(Color.YELLOW);
			gc.fillText("Game Over! \n Tu puntuación ha sido de: " + score + " puntos.", WIDTH / 2, HEIGHT /2.5);
		
			menu_juego_login.recibirscore(score);
			try {
				menu_juego_login.GuardarScoreBBDD();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.exit();
	
		}
		
		
		
		
		univ.forEach(Universe::draw); // Dibuja cada objeto del universo en la pantalla.

		player.update(); // Actualiza el estado del jugador.
		player.draw(); // Dibuja al jugador en la pantalla.
		player.posX = (int) mouseX; // Establece la posición horizontal del jugador en función de la posición del mouse.

		Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
		    if(player.colide(e) && !player.exploding) {
		        player.explode(); // Si el jugador colisiona con una bomba, hace que el jugador explote.
		    }
		}); // Actualiza y dibuja cada bomba en la pantalla. Si una bomba colisiona con el jugador, hace que el jugador explote.

		for (int i = shots.size() - 1; i >=0 ; i--) { // Para cada disparo en la lista de disparos.
		    Shot shot = shots.get(i); // Obtener el disparo en la posición actual.
		    if(shot.posY < 0 || shot.toRemove)  { 
		        shots.remove(i); // Si el disparo llega al borde superior de la pantalla o necesita ser eliminado, eliminar el disparo.
		        continue;
		    }
		    shot.update(); // Actualiza el estado del disparo.
		    shot.draw(); // Dibuja el disparo en la pantalla.
		    for (Bomb bomb : Bombs) { // Para cada bomba en la lista de bombas.
		        if(shot.colide(bomb) && !bomb.exploding) {
		            score++; // Si el disparo colisiona con una bomba, incrementa la puntuación del juego.
		            bomb.explode(); // Hace que la bomba explote.
		            shot.toRemove = true; // Marca el disparo para su eliminación.
		        }
		    }
		}

		for (int i = Bombs.size() - 1; i >= 0; i--){  
		    if(Bombs.get(i).destroyed)  {
		        Bombs.set(i, newBomb()); // Si la bomba está destruida, crear una nueva bomba en su lugar.
		    }
		}

		gameOver = player.destroyed; // Si el jugador ha sido destruido, el juego ha terminado.

		if(RAND.nextInt(10) > 2) {
		    univ.add(new Universe()); // Agregar un nuevo objeto del universo a la lista de universos de vez en cuando.
		}

		for (int i = 0; i < univ.size(); i++) {
		    if(univ.get(i).posY > HEIGHT)
		        univ.remove(i); // Si el objeto del universo ha llegado al borde inferior de la pantalla, eliminarlo de la lista.
		}
	}

	//Jugadlr
	public class Rocket {

		/* 
		 * Variables de la clase Rocket. 
		 * posX y posY representan la posición de la nave en el eje x e y,
		 * respectivamente. size representa el tamaño de la nave. 
		 * exploding y destroyed son variables booleanas que indican si 
		 * la nave está explotando y si está destruida, respectivamente. 
		 * img es la imagen de la nave. explosionStep es un entero que se 
		 * utiliza para controlar el estado de la animación de explosión. 
		 *  
		 */

		int posX, posY, size;
		boolean exploding, destroyed;
		Image img;
		int explosionStep = 0;
		
	/* 
	 * Constructor de la clase Rocket. 
	 * Este método se llama al crear una instancia de la clase Rocket 
	 * y establece los valores iniciales de las variables. 
	 * 
	 */
		
		public Rocket(int posX, int posY, int size,  Image image) {
			this.posX = posX;
			this.posY = posY;
			this.size = size;
			img = image;
		}
		
	/* 
	 * Método shoot. Este método devuelve un nuevo objeto de tipo 
	 * Shot que representa el disparo de la nave. 
	 */
		
		public Shot shoot() {
			return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
		}

	/* 
	 * Método update. Este método se llama en cada iteración del juego 
	 * y actualiza las variables de la nave según su comportamiento. 
	 * 
	 */
		
		public void update() {
			if(exploding) explosionStep++;
			destroyed = explosionStep > EXPLOSION_STEPS;
		}
		
	/*
	 *  Método draw. Este método se llama en cada iteración del juego 
	 * y dibuja la nave en la pantalla. Si la nave está explotando, 
	 * se dibuja la animación de explosión.
	 *  
	 */
		
		public void draw() {
			if(exploding) {
				gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
						EXPLOSION_W, EXPLOSION_H,
						posX, posY, size, size);
			}
			else {
				gc.drawImage(img, posX, posY, size, size);
			}
		}

	/* 
	 * Método colide. Este método se llama para comprobar si 
	 * la nave colisiona con otra nave.
	 *  
	 */
		
		public boolean colide(Rocket other) {
			int d = distance(this.posX + size / 2, this.posY + size /2, 
							other.posX + other.size / 2, other.posY + other.size / 2);
			return d < other.size / 2 + this.size / 2 ;
		}
		
	/* 
	 * Método explode. Este método se llama cuando la nave es destruida 
	 * y activa la animación de explosión.
	 *  
	 */
		public void explode() {
			exploding = true;
			explosionStep = -1;
		}

	}
	
	// Clase que representa un proyectil de tipo "bomba"
	public class Bomb extends Rocket {
	    // Velocidad de la bomba en función de la puntuación
	    int SPEED = (score/5)+2;

	    public Bomb(int posX, int posY, int size, Image image) {
	        super(posX, posY, size, image);
	    }

	    // Actualiza la posición de la bomba en cada fotograma
	    public void update() {
	        super.update();
	        if(!exploding && !destroyed) posY += SPEED; // Si no está explotando ni destruida, avanza en el eje Y a la velocidad establecida
	        if(posY > HEIGHT) destroyed = true; // Si se sale de la pantalla, se considera destruida
	    }
	}

	// Clase que representa una bala disparada por el jugador
	public class Shot {
	    // Indica si la bala debe eliminarse porque ha impactado o se ha salido de la pantalla
	    public boolean toRemove;

	    int posX, posY, speed = 10;
	    static final int size = 6;

	    public Shot(int posX, int posY) {
	        this.posX = posX;
	        this.posY = posY;
	    }

	    // Actualiza la posición de la bala en cada fotograma
	    public void update() {
	        posY-=speed; // La bala avanza en el eje Y a la velocidad establecida
	    }

	    // Dibuja la bala en la pantalla
	    public void draw() {
	        gc.setFill(Color.RED);
	        if (score >=50 && score<=70 || score>=120) {
	            gc.setFill(Color.YELLOWGREEN);
	            speed = 50; // Si la puntuación del jugador está dentro de ciertos valores, cambia el color de la bala y aumenta su velocidad
	            gc.fillRect(posX-5, posY-10, size+10, size+30); // Dibuja un rectángulo en lugar de un óvalo
	        } else {
	            gc.fillOval(posX, posY, size, size);
	        }
	    }

	    // Comprueba si la bala ha impactado con un objeto de tipo Rocket
	    public boolean colide(Rocket Rocket) {
	        int distance = distance(this.posX + size / 2, this.posY + size / 2, 
	                Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
	        return distance  < Rocket.size / 2 + size / 2; // Si la distancia entre el centro de la bala y el centro del objeto Rocket es menor que la suma de sus radios, se considera impacto
	    } 
	}

	// Clase que representa un objeto del fondo (universo)
	public class Universe {
	    int posX, posY;
	    private int h, w, r, g, b;
	    private double opacity;

	    public Universe() {
	        posX = RAND.nextInt(WIDTH);
	        posY = 0;
	        w = RAND.nextInt(5) + 1;
	        h =  RAND.nextInt(5) + 1;
	        r = RAND.nextInt(100) + 150;
	        g = RAND.nextInt(100) + 150;
	        b = RAND.nextInt(100) + 150;
	        opacity = RAND.nextFloat();
	        if(opacity < 0) opacity *=-1;
	        if(opacity > 0.5) opacity = 0.5;
	    }
	    
	    // Dibuja el objeto del fondo
		public void draw() {
			if(opacity > 0.8) opacity-=0.01;
			if(opacity < 0.1) opacity+=0.01;
			gc.setFill(Color.rgb(r, g, b, opacity));
			gc.fillOval(posX, posY, w, h);
			posY+=20;
		}
	}
	
	// creacion de bombas
	Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)]);
	}
	
	int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
	
	//main para poder ejecutar por separado el juego
	public static void main(String[] args) {
		launch();
	}
}
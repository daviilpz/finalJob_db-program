package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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

public class SpaceInvaders extends Application {
	
	//variables
	private static final Random RAND = new Random();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 60;
	static final Image PLAYER_IMG = new Image("file:src/imagenes/nave.png"); 
	static final Image EXPLOSION_IMG = new Image("file:src/imagenes/explosion.png");
	static final int EXPLOSION_W = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	
	static final Image BOMBS_IMG[] = {
			new Image("file:src/imagenes/1.png"),
			new Image("file:src/imagenes/2.png"),
			new Image("file:src/imagenes/3.png"),
			new Image("file:src/imagenes/4.png"),
			new Image("file:src/imagenes/5.png"),
			new Image("file:src/imagenes/6.png"),

	};
	
	final int MAX_BOMBS = 10,  MAX_SHOTS = MAX_BOMBS * 2;
	boolean gameOver = false;
	private GraphicsContext gc;
	
	Rocket player;
	List<Shot> shots;
	List<Universe> univ;
	List<Bomb> Bombs;
	
	private double mouseX;
	private int score;

	//start
	public void start(Stage stage) throws Exception {
		// Crear un nuevo objeto Canvas con el ancho y alto especificados
		Canvas canvas = new Canvas(WIDTH, HEIGHT);

		// Obtener el contexto gráfico 2D del canvas
		gc = canvas.getGraphicsContext2D();

		// Crear un nuevo Timeline con una KeyFrame que se ejecuta cada 100 milisegundos 
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));

		// Establecer el número de veces que se repetirá el ciclo del Timeline (en este caso, de forma indefinida)
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Iniciar el Timeline
		timeline.play();

		// Establecer el cursor del canvas a "MOVE"
		canvas.setCursor(Cursor.MOVE);

		// Manejar el evento de movimiento del ratón sobre el canvas
		canvas.setOnMouseMoved(e -> mouseX = e.getX());

		// Manejar el evento de clic del ratón sobre el canvas
		canvas.setOnMouseClicked(e -> {

		    // Si la lista de disparos es menor que el máximo permitido, añadir un nuevo Shot a la lista
		    if(shots.size() < MAX_SHOTS) shots.add(player.shoot());

		    // Si el juego ha terminado, restablecer el juego
		    if(gameOver) {
		        gameOver = false;
		        setup();
		    }
		});

		// Establecer la configuración inicial del juego
		setup();

		// Crear una nueva escena con el StackPane que contiene el canvas
		stage.setScene(new Scene(new StackPane(canvas)));

		// Establecer el título de la ventana
		stage.setTitle("Space Invaders");

		// Mostrar la ventana
		stage.show();

	}

	//setup the game
	private void setup() {
		// Inicializar las listas de universos, disparos y bombas
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		Bombs = new ArrayList<>();

		// Crear un nuevo objeto Rocket en la posición central inferior de la pantalla
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);

		// Establecer la puntuación del juego a 0
		score = 0;

		// Generar MAX_BOMBS bombas y añadir cada una a la lista de bombas
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);

	}
	
	// Método que se encarga de dibujar y actualizar el juego
	private void run(GraphicsContext gc) {
	    // Rellena el fondo de gris oscuro
	    gc.setFill(Color.grayRgb(20));
	    gc.fillRect(0, 0, WIDTH, HEIGHT);
	    
	    // Dibuja el puntaje del jugador en la parte superior de la pantalla
	    gc.setTextAlign(TextAlignment.CENTER);
	    gc.setFont(Font.font(20));
	    gc.setFill(Color.WHITE);
	    gc.fillText("Puntuacion: " + score, 60,  20);
	    
	    // Si el juego terminó, dibuja el mensaje de "Game Over" y detiene la actualización del juego
	    if (gameOver) {
	        gc.setFont(Font.font(35));
	        gc.setFill(Color.YELLOW);
	        gc.fillText("Game Over! \n Tu puntuacion es: " + score + " \n Haz click para jugar de nuevo.", WIDTH / 2, HEIGHT / 2.5);
	        // return;
	    }
	    
	    // Dibuja el universo
	    univ.forEach(Universe::draw);
	    
	    // Actualiza y dibuja al jugador
	    player.update();
	    player.draw();
	    player.posX = (int) mouseX;
	    
	    // Comprueba si el jugador choca con una bomba y si es así, lo destruye
	    Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
	        if (player.colide(e) && !player.exploding) {
	            player.explode();
	        }
	    });
	    
	    // Actualiza y dibuja los disparos del jugador, y comprueba si golpean a una bomba
	    for (int i = shots.size() - 1; i >= 0 ; i--) {
	        Shot shot = shots.get(i);
	        if (shot.posY < 0 || shot.toRemove)  { 
	            shots.remove(i);
	            continue;
	        }
	        shot.update();
	        shot.draw();
	        for (Bomb bomb : Bombs) {
	            if (shot.colide(bomb) && !bomb.exploding) {
	                score++;
	                bomb.explode();
	                shot.toRemove = true;
	            }
	        }
	    }
	    
	    // Comprueba si las bombas fueron destruidas y las reemplaza por nuevas
	    for (int i = Bombs.size() - 1; i >= 0; i--) {  
	        if (Bombs.get(i).destroyed)  {
	            Bombs.set(i, newBomb());
	        }
	    }
	    
	    // Comprueba si el jugador fue destruido y establece el valor de gameOver
	    gameOver = player.destroyed;
	    
	    // Agrega nuevos universos al juego con cierta probabilidad
	    if (RAND.nextInt(10) > 2) {
	        univ.add(new Universe());
	    }
	    
	    // Elimina los universos que están fuera de la pantalla
	    for (int i = 0; i < univ.size(); i++) {
	        if (univ.get(i).posY > HEIGHT)
	            univ.remove(i);
	    }
	}


	//player
	public class Rocket {

	    // Atributos de la nave
	    int posX, posY, size;
	    boolean exploding, destroyed;
	    Image img;
	    int explosionStep = 0;
	    
	    // Constructor de la nave
	    public Rocket(int posX, int posY, int size, Image image) {
	        this.posX = posX;
	        this.posY = posY;
	        this.size = size;
	        img = image;
	    }
	    
	    // Método para disparar un proyectil desde la nave
	    public Shot shoot() {
	        return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
	    }

	    // Método para actualizar la nave
	    public void update() {
	        if(exploding) explosionStep++;
	        destroyed = explosionStep > EXPLOSION_STEPS;
	    }
	    
	    // Método para dibujar la nave en la pantalla
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

	    // Método para verificar si la nave colisiona con otra nave
	    public boolean colide(Rocket other) {
	        int d = distance(this.posX + size / 2, this.posY + size /2, 
	                        other.posX + other.size / 2, other.posY + other.size / 2);
	        return d < other.size / 2 + this.size / 2 ;
	    }
	    
	    // Método para explotar la nave
	    public void explode() {
	        exploding = true;
	        explosionStep = -1;
	    }

	}

	//computer player
	public class Bomb extends Rocket {
		
		int SPEED = (score/5)+2; // La velocidad de la bomba depende de la puntuación del jugador

		public Bomb(int posX, int posY, int size, Image image) {
			super(posX, posY, size, image);
		}

		public void update() {
			super.update();
			if(!exploding && !destroyed) posY += SPEED; // Si no está explotando ni ha sido destruida, la bomba se mueve hacia abajo
			if(posY > HEIGHT) destroyed = true; // Si la bomba se sale de la pantalla, se marca como destruida
		}

	}

	//bullets
	public class Shot {
		
		// Atributos de la clase
		public boolean toRemove; // Indica si el disparo debe ser eliminado
		int posX, posY, speed = 10; // Posición y velocidad del disparo
		static final int size = 6; // Tamaño del disparo
				
		// Constructor de la clase Shot
		public Shot(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}

		// Método para actualizar la posición del disparo
		public void update() {
			posY -= speed; // El disparo se mueve hacia arriba
		}
			

		// Método para dibujar el disparo en el canvas
		public void draw() {
			gc.setFill(Color.RED); // Se establece el color rojo por defecto
			
			// Si la puntuación está entre 50 y 70 o es mayor o igual a 120, se cambia el color y la velocidad del disparo
			if (score >= 50 && score <= 70 || score >= 120) { 
				gc.setFill(Color.YELLOWGREEN);
				speed = 50; // Se aumenta la velocidad del disparo
				gc.fillRect(posX-5, posY-10, size+10, size+30); // Se dibuja un rectángulo en lugar de un óvalo
			} else {
				gc.fillOval(posX, posY, size, size); // Se dibuja un óvalo
			}
		}
			
		// Método que detecta si el disparo ha colisionado con un cohete
		public boolean colide(Rocket Rocket) {
			int distance = distance(this.posX + size / 2, this.posY + size / 2, 
					Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2); // Se calcula la distancia entre el disparo y el cohete
			return distance  < Rocket.size / 2 + size / 2; // Si la distancia es menor que la suma de los radios, hay colisión
		} 

		
	}
	
	//environment
	public class Universe {
		int posX, posY; // posición del universo en el eje X y Y
		private int h, w, r, g, b; // altura, ancho, valores de color RGB
		private double opacity; // valor de opacidad del universo
		
		public Universe() {
		    posX = RAND.nextInt(WIDTH); // posición aleatoria en el eje X dentro de los límites de la pantalla
		    posY = 0; // posición inicial en el borde superior de la pantalla
		    w = RAND.nextInt(5) + 1; // ancho aleatorio de 1 a 6
		    h = RAND.nextInt(5) + 1; // altura aleatoria de 1 a 6
		    r = RAND.nextInt(100) + 150; // valor de rojo aleatorio de 150 a 249
		    g = RAND.nextInt(100) + 150; // valor de verde aleatorio de 150 a 249
		    b = RAND.nextInt(100) + 150; // valor de azul aleatorio de 150 a 249
		    opacity = RAND.nextFloat(); // valor de opacidad aleatorio entre 0.0 y 1.0
		    if(opacity < 0) opacity *=-1; // si la opacidad es negativa, se convierte a positiva
		    if(opacity > 0.5) opacity = 0.5; // si la opacidad es mayor que 0.5, se ajusta a 0.5
		}

		public void draw() {
		    if(opacity > 0.8) opacity-=0.01; // disminuye la opacidad si es mayor que 0.8
		    if(opacity < 0.1) opacity+=0.01; // aumenta la opacidad si es menor que 0.1
		    gc.setFill(Color.rgb(r, g, b, opacity)); // establece el color y la opacidad del universo
		    gc.fillOval(posX, posY, w, h); // dibuja el universo como una elipse en la posición actual
		    posY+=20; // mueve el universo hacia abajo en 20 píxeles
		}

	}
	
	
	/* Método que crea una nueva instancia de la clase Bomb y la inicializa con valores aleatorios
	 en cuanto a su posición, tamaño y la imagen que la representa.
	 */
	Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)]);
	}

	/* Método que calcula la distancia entre dos puntos en el plano cartesiano usando la fórmula
	 	de la distancia euclidiana.
	*/
	int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	// Método principal que inicia la aplicación de JavaFX
	public static void main(String[] args) {
		launch();
	}

}
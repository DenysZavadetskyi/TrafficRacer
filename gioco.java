package gioco;

import java.util.Hashtable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class gioco extends Application {
	
	// Foto dei giocatori da sceglire 
	int Macchina;
	Image player1 = new Image(getClass().getResourceAsStream("player.png"));
	Image player2 = new Image(getClass().getResourceAsStream("player1.png"));
	Image player3 = new Image(getClass().getResourceAsStream("player2.png"));
	Image player4 = new Image(getClass().getResourceAsStream("player3.png"));
	Image player5 = new Image(getClass().getResourceAsStream("player4.png"));
	Image player6 = new Image(getClass().getResourceAsStream("player5.png"));
	Image player7 = new Image(getClass().getResourceAsStream("player6.png"));
	ImageView player= new ImageView(player1);
	
	// Foto della strada 
	Image strada1 = new Image(getClass().getResourceAsStream("strada.jpg"));
	ImageView strada = new ImageView(strada1);
	ImageView strada3 = new ImageView(strada1);
	
	// Foto delle macchine degli avversari
	Image car1 = new Image(getClass().getResourceAsStream("car1.png"));
	ImageView macchina1 = new ImageView(car1);
	Image car2 = new Image(getClass().getResourceAsStream("car2.png"));
	ImageView macchina2 = new ImageView(car2);
	Image car3 = new Image(getClass().getResourceAsStream("car3.png"));
	ImageView macchina3 = new ImageView(car3);
	Image car4 = new Image(getClass().getResourceAsStream("car4.png"));
	ImageView macchina4 = new ImageView(car4);
	
	// Dizionario delle macchine che posso scegliere
	Hashtable<Integer, Image> SceltaMacchine = new Hashtable<Integer, Image>();
	
	// Questo timeline si usa per vedere quanti kilometri la macchina ha già percorso
	Timeline distanza=new Timeline(new KeyFrame(Duration.seconds(3), x -> distanza()));
	
	// Questo timeline si usa per far partire il gioco
	Timeline timeline=new Timeline(new KeyFrame(Duration.seconds(0.025), x -> aggiorna()));
	
	// Questa modalita di nomero segna i kilometri passati nell'interfaccia del gioco 
	double kilometri=0.0;
	Label lkilometri = new Label();
	
	// Questo è il pannello di gioco
	Pane panello;
	
	// Questa label segna la velocità della macchina
	Label lvelocità = new Label();
	
	// Questa modalità di numero indica la posizione delle 2 strade che scorrono
	int posStrada1;
	int posStrada2;
	
	// Queste 2 variabili indicano la posizione del player sulla strada
	double posautoX = 50;
	double posautoY = 225;
	
	// Queste 2 variabili indicano la posizione delle auto che non devo tamponare 
	int posizione1 = 1400;
	int posizione2 = 1050;
	
	// Indica la velocita della maccchina
	int velocità;
	
	// Indica in modo casuale l'ordina di uscita delle macchine 
	int num = 1;
	
	// Indca la rotazione della macchina per farla curvare fluidamente
	int rotazione = 0;
	
	// Segna sull'interfaccia del gioco la velocità
	int v =0;
	
	// Si usa per scorrere la macchina da scegliere nel garage
	int i=0;
	
	// Uso il boolean per cominciare la rotazione della macchina
	boolean giocatore = true;
	
	// Velocità massima che l'auto raggionge quando è attiva la timeline
	public static final int MAX_V = 20;
	
	// Entrambe si usano per non far uscire il player dalla strada 
	public static final int MAX_TOP = -40;
	public static final int MAX_BOTTON = 410;
	
	// Questo button ti riporta nell'interfaccia iniziale quando perdi
	Button pmenu = new Button();
	
	// Questo button fa ricominciare il game
	Button prestart = new Button();
	
	// Segna quanti punti faccio 
	int punti =0;
	Label lpunti = new Label();
	
	// Questo frammento di codice nterfaccia principale
	public void start(Stage finestra) {

		Pane panello = new Pane();
		panello.setPrefSize(900, 500);
		panello.getStyleClass().add("pane");

		Button pgioca = new Button();
		pgioca.setMinHeight(50);
		pgioca.setMinWidth(150);
		pgioca.setText("GIOCA");
		pgioca.setLayoutX(700);
		pgioca.setLayoutY(150);
		panello.getChildren().add(pgioca);
		pgioca.setOnAction(e -> gioca());

		Button pintruduzione = new Button();
		pintruduzione.setText("COME SI GIOCA");
		pintruduzione.setMinWidth(150);
		pintruduzione.setMinHeight(50);
		pintruduzione.setLayoutX(700);
		pintruduzione.setLayoutY(350);
		panello.getChildren().add(pintruduzione);
		pintruduzione.setOnAction(e -> intruduzione());
		
		Button pmachine = new Button();
		pmachine.setText("GARAGE");
		pmachine.setMinWidth(150);
		pmachine.setMinHeight(50);
		pmachine.setLayoutX(700);
		pmachine.setLayoutY(250);
		panello.getChildren().add(pmachine);
		pmachine.setOnAction(e -> garage());

		Scene scena = new Scene(panello);
		finestra.setScene(scena);
		scena.getStylesheets().add("gioco/Css.css");
		finestra.setTitle("Traffic Racer");
		finestra.show();
	}
	
	// Questo frammento di codice indica la creazione di una nuova finestra che viene aperta dal button pmacchine 
	public class Garage extends Stage {
		public Garage() {
			
			// Aggiunge nel dizionario le immagini delle macchine
			SceltaMacchine.put(0, player1);
			SceltaMacchine.put(1, player2);
			SceltaMacchine.put(2, player3);
			SceltaMacchine.put(3, player4);
			SceltaMacchine.put(4, player5);
			SceltaMacchine.put(5, player6);
			SceltaMacchine.put(6, player7);
			
			GridPane griglia = new GridPane();
			griglia.getStyleClass().add("garage");
		
			Button pAvanti = new Button(">");
			pAvanti.setOnAction(e -> Avanti());
			Button pIndietro = new Button("<");
			pIndietro.setOnAction(e -> Indietro());
			Button pConferma= new Button("CONFERMA SCELTA");
			pConferma.setOnAction(e -> Conferma());
			
			
			griglia.add(pIndietro, 0, 1);
			griglia.add(pAvanti, 2, 1);
			griglia.add(player, 1, 1);
			griglia.add(pConferma, 1, 2);
			griglia.setAlignment(Pos.CENTER);
			griglia.setVgap(10);
			griglia.setHgap(10);
			
			Scene scene = new Scene(griglia, 500, 500);
			scene.getStylesheets().add("gioco/Css.css");
			setTitle("Traffic Racer");
			setScene(scene);
		}
		
		// Questa funzione serve per il button che scorre le macchine in avanti nel garage 
		private void Avanti() {
			i++;
			
			if(i > 6) {
				i=0;
			}
			player.setImage( SceltaMacchine.get(i));
		}
		
		// Questa funzione serve per il button che scorre le macchine all'indietro nel garage
		private void Indietro() {
			i--;
			
			if(i < 0) {
				i=6;
			}
			player.setImage(SceltaMacchine.get(i));
		}
		
		// Questa funzione conferma la scelta di macchina nel garage
		private void Conferma() {
			Macchina = i; 
			player.setImage(SceltaMacchine.get(Macchina));
			
		}
	}
			
	// Questa funzione sull'interfaccia principale tramite il button pmacchine fa aprire il garage
	private void garage() {
		Garage finestra = new Garage();
		finestra.show();
	}
	
	// Questa funzione tramite il button pintroduzione apre un interfaccia con le regole del gioo e come funziona
	public class Intruduzione extends Stage {
		public Intruduzione() {
			Image tasto1 = new Image(getClass().getResourceAsStream("tasto1.jpg"));
			ImageView tasti1 = new ImageView(tasto1);
			Image tasto2 = new Image(getClass().getResourceAsStream("tasto2.jpg"));
			ImageView tasti2 = new ImageView(tasto2);
			Image tasto3 = new Image(getClass().getResourceAsStream("tasto3.jpg"));
			ImageView tasti3 = new ImageView(tasto3);
			Image tasto4 = new Image(getClass().getResourceAsStream("tasto4.jpg"));
			ImageView tasti4 = new ImageView(tasto4);
			GridPane griglia = new GridPane();
			Label testo = new Label("INTRODUZIONE");

			Label testo1 = new Label("Benvenuti nalla pagina relativa ai trucchi di Traffic Racer."
					+ " Vi mostriamo tutto il materiale che abbiamo fatto.");

			Label testo2 = new Label("SPIEGAZIONE TASTI");
			Label testo3 = new Label("Quando premi questo pulsante,l'auto aumenta velocità");
			Label testo4 = new Label("Quando premi questo pulsante,l'auto gira a sinistra");

			Label testo5 = new Label("Quando premi questo pulsante,l'auto gira a destra");
			Label testo6 = new Label("Quando premi questo pulsante,l'auto riduce velocità");

			Label testo7 = new Label(
					"L'auto deve sorpassare altre auto per non colpirle, più auto passato, più punti ottiene.");
			Label testo8 = new Label("Quando colpisci un'altra macchina , il gioco finisce.");
			Label testo9 = new Label("Per iniziare il gioco,devi prima sceglire un'auto nel garage");
			tasti1.setFitWidth(50);
			tasti1.setFitHeight(50);
			tasti2.setFitWidth(50);
			tasti2.setFitHeight(50);
			tasti3.setFitWidth(50);
			tasti3.setFitHeight(50);
			tasti4.setFitWidth(50);
			tasti4.setFitHeight(50);

			griglia.add(testo, 1, 0);
			griglia.add(testo1, 0, 1, 3, 1);
			griglia.add(testo2, 1, 2);
			griglia.add(tasti1, 0, 3);
			griglia.add(testo3, 1, 3);
			griglia.add(tasti2, 0, 4);
			griglia.add(testo4, 1, 4);
			griglia.add(tasti3, 0, 5);
			griglia.add(testo5, 1, 5);
			griglia.add(tasti4, 0, 6);
			griglia.add(testo6, 1, 6);
			griglia.add(testo7, 0, 8, 2, 8);
			griglia.add(testo8, 0, 10, 2, 10);
			griglia.add(testo9, 0, 12,2,12);	
			griglia.setAlignment(Pos.CENTER);
			griglia.setHgap(10);
			griglia.setVgap(10);
			griglia.getStyleClass().add("grid");

			Scene scene = new Scene(griglia, 1000, 700);
			scene.getStylesheets().add("gioco/Css.css");
			setTitle("Traffic Racer");
			setScene(scene);
		}
	}

	// Questa funzione tramite il button pgioca apra l'inrfaccia dell'inizio del gioco 
	public class Gioca extends Stage {
		public Gioca() {
			panello = new Pane();
			panello.setPrefSize(1000, 813);
			panello.getStyleClass().add("strada");
			panello.getChildren().add(strada3);
			panello.getChildren().add(strada);
			posStrada1 = 0;
			posStrada2 = 550;
			lkilometri.setLayoutX(700);
			lkilometri.setLayoutY(60);
			panello.getChildren().add(lkilometri);
			lpunti.setLayoutX(500);
			lpunti.setLayoutY(60);
			panello.getChildren().add(lpunti);
			player.setX(posautoX);
			player.setY(posautoY);
			lvelocità.setLayoutX(70);
			lvelocità.setLayoutY(60);
			panello.getChildren().add(lvelocità);
			strada.setX(posStrada1);
			strada.setY(0);
			strada3.setX(posStrada2);
			strada3.setY(0);			
			
			panello.getChildren().add(player);
			macchina1.setX(posizione1);
			macchina1.setY(200);
			panello.getChildren().add(macchina1);
			macchina2.setX(posizione2);
			macchina2.setY(550);
			panello.getChildren().add(macchina2);
			macchina3.setX(posizione2);
			macchina3.setY(180);
			panello.getChildren().add(macchina3);
			macchina4.setX(posizione1);
			macchina4.setY(520);
			panello.getChildren().add(macchina4);

			Scene scena = new Scene(panello);
			setScene(scena);
			scena.getStylesheets().add("gioco/Css.css");
			scena.setOnKeyPressed(e -> pressione(e));
			scena.setOnKeyReleased(e -> lascia(e));

			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
		    distanza.setCycleCount(Timeline.INDEFINITE);
			distanza.play();
			setTitle("Traffic Racer");

		}
	}
	
	// Questa è una funzione della timeline che segna quanti kilometri abbiamo passato
	private void distanza() {
		if(velocità>0) {
		 kilometri = kilometri+ 0.1;
		}
		
	}

	// Questa funzione serve per quando si premono i tasti per andare avanti, indietro, destra o sinistra
	public void pressione(KeyEvent e) {
		if (e.getCode() == KeyCode.UP) {
			velocità = velocità + 1;

		}
		if (e.getCode() == KeyCode.DOWN) {
			velocità = velocità - 1;

		}
		if (e.getCode() == KeyCode.RIGHT) {
			posautoY = posautoY + 20;
			rotazione = 5;
			player.setImage(SceltaMacchine.get(Macchina));

		}
		if (e.getCode() == KeyCode.LEFT) {
			posautoY = posautoY - 20;
			rotazione = -5;
			player.setImage(SceltaMacchine.get(Macchina));
			
		}
		if (giocatore) {
			player.setRotate(rotazione);
		}
	}

	// Questa funzione serve quando lascio il tasto 
	public void lascia(KeyEvent e) {
		if (e.getCode() == KeyCode.UP) {
			velocità = 0;
		}
		if (e.getCode() == KeyCode.DOWN) {
			velocità = 0;
		}
		if (e.getCode() == KeyCode.RIGHT) {
			player.setImage(player1);
			rotazione = 0;
			player.setImage(SceltaMacchine.get(Macchina));
		}
		if (e.getCode() == KeyCode.LEFT) {
			player.setImage(player1);
			rotazione = 0;
			player.setImage(SceltaMacchine.get(Macchina));
		}
		player.setRotate(rotazione);
	}

	// Questa funzione tramite il button pgioca apre la finestra dell'inizio gioco 
	private void gioca() {
		Gioca finestra = new Gioca();
		finestra.show();
		

	}

	// Questa funzione serve per far partire il gioco 
	private void aggiorna() {
		lpunti.setText("Punti:  "+punti);
		if(velocità>0) {
			if(v<200) {
		v=v+1;
				}
		}else {
			if(velocità<MAX_V) {
			if(v>0) {
				v=v-1;
			}
			}
		}
		lvelocità.setText(""+v+" "+"km/h");
		 lkilometri.setText("Distanza: "+kilometri+" km");
		if (num == 1) {
			macchina1.setX(posizione1);
			macchina1.setY(200);

		}
		if (num == 2) {
			macchina2.setX(posizione2);
			macchina2.setY(550);

		}
		if (num == 3) {
			macchina3.setX(posizione2);
			macchina3.setY(180);
		}
		if (num == 4) {
			macchina4.setX(posizione1);
			macchina4.setY(520);
		}
		if (velocità > 0) {
			posizione1 = posizione1 - velocità;
			posizione2 = posizione2 - velocità;
		}
		if (posizione1 < -50 || posizione2 < -50) {
			num = (int) (Math.random() * 5);
			posizione1 = 1400;
			posizione2 = 1050;
			punti = punti +100;
			macchina1.setX(posizione1);
			macchina1.setY(180);
			macchina2.setX(posizione2);
			macchina2.setY(570);
			macchina3.setX(posizione2);
			macchina3.setY(160);
			macchina4.setX(posizione1);
			macchina4.setY(500);
		}

		// Questo comando si usa per capire quando le macchine sbandano
		Bounds b = player.getBoundsInParent();
		Bounds b1 = macchina1.getBoundsInParent();
		Bounds b2 = macchina2.getBoundsInParent();
		Bounds b3 = macchina3.getBoundsInParent();
		Bounds b4 = macchina4.getBoundsInParent();
		if (b.intersects(b1) || b.intersects(b2) || b.intersects(b3) || b.intersects(b4)) {
			timeline.stop();
			distanza.stop();
			posizione1=1400;
			posizione2=1500;
			gameover();
		}

		// Questi comandi si usano per la rotazione della macchina
		if (posautoY > 99 && posautoY < 569) {
			if (rotazione > 0) {
				posautoY += 1.5;
			}
			if (rotazione < 0) {
				posautoY -= 1.5;
			}
			if (rotazione > 5) {
				posautoY += 1.8;
			}
			if (rotazione < -5) {
				posautoY -= 1.8;
			}

		}
		if (velocità <= 0) {
			velocità = 0;
		}
		
		// Questi controlli mettono un limite di velocità e non fanno uscire la macchina dalla strada
		if (velocità > MAX_V) {
			velocità = MAX_V;
		}
		if (posautoY <= MAX_TOP) {
			posautoY = MAX_TOP;
		}
		if (posautoY >= MAX_BOTTON) {
			posautoY = MAX_BOTTON;
		}
		posStrada1 = posStrada1 - velocità;
		strada.setLayoutX(posStrada1);
		posStrada2 = posStrada2 - velocità;
		strada3.setLayoutX(posStrada2);
		player.setX(posautoX);
		player.setY(posautoY);
		
		// Questi controlli fanno si che quando la strada scorre la riportano all'inizio per far si che vada all'infinito
		if (posStrada2 <= 0) {
			posStrada1 = 0;
			posStrada2 = 550;
		} else {
			posStrada1 = posStrada1 - velocità;
			posStrada2 = posStrada2 - velocità;
		}
	}
	
	// Queta funzione quando sbando apre una finestra di fine gioco che mi dice quanti km ho fatto e quanti punti ho fatto
	public class GameOver extends Stage {
		
			public GameOver() {
				GridPane griglia = new GridPane();
				griglia.getStyleClass().add("game");
				Label text2 = new Label();
				text2.setText("Kilometri passati:  "+kilometri+" km");
				Label text3 = new Label();
				text3.setText("Punti:"+punti);
				prestart.setText("RESTART");
				prestart.setOnAction(e -> restart());
				prestart.setMinWidth(150);
				prestart.setMinHeight(50);
				prestart.getStyleClass().add("colorebutton");
				pmenu.setText("CONTINUA");
				pmenu.setOnAction(e -> chiudere());  
				pmenu.setMinWidth(150);
				pmenu.setMinHeight(50);
				pmenu.getStyleClass().add("colorebutton");
				
				griglia.add(text2, 0, 20, 6, 20);
				griglia.add(pmenu, 0, 0);
				griglia.add(prestart, 6, 0);
				griglia.add(text3, 1, 23, 6, 23);
				griglia.setAlignment(Pos.CENTER);
				griglia.setHgap(10);
				griglia.setVgap(10);
				
				Scene scene = new Scene(griglia, 500, 500);
				scene.getStylesheets().add("gioco/Css.css");
				setTitle("Traffic Racer");
				setScene(scene);
		}
	}
	
	// Questa funzione tramite il button prestart ricmincia il gioco dopo aver perso
	private void restart() {
		
		 // Con questi comandi chiudo finestra di GameOver e ricomincio all'inizio il gioco
		 Stage a=(Stage) pmenu.getScene().getWindow();
		 a.close();
		 reinizializza();
	}
	
	// Questa funione tramite il button pmenu chiude le finestre e ci riporta in quella iniziale
	private void chiudere() {
		
		 // Con questi comandi chiudo tutte le finestre aperte e torno in quellla iniziale 
		 Stage a=(Stage) pmenu.getScene().getWindow();
		 a.close();
		 a=(Stage) strada.getScene().getWindow();
		 a.close();
	}
		
	// Questa funzione apre la finestra del GameOver quado sbando
	private void gameover() {
		GameOver finestra = new GameOver();
		finestra.show();
	}
	
	// Questa funzione tramite il button pintroduzione apre la finestra delle regole e del funzionamento del gioco 
	private void intruduzione() {
		Intruduzione finestra = new Intruduzione();
		finestra.show();

	}
	
	// Questa funzione fa ripartire da 0 il gioco tramite il button prestart
    public void reinizializza() {
	
	posStrada1 = 0;
	posStrada2 = 550;
	
	lkilometri.setLayoutX(700);
	lkilometri.setLayoutY(60);
	
	lpunti.setLayoutX(500);
	lpunti.setLayoutY(60);

	player.setX(posautoX);
	player.setY(posautoY);
	lvelocità.setLayoutX(70);
	lvelocità.setLayoutY(60);
	
	strada.setX(posStrada1);
	strada.setY(0);
	strada3.setX(posStrada2);
	strada3.setY(0);			
	
	
	macchina1.setX(posizione1);
	macchina1.setY(200);
	
	macchina2.setX(posizione2);
	macchina2.setY(550);
	
	macchina3.setX(posizione2);
	macchina3.setY(180);
	
	macchina4.setX(posizione1);
	macchina4.setY(520);
	velocità=0;
	v=0;
	punti=0; 
	kilometri=0.0;
	
	
	timeline.play();
    
	distanza.play();
}
	public static void main(String[] args) {
		launch(args);
	}
}
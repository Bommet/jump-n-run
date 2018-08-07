import sas.*;
import java.awt.Color;
public class Spielwelt
{
  final static int bHoehe = 800; 
  final static int bBreite = bHoehe+bHoehe/2;
  private View view;
  private Boden boden;
  private Player mario;
  private Hindernis[] hindernis;
  private List<Hindernis> kollisionen;
  private int yBeschleunigung = 1;
  private double spielerpositionX = 0;
  private double spielerpositionY = 0;
  private boolean zurueckgesetzt = false;
  private boolean weitergerueckt = false;

  public Spielwelt()
  {
    //Test
    view = new View(bBreite,bHoehe, "Ultra Mario");
    view.setBackgroundColor(Color.CYAN);

    int x = view.getHeight()/10*2;
    boden = new Boden(0, view.getHeight()-x, x);

    hindernis = new Hindernis[10];
    hindernis[0] = new Block((int)boden.getShapeWidth()/2, (int)boden.getShapeY());
    hindernis[0].move(0, -hindernis[0].getShapeHeight());
    for(int i=1; i<hindernis.length; i++)
    {
      hindernis[i] = new Block((int)hindernis[i-1].getShapeX()+(int)hindernis[i-1].getShapeX()/2, (int)hindernis[i-1].getShapeY());
    }

    kollisionen = new List<Hindernis>();
    for(int i=0; i<hindernis.length; i++)
    {
      kollisionen.append(hindernis[i]);
    }

    mario = new Player(50, (int)boden.getShapeY());
    mario.move(0, -mario.getShapeHeight());

    spielerpositionX = 0;
    spielerpositionY = 0;
    this.fuehreAus();
  }

  //Programmstart
  public void fuehreAus()
  {
    while(!view.keyBackspacePressed()){
      this.bewegung();
      this.kollisionWeiter();
    }
  }

  //Bewegung
  public void bewegung()
  {
    int i = 0;
    kollisionen.toFirst();
    this.zurueck();
    if(view.keyUpPressed()) i=1; 
    if(view.keyRightPressed()) i=2;
    if(view.keyLeftPressed()) i=3;
    if(view.keyUpPressed() && view.keyRightPressed()) i=4;
    if(view.keyUpPressed() && view.keyLeftPressed()) i=5;
    if(!boden.gibKollision(mario) || kollisionen.getContent().gibKollision(mario)) i=6;

    switch(i){
      case 1:
      this.positionSpeichern();
      mario.springen(0);
      mario.setzeGravitation();
      view.wait(25);
      break;

      case 2:
      this.positionSpeichern();
      mario.laufeRechts();
      this.kamerabewegung();
      this.zurueck();
      break;

      case 3:
      this.positionSpeichern();
      mario.laufeLinks();
      this.zurueck();
      break;

      case 4:
      mario.springen(mario.gibXTempo());
      mario.setzeGravitation();
      break;

      case 5:
      mario.springen(-(mario.gibXTempo()));
      mario.setzeGravitation();
      break;

      case 6:
      this.positionSpeichern();
      if(view.keyLeftPressed()) mario.laufeLinks();
      if(view.keyRightPressed()) mario.laufeRechts();
      this.zurueck();
      if((mario.getShapeY()+mario.getShapeHeight()+1) != kollisionen.getContent().getShapeY()) mario.gravitation();
      break;

      default:
      view.keyBufferDelete();
      break;
    }
    view.wait(25);
  }

  public void kamerabewegung()
  {
    for(int i=0; i<hindernis.length; i++)
    {
      hindernis[i].move(-(mario.gibXTempo()));
    }
  }

  //Kollisionsverwaltung
  public void kollisionWeiter()
  {
    if(mario.getShapeX() > kollisionen.getContent().getCenterX()+kollisionen.getContent().getShapeWidth())
    {
      kollisionen.next();
    }
  }

  public void positionSpeichern()
  {
    spielerpositionX = mario.getShapeX();
    spielerpositionY = mario.getShapeY();
  }

  public void zurueck()
  {
    if(kollisionen.getContent().gibKollision(mario))
    {
      mario.moveTo(spielerpositionX, spielerpositionY-1);
      zurueckgesetzt = true;
    }
    kollisionen.next();
    if(kollisionen.getContent().gibKollision(mario))
    {
      mario.moveTo(spielerpositionX, spielerpositionY-1);
      zurueckgesetzt = true;
    }
  }

  //Getter und Setter
  public View gibView()
  {
    return View.getView();
  }

  public static int gibHoehe()
  {
    return bHoehe;
  }

  public static int gibBreite()
  {
    return bBreite;
  }
}

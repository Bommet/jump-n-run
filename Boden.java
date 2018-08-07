import sas.*;
import java.awt.Color;
public class Boden extends Hindernis
{

    public Boden(int pX, int pY, int pHoehe)
    {
        super(pX, pY, Spielwelt.gibBreite(), pHoehe, Color.GREEN);
    }

}
